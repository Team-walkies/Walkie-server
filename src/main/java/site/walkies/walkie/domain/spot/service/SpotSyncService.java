package site.walkies.walkie.domain.spot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.enums.SpotKeyword;
import site.walkies.walkie.domain.spot.repository.SpotPhotoRepository;
import site.walkies.walkie.domain.spot.repository.SpotRepository;

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

    private final WebClient webClient;

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
    
    // 대한민국의 모든 범위 탐색을 위해서 20km 반경 검색을 했을때를 가정해 point 설정
    private List<Point> generateNationwideGrid() {
        List<Point> grid = new ArrayList<>();
        for (double lat = 33.6; lat <= 38.5; lat += 0.35) {
            for (double lon = 124.0; lon <= 130.0; lon += 0.4) {
                grid.add(new Point(lat, lon));
            }
        }
        return grid;
    }
    
    // tour api를 통해서 모든 spot 탐색
    @Transactional
    public void syncAllSpots() {
        int radius = 20000;     // 10km 반경
        int pageSize = 10000;   // 한 번에 최대 10,000개

        // 생성해둔 모든 위경도 값 순회
        for (Point p : generateNationwideGrid()) {
            int pageNo = 1;
            while (true) {
                try {
                    // tour api 호출
                    URI uri = UriComponentsBuilder
                            .fromHttpUrl(TOUR_API_URL)
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("_type", "json")
                            .queryParam("MobileOS", "ETC")
                            .queryParam("MobileApp", "walkie")
                            .queryParam("listYN", "Y")
                            .queryParam("arrange", "A")
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

                    
                    for (JsonNode item : items) {
                        String contentTypeId = item.path("contenttypeid").asText();
                        // 축제는 제외
                        if ("15".equals(contentTypeId)) {
                            continue;
                        }
                        
                        // 정의되지 않은 카테고리 제외
                        String cat1 = item.path("cat1").asText();
                        SpotKeyword kw = categoryMapping.get(cat1);
                        if (kw == null) {
                            continue; 
                        }
                        
                        // 이미 같은 이름의 spot이 있는 경우 pass
                        String title = item.path("title").asText();
                        if (spotRepository.existsByLocationName(title)) {
                            continue;
                        }

                        // Spot 엔티티 생성
                        Spot spot = Spot.builder()
                                .locationName(title)
                                .latitude(item.path("mapy").asDouble())
                                .longitude(item.path("mapx").asDouble())
                                .streetAddress(item.path("addr1").asText())
                                .keyword(kw)
                                .build();
                        
                        // spot 저장
                        saveOne(spot);
                    }
                    
                    // 뒤쪽 page가 남은 경우 다음 page를 검색
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
