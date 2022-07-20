## Setup local-docker 환경

### 1. 로컬 환경 구성 with docker-compose

```bash
sh setup-local-docker.sh
docker-compose up -d
```

### 2. Run Application

```bash
./gradlew :threedollar-api-user-service:bootRun -Dspring.profiles.active=local-docker
```

### 3. Add Seed Data

- threedollar-domain/threedollar-domain-rds/db/seed/seed.sql
- threedollar-domain/threedollar-domain-mongo/db/seed/seed.js
