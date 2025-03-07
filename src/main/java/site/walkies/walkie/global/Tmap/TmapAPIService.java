package site.walkies.walkie.global.Tmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import site.walkies.walkie.global.web.exception.CustomException;
import site.walkies.walkie.global.web.exception.ErrorCode;

@Service
public class TmapAPIService {

    @Value("${tmap-key}")
    private String tmapKey;

    // Tmap api를 통해서 좌표를 시도로 변환해주는 함수
    // input : latitude(위도), longitude(경도)
    // outPut : position
    public String convertGeoToString(double latitude, double longitude) {
        String tmapUrl = "https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=1&lat="
                + latitude + "&lon=" + longitude;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("appKey", tmapKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(tmapUrl, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseBody);
                JsonNode addressInfo = root.path("addressInfo");

                String city = addressInfo.path("city_do").asText();
                String district = addressInfo.path("gu_gun").asText();

                return city + " " +district;
            }
        } catch (Exception e) {
            // API 호출 또는 JSON 파싱 에러 처리
            e.printStackTrace();
            throw new CustomException(ErrorCode.TMAP_SERVER_ERROR);
        }
        return "";
    }
}
