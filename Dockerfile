FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests -B

RUN cp target/*.jar application.jar

RUN java -Djarmode=tools \
    -jar application.jar \
    extract \
    --layers \
    --destination extracted


FROM eclipse-temurin:17-jre

RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY --from=builder /app/extracted/dependencies/ ./
COPY --from=builder /app/extracted/spring-boot-loader/ ./
COPY --from=builder /app/extracted/snapshot-dependencies/ ./
COPY --from=builder /app/extracted/application/ ./

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]
