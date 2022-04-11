# 가슴속 삼천원 백엔드

![Version](https://img.shields.io/github/v/release/depromeet/3dollars-in-my-pocket-backend?include_prereleases)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fgjbae1212%2Fhit-counterhttps%3A%2F%2Fgithub.com%2F3dollar-in-my-pocket%2F3dollars-in-my-pocket-backend&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=3dollar-in-my-pocket_3dollars-in-my-pocket-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=3dollar-in-my-pocket_3dollars-in-my-pocket-backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=3dollar-in-my-pocket_3dollars-in-my-pocket-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=3dollar-in-my-pocket_3dollars-in-my-pocket-backend)

![img.png](images/logo.png)

### 프로젝트 설명

전국 붕어빵 등 길거리 음식을 파는 곳을 알려주는 [**가슴속 3천원**](https://intro.threedollars.co.kr/)

**"가슴 속 3천원"** 은 겨울철 우리 가슴속에 지니고 다니는 3천원을 털어가는 붕어빵, 문어빵, 계란빵, 호떡 등을 파는 곳을 포함해 길거리 음식점들을 알려주는 사용자 기반 서비스입니다.

### 앱 다운로드

- [AppStore](https://apps.apple.com/kr/app/%EA%B0%80%EC%8A%B4%EC%86%8D3%EC%B2%9C%EC%9B%90-%EB%82%98%EC%99%80-%EA%B0%80%EA%B9%8C%EC%9A%B4-%EB%B6%95%EC%96%B4%EB%B9%B5/id1496099467)
- [PlayStore](https://play.google.com/store/apps/details?id=com.zion830.threedollars)

<p align="center">
    <img src="https://user-images.githubusercontent.com/7058293/110067262-b179c700-7db6-11eb-8451-223956dca69d.jpg" width="40%" alt="IOS 인증샷"/>
    <img src="./images/appstore.png" width="40%" alt="IOS 인증샷">
</p>

## 기술 스택

### 애플리케이션 (Language & Framework)

- Java 11, Kotlin 1.6
- Spring Framework (Spring Boot, Spring MVC, Spring Batch)
- JPA/Hibernate, QueryDSL
- MariaDB, flyway
- MongoDB, Redis, Caffeine Cache
- Feign Client
- Gradle
- Junit5, AssertJ, Mockito

### 인프라 아키텍처

![img.png](images/3dollars-architecture-20220409.png)

- ECS Fargate (API, Batch)
- MariaDB(RDS), MongoDB(EC2), Redis Cluster(Elastic Cache)
- S3, CloudFront
- Application LoadBalancer
- GitHub Actions CI/CD

### 운영 툴

- 로그 및 모니터링: AWS CloudWatch
- 에러 모니터링 & 알림: Sentry, Slack
- APM 도구: NewRelic
- 부하 테스트 도구: Locust

### 멀티 모듈 구조

![img.png](images/modules-20220307.png)

## Developers & Contacts

- will.seungho@gmail.com (백엔드 개발자, [강승호](https://github.com/seungh0))
- 3dollarinmypocket@gmail.com (가슴속 삼천원 대표 메일)
