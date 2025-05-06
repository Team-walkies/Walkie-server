package site.walkies.walkie.domain.spot.service;

import java.time.Duration;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.util.stream.Collectors;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import site.walkies.walkie.domain.spot.entity.Spot;
import site.walkies.walkie.domain.spot.entity.SpotPhoto;
import site.walkies.walkie.domain.spot.repository.SpotPhotoRepository;
import site.walkies.walkie.domain.spot.repository.SpotRepository;

@Service
@RequiredArgsConstructor
public class SpotPhotoService {

    private final SpotRepository spotRepository;
    private final SpotPhotoRepository spotPhotoRepository;
    private final RestTemplate restTemplate;

    @Value("${kakao-key}")
    private String kakaoApiKey;

    private WebDriver driver;
    private WebDriverWait wait;

    // 크롬 드라이버 설정
    @PostConstruct
    public void initWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @PreDestroy
    public void quitWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    // 사진과 상세 url이 없는 경우 사용하는 함수
    // 사진이 없는 spot 확인 => 상세 url 생성 => 사진 생성
    public void enrichSpotPhotos() {
        // 모든 spot 담기
        List<Spot> spots = spotRepository.findAll();

        // spot 순회
        spots.parallelStream().forEach(spot -> {
            // spot 사진이 있는 경우 pass
            if (!spotPhotoRepository.findBySpotId(spot.getId()).isEmpty()) {
                return;
            }

            // spot 상세 url을 카카오 api에서 가져오기
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
                String placeUrl = docs.get(0).get("place_url").asText();

                // 응답이 있는 경우 상셍 url 저장
                spot.changeDetailUrl(placeUrl);
                spotRepository.save(spot);

                // 상세 url을 통해서 크롤링 후 사진 저장
                crawlAndSave(spot);

            }
        });
    }

    // 상세 url이 전부 있는 경우 사진만 추가하는 함수
    public void enrichPhotosFromDetailUrls() {
        // 모든 스팟돌기
        spotRepository.findAll().stream()
                // 상세 url이 없으면 pass
                .filter(s -> s.getDetailUrl() != null && !s.getDetailUrl().isBlank())
                // 사진이 이미 있으면 pass
                .filter(s -> spotPhotoRepository.findBySpotId(s.getId()).isEmpty())
                .forEach(this::crawlAndSave);
    }

    // 크롤링 후 저장 함수
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void crawlAndSave(Spot spot) {
        // 뒤에 사진 url을 붙여 사진 화면으로 이동
        String url = spot.getDetailUrl() + "#photoview";
        try{
            driver.get(url);
            
            // 사진 element가 나올때 까자 대기
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.list_photo")));

            // img들 찾기
            List<WebElement> imgs = driver.findElements(By.cssSelector("ul.list_photo li img"));
            
            // img의 링크 저장
            List<String> imageUrls = imgs.stream()
                    .map(img -> img.getAttribute("src"))
                    .filter(src -> src != null && !src.isEmpty())
                    .collect(Collectors.toList());

            // img 링크 DB에 저장
            imageUrls.forEach(src -> {
                SpotPhoto photo = new SpotPhoto();
                photo.setSpot(spot);
                photo.setPhotoUrl(src);
                spotPhotoRepository.save(photo);
            });
        } catch (Exception e) {
            // 저장 실패 url 저장
            System.out.println(url);
        }
    }
}
