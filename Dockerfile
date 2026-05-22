# Fase 1: Compilar el proyecto con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
# Entramos a la carpeta de tu backend antes de compilar
RUN cd EpresSmart_Backend && mvn clean package -DskipTests

# Fase 2: Ejecutar la aplicación con una imagen ligera de Java
FROM eclipse-temurin:21-jre
WORKDIR /app
# Buscamos el archivo .jar dentro de la subcarpeta donde se compiló
COPY --from=build /app/EpresSmart_Backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]