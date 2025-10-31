# ===== Etapa de build =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
# gera o jar sem rodar testes
RUN mvn -DskipTests package

# ===== Etapa de runtime =====
FROM eclipse-temurin:17-jre
WORKDIR /app
# copia o jar gerado na etapa de build (qualquer nome *.jar)
COPY --from=build /src/target/*.jar app.jar

# Cloud-style: app deve escutar na porta $PORT
ENV PORT=8080
EXPOSE 8080
CMD ["sh","-c","java -Dserver.port=${PORT} -jar app.jar"]
