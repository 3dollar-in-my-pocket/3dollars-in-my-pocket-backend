## Setup Disk Memory DB with docker-compose

### 1. 로컬 환경 구성 with docker-compose
```bash
docker-compose -f ./docker-compose.yml -p threedollar up -d
```

### 2. Run Application
```bash
./gradlew :threedollar-api-user:bootRun -Dspring.profiles.active=local-docker
```

### 3. Add Seed Data
threedollar-domain-rds/db/seed/seed.sql
