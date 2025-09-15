# Utiliser l'image officielle Node.js version 20, basée sur Alpine Linux
# Alpine = une version légère de Linux
# Donc notre conteneur aura Node.js et npm déjà installés
FROM node:20-alpine

# Définir le dossier /app comme dossier de travail à l'intérieur du conteneur
# Toutes les commandes qui suivent (COPY, RUN ...) s'exécuteront dans /app
WORKDIR /app

# Copier juste les fichiers package.json et package-lock.json du projet ( du PC vers /app dans le conteneur )
# C'est utile pour installer les dépendances avant de copier tout le code
COPY package.json package-lock.json ./

# Installer les dépendances du projet (celles dans package-lock.json)
RUN npm ci

# Copier tout le projet dans /app (après les dépendances)
# PS : il vaux mieux mettre un .dockerignore pour ne pas tout copier (node_modules, .git...)
COPY . .

# Indiquer que l'app tourne sur le port 4200 (pardéfaut Angular ng serve)
# ça ne publie pas le port, ça sert justede documentation (le vrai mapping se fait avec docker run -p 4200:4200)
EXPOSE 4200

# Définir la commande à exécuter quand le conteneur démarre
# "npm run start" qui (dans ANgular) lance généralement ng serve
CMD ["npm","run","start"]





