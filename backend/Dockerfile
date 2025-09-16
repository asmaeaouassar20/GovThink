# Utiliser une image OpenJDK officielle
# openjdk : c'est le nom de l'image de base officielle qui contient Java - C'est comme dire "je veux Docker avec Java dedans"
# OpenJDK (Open Java Development Kit) c'est la version libre et gratuite de Java  "elle contient tout ce qu'il faut pour exécuter et compiler des application Java"
# 17-jdk-slim : est le tag d'une image Docker officielle OpenJDK qui contient Java 17 complet (JDK) dans une version allégée (slim)
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Installer Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Exposer le port de développement Spring Boot (par défaut 8080)
# Indiquer à Docker que ton conteneur va utiliser le port 8080 (informatif pour Docker et pour les autres développeurs)
# ça ne publie pas automatiquement le port à l'extérieur du conteneur
# Pour que je puisse accéder à l'app depuis ma machine il faut mapper le port au moment du "docker run"
EXPOSE 8080

# Indiquer à Docker de lancer Maven pour compiler et démarre automatiquement l'app Spring Boot lorsque le conteneur démarre
CMD ["mvn", "spring-boot:run"]





