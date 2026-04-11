# Walkie-server

<div align="center">

**위치 기반 산책 게이미피케이션 서비스 Walkie의 백엔드 시스템**

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-6DB33F?logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.12.1-02303A?logo=gradle&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Docker-4479A1?logo=mysql&logoColor=white)

</div>

---

## 📌 프로젝트 개요

Walkie-server는 위치 기반 산책 게이미피케이션 서비스 **Walkie**의 핵심 백엔드 시스템입니다. Android 및 iOS 모바일 클라이언트, 그리고 지도 기능을 제공하는 웹뷰 클라이언트와 연동하여, 사용자의 산책 활동을 데이터화하고 이를 게임 콘텐츠로 확장하는 역할을 수행합니다.

### 관련 레포지토리

| 파트 | 레포지토리 |
| :--- | :--- |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/android/android-original.svg" width="16"/> **Android** | [Team-walkies/Walkie-Android](https://github.com/Team-walkies/Walkie-Android) |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/apple/apple-original.svg" width="16"/> **iOS** | [Team-walkies/Walkie-iOS](https://github.com/Team-walkies/Walkie-iOS) |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg" width="16"/> **Web (지도 웹뷰)** | [Team-walkies/Walkie-Frontend](https://github.com/Team-walkies/Walkie-Frontend) |

---

## 🛠 기술 스택

### Backend

| 기술 | 선택 이유 |
| :--- | :--- |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="16"/> **Java 21** | Record, Pattern Matching 등 최신 언어 기능을 활용하여 코드의 간결성과 생산성을 높이기 위해 선택했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="16"/> **Spring Boot 3.4.2** | 내장 서버와 자동 설정을 통해 빠르고 안정적인 애플리케이션 개발을 위해 사용했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="16"/> **Spring Data JPA** | 객체 지향적인 데이터 접근과 생산성 높은 쿼리 작성을 위해 채택했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="16"/> **Spring Security** | 인증 및 인가 로직을 표준화된 방식으로 안전하게 처리하기 위해 사용했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="16"/> **Spring WebFlux** | 외부 API(Kakao, Apple, Tmap 등)와의 비동기 논블로킹 HTTP 통신을 위해 사용했습니다. |
| **JWT (JSON Web Token)** | 모바일 환경에 적합한 무상태(Stateless) 인증 방식을 구현하기 위해 선택했습니다. |
| **Lombok** | 반복적인 보일러플레이트 코드를 줄여 가독성과 개발 생산성을 높이기 위해 사용했습니다. |

### Database

| 기술 | 선택 이유 |
| :--- | :--- |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="16"/> **MySQL** | 안정적이고 범용적인 관계형 데이터베이스로, 복잡한 도메인 간 데이터 관계를 관리하기 위해 사용했습니다. |

### Infrastructure & Tools

| 기술 | 선택 이유 |
| :--- | :--- |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/gradle/gradle-original.svg" width="16"/> **Gradle** | Groovy DSL 기반의 유연한 빌드 스크립트와 멀티 모듈 확장 가능성을 위해 선택했습니다. |
| **Swagger (SpringDoc)** | API 명세를 코드와 함께 자동으로 생성하여 클라이언트 팀과의 협업 효율을 높이기 위해 사용했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/firebase/firebase-plain.svg" width="16"/> **Firebase Admin SDK** | 모바일 기기로의 푸시 알림(FCM) 전송을 위해 통합했습니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/elasticsearch/elasticsearch-original.svg" width="16"/> **ELK Stack** | 단순 파일 로그만으로는 운영 중 실시간 이슈 추적이 어려워, Filebeat·Elasticsearch·Kibana를 통해 로그를 구조화하고 대시보드로 모니터링하기 위해 구축했습니다. |

### Others

| 기술 | 선택 이유 |
| :--- | :--- |
| **Uber H3** | 위치 데이터를 육각형 그리드로 효율적으로 인덱싱하여 스팟 방문 인증의 정확도와 성능을 높이기 위해 도입했습니다. |
| **Tmap Reverse Geocoding API** | GPS 좌표를 사용자에게 읽기 쉬운 주소(시/구) 문자열로 변환하기 위해 사용했습니다. |
| **한국관광공사 Tour API** | 전국 산책 스팟 데이터를 수집·동기화하기 위해 사용합니다. |
| **Kakao Local API** | Tour API로 수집한 스팟의 상세 페이지 URL을 조회하기 위해 사용합니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/discord/discord-original.svg" width="16"/> **Discord Webhook** | 서버 에러 발생 시 Discord 채널로 즉시 알림을 전달하여 팀 내 빠른 이슈 감지와 공유를 위해 사용합니다. |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/selenium/selenium-original.svg" width="16"/> **Jsoup / Selenium** | Kakao Local API로 조회한 URL을 크롤링하여 스팟 사진 데이터를 수집합니다. |

---

## 🏗 시스템 아키텍처

<img width="1610" height="1412" alt="diagram-export-2026 -4 -11 -오후-8_42_48" src="https://github.com/user-attachments/assets/e1880753-42fb-418e-b83c-6f69b01bafbb" />

Walkie 서비스는 클라이언트, 백엔드 서버, 데이터 저장소, 외부 서비스로 구성됩니다.

### 1. Backend Server (On-premise)

| 구성 요소 | 설명 |
| :--- | :--- |
| **Spring Boot Application** | 온프레미스 환경에서 운영되며, `/api/v1` 컨텍스트 경로로 핵심 비즈니스 로직과 API를 처리합니다. |
| **Authentication** | 모든 요청은 JWT 기반 인증을 거치며, 인증된 요청만 비즈니스 로직 계층으로 전달됩니다. |
| **Scheduled Tasks** | 매일 02:00 건강 데이터 정합성 보정, 03:00 탈퇴 유예 만료 회원 자동 삭제 등 정기 배치 작업을 수행합니다. |
| **Logging** | Logback이 로그를 파일로 기록하고, Filebeat가 수집하여 ELK Stack으로 전달합니다. |

### 2. Data Layer

| 구성 요소 | 설명 |
| :--- | :--- |
| **MySQL (Docker)** | 회원, 산책 기록, 스팟, 캐릭터 상태 등 주요 데이터를 저장합니다. |
| **Spring Data JPA** | 도메인 중심의 데이터 접근 구조를 유지하며 데이터베이스와 상호작용합니다. |

### 3. Logging & Monitoring

`Logback` → `app.log` → `Filebeat` → `Elasticsearch` → `Kibana`

각 컴포넌트(Filebeat, Elasticsearch, Kibana)는 개별 Docker 컨테이너로 운영됩니다.

### 4. External Services

| 서비스 | 용도 |
| :--- | :--- |
| **Kakao OAuth API** (`kapi.kakao.com`) | 소셜 로그인 사용자 정보 조회 |
| **Apple Auth Server** (`appleid.apple.com`) | Apple ID Token 검증용 공개키 조회 |
| **Firebase FCM** | 모바일 푸시 알림 전송 |
| **Tmap API** | GPS 좌표 → 주소 변환 (Reverse Geocoding) |
| **한국관광공사 Tour API** (`apis.data.go.kr`) | 전국 산책 스팟 데이터 수집·동기화 |
| **Kakao Local API** (`dapi.kakao.com`) | 스팟 상세 페이지 URL 조회 |
| **Discord Webhook** | 서버 에러 / 신규 가입 / 사용자 의견 알림 (채널별 분리) |

---

## 📁 프로젝트 구조

<details>
<summary>디렉토리 구조 펼치기</summary>

```
src/main/java/site/walkies/walkie/
├── domain/
│   ├── auth/           # 소셜 로그인(Kakao/Apple), JWT 발급
│   ├── member/         # 회원 정보 관리, 탈퇴, 자동 삭제 스케줄러
│   ├── health/         # 걸음 수/건강 데이터 기록, 일일 정합성 보정 스케줄러
│   ├── character/      # 캐릭터 육성 및 레벨업
│   ├── egg/            # 알(Egg) 아이템 생성·획득·부화
│   ├── spot/           # 스팟 조회, H3 기반 방문 인증, 경로 기록
│   ├── review/         # 스팟 리뷰 및 평점
│   ├── notification/   # FCM 푸시 알림
│   ├── event/          # 이벤트
│   ├── notice/         # 공지사항
│   └── internal/       # 내부 관리 API (스팟 동기화 트리거 등)
└── global/
    ├── auth/           # JWT 필터, 인증 유틸리티
    ├── config/         # Spring Security, Swagger 등 설정
    ├── web/            # 공통 응답 DTO, 예외 처리
    ├── Tmap/           # Tmap Reverse Geocoding 연동
    ├── webhook/        # Discord Webhook 연동
    ├── file/           # 파일 업로드 처리
    ├── logging/        # 로그 설정
    └── probability/    # 확률 기반 보상 로직
```

</details>

---

## ⚙️ 주요 기능

### 🔐 Authentication & Member

| 기능 | 설명 |
| :--- | :--- |
| **소셜 로그인/회원가입** | 카카오 및 애플 OAuth를 통한 간편 로그인 및 회원가입을 지원합니다. |
| **JWT 인증** | Access/Refresh Token 기반의 인증 및 토큰 재발급 기능을 제공합니다. |
| **회원 정보 관리** | 닉네임 변경, 프로필 조회, 회원 탈퇴 등 사용자 계정 관리 기능을 수행합니다. |
| **탈퇴 유예 자동 삭제** | 매일 03:00 탈퇴 유예 기간이 만료된 회원 데이터를 자동으로 영구 삭제합니다. |

### 📍 Spot & Location

| 기능 | 설명 |
| :--- | :--- |
| **스팟 조회** | 사용자 위치 기반으로 주변 산책 스팟 정보를 제공합니다. |
| **스팟 방문 인증** | GPS 좌표와 H3 인덱싱을 활용하여 사용자의 스팟 방문 여부를 검증합니다. |
| **경로 기록** | 사용자의 산책 경로 데이터를 저장하고 분석합니다. |
| **주소 변환** | Tmap Reverse Geocoding을 통해 GPS 좌표를 시/구 단위 주소로 변환합니다. |
| **스팟 데이터 동기화** | 한국관광공사 Tour API로 전국 스팟을 수집하고, Kakao Local API와 Selenium 크롤링으로 사진 데이터를 보강합니다. (내부 API 트리거) |

### 🎮 Character & Gamification

| 기능 | 설명 |
| :--- | :--- |
| **캐릭터 육성** | 산책 활동에 따라 캐릭터 경험치를 증가시키고 레벨업을 처리합니다. |
| **아이템(Egg) 관리** | 산책 중 획득한 알(Egg) 아이템의 생성·획득·부화 로직을 수행합니다. |
| **보상 시스템** | 일일 미션 달성, 스팟 방문 등에 따른 보상을 지급합니다. |

### 🏃 Health & Activity

| 기능 | 설명 |
| :--- | :--- |
| **건강 데이터 기록** | 사용자의 걸음 수 등 건강 활동 데이터를 기록하고 관리합니다. |
| **일일 정합성 보정** | 매일 02:00 전체 회원의 건강 데이터 정합성을 자동으로 보정합니다. |

### 💬 Community & Interaction

| 기능 | 설명 |
| :--- | :--- |
| **리뷰 작성** | 방문한 스팟에 대한 리뷰 및 평점을 등록하고 조회합니다. |
| **알림(Notification)** | FCM을 연동하여 이벤트, 보상 획득 등의 푸시 알림을 전송합니다. |
| **이벤트** | 기간 한정 이벤트를 생성하고 사용자 참여를 관리합니다. |
| **공지사항** | 서비스 공지사항을 등록하고 조회합니다. |
