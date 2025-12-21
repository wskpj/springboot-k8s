# 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /builder

# Gradle Wrapper & 설정 파일 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle

# 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐싱)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

# 실행 권한 부여
RUN chmod +x ./gradlew

# 소스 코드 복사
COPY src src

# 프로젝트 빌드 (테스트 생략)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar -x test --no-daemon

# 레이어 추출
# 생성된 JAR 파일을 4개의 계층으로 분리
RUN java -Djarmode=layertools -jar build/libs/*.jar extract

# ------------------------------------------------

# 실행 스테이지
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 빌드 스테이지에서 분리된 레이어 복사
# 1. 외부 라이브러리
COPY --from=builder /builder/dependencies/ ./
# 2. 스프링 부트 로더
COPY --from=builder /builder/spring-boot-loader/ ./
# 3. 스냅샷 의존성
COPY --from=builder /builder/snapshot-dependencies/ ./
# 4. 소스 코드
COPY --from=builder /builder/application/ ./

# 컨테이너 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
