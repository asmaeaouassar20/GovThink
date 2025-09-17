# Utiliser une image OpenJDK officielle
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Installer Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Exposer le port
EXPOSE 8080

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=dev

# Démarrage simple
CMD ["mvn", "spring-boot:run"]