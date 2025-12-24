# 빌드 스테이지
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /builder

# Gradle Wrapper & 설정 파일 복사
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle

# 각 모듈의 build.gradle 복사 (의존성 캐싱을 위함)
COPY libs/common-core/build.gradle libs/common-core/
COPY libs/event-core/build.gradle libs/event-core/
COPY libs/event-starter/build.gradle libs/event-starter/
COPY libs/jpa-core/build.gradle libs/jpa-core/
COPY libs/jpa-starter/build.gradle libs/jpa-starter/
COPY libs/logging-core/build.gradle libs/logging-core/
COPY libs/logging-starter/build.gradle libs/logging-starter/
COPY libs/security-core/build.gradle libs/security-core/
COPY libs/security-starter/build.gradle libs/security-starter/
COPY libs/web-core/build.gradle libs/web-core/
COPY libs/web-starter/build.gradle libs/web-starter/
COPY application/build.gradle application/

# 실행 권한 부여 및 의존성 다운로드 (의존성 레이어 캐싱)
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :application:dependencies --no-daemon

# 전체 소스 코드 복사
COPY libs libs
COPY application application

# 프로젝트 빌드 (테스트 생략)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :application:bootJar -x test --no-daemon

# 레이어 추출
RUN java -Djarmode=layertools -jar application/build/libs/*.jar extract --destination application/extracted

# 최종 실행 스테이지
FROM eclipse-temurin:17-jre-alpine

WORKDIR /application

# 추출된 레이어 복사
COPY --from=builder /builder/application/extracted/dependencies/ ./
COPY --from=builder /builder/application/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/application/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/application/extracted/application/ ./

EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
