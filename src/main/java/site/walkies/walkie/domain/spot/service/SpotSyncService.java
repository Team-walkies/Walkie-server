package site.walkies.walkie.domain.spot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.enums.SpotKeyword;
import site.walkies.walkie.domain.spot.repository.SpotPhotoRepository;
import site.walkies.walkie.domain.spot.repository.SpotRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpotSyncService {

    private static final String TOUR_API_URL =
            "http://apis.data.go.kr/B551011/KorService1/locationBasedList1";

    private final RestTemplate restTemplate;
    private final SpotRepository spotRepository;
    private final SpotPhotoRepository spotPhotoRepository;

    @Value("${tourapiKey}")
    private String serviceKey;

    @Value("${kakao-key}")
    private String kakaoApiKey;

    // cat1 → SpotKeyword 매핑
    private final Map<String, SpotKeyword> categoryMapping = Map.of(
            "A01", SpotKeyword.NATURE,
            "A02", SpotKeyword.HUMANITIES,
            "A03", SpotKeyword.SPORTS,
            "A04", SpotKeyword.SHOPPING,
            "A05", SpotKeyword.FOOD
    );

    /** 대한민국 전역을 스캔하는 격자 좌표 생성(예: lat 33~38.5, lon 124~131, step 0.2°) */
    private List<Point> generateNationwideGrid() {
        List<Point> grid = new ArrayList<>();
        for (double lat = 33.0; lat <= 38.5; lat += 0.35) {
            for (double lon = 124.0; lon <= 131.0; lon += 0.4) {
                grid.add(new Point(lat, lon));
            }
        }
        return grid;
    }
    @Transactional
    public void syncAllSpots() {
        int radius = 20000;     // 10km 반경
        int pageSize = 10000;   // 한 번에 최대 10,000개

        for (Point p : generateNationwideGrid()) {
            int pageNo = 1;
            while (true) {
                try {
                    URI uri = UriComponentsBuilder
                            .fromHttpUrl(TOUR_API_URL)
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("_type", "json")
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "walkie")
                            // 필수 옵션 추가
                            .queryParam("listYN", "Y")
                            .queryParam("arrange", "A")
                            // 위치 및 페이징
                            .queryParam("mapX", p.lon)
                            .queryParam("mapY", p.lat)
                            .queryParam("radius", radius)
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", pageSize)
                            .build(false)  // 중복 인코딩 방지
                            .toUri();

                    System.out.println(uri);

                    // JSON 응답 수신
                    JsonNode root = restTemplate.getForObject(uri, JsonNode.class);
                    if (root == null) break;

                    JsonNode items = root.path("response")
                            .path("body")
                            .path("items")
                            .path("item");
                    if (!items.isArray() || items.isEmpty()) {
                        break;
                    }

                    // 각 아이템 처리
                    for (JsonNode item : items) {
                        String contentTypeId = item.path("contenttypeid").asText();
                        if ("15".equals(contentTypeId)) {
                            continue;  // 축제/공연/행사 제외
                        }

                        String cat1 = item.path("cat1").asText();
                        SpotKeyword kw = categoryMapping.get(cat1);
                        if (kw == null) {
                            continue;  // 정의되지 않은 카테고리 무시
                        }

                        String title = item.path("title").asText();
                        if (spotRepository.existsByLocationName(title)) {
                            continue;  // 중복 저장 방지
                        }

                        // Spot 엔티티 생성
                        Spot spot = Spot.builder()
                                .locationName(title)
                                .latitude(item.path("mapy").asDouble())
                                .longitude(item.path("mapx").asDouble())
                                .streetAddress(item.path("addr1").asText())
                                .keyword(kw)
                                .build();

                        saveOne(spot);
                    }
                    // 전체 개수 대비 다음 페이지 여부 체크
                    int total = root.path("response").path("body").path("totalCount").asInt();
                    if (pageNo * pageSize >= total) {
                        break;
                    }
                    pageNo++;
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    @Transactional
    public void enrichSpotPhotos() {
        List<Spot> spots = spotRepository.findAll();
        for (Spot spot : spots) {
            // 1) 이미 사진이 있으면 건너뜀
            if (!spotPhotoRepository.findBySpotId(spot.getId()).isEmpty()) continue;

            // 2) 키워드 검색 API 호출
            String url = UriComponentsBuilder
                    .fromUriString("https://dapi.kakao.com/v2/local/search/keyword.json")
                    .queryParam("query", spot.getLocationName())
                    .queryParam("x", spot.getLongitude())
                    .queryParam("y", spot.getLatitude())
                    .queryParam("size", 1)
                    .build()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> resp = restTemplate.exchange(
                    url, HttpMethod.GET, entity, JsonNode.class);

            JsonNode docs = resp.getBody().path("documents");
            if (docs.isArray() && docs.size() > 0) {
                JsonNode doc = docs.get(0);
                String placeUrl = doc.get("place_url").asText();

//                // 3) 상세 페이지 크롤링하여 사진 URL 추출
//                List<String> imageUrls = crawlPlaceImages(placeUrl);
//
//                // 4) SpotPhoto 엔티티로 저장
//                for (String img : imageUrls) {
//                    SpotPhoto photo = new SpotPhoto();
//                    photo.setSpot(spot);
//                    photo.setPhotoUrl(img);
//                    spotPhotoRepository.save(photo);
//                }
            }
        }
    }

    // 단순 위경도 구조체
    @Getter
    @AllArgsConstructor
    static class Point {
        private double lat;
        private double lon;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOne(Spot spot) {
        spotRepository.save(spot);
    }
}
