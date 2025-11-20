# Use a imagem base do OpenJDK para a JVM
FROM openjdk:17-jdk-slim as build

# Define o diretório de trabalho
WORKDIR /app

# Copia o pom.xml e baixa as dependências
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copia o código fonte
COPY src ./src

# Empacota a aplicação em um JAR executável
RUN ./mvnw package -DskipTests

# Imagem final
FROM openjdk:17-jdk-slim
WORKDIR /app

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Copia o JAR da fase de build
COPY --from=build /app/target/*.jar app.jar

# Define o ponto de entrada para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
