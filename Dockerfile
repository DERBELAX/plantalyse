# Dockerfile

# 1. Utiliser l'image Java 17 officielle
FROM eclipse-temurin:21-jdk


# 2. Créer un dossier de travail dans le conteneur
WORKDIR /app

# 3. Copier le fichier .jar généré par Maven
COPY target/plantalysBackend-0.0.1-SNAPSHOT.jar app.jar

# 4. Définir la commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
