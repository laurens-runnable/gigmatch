FROM node:lts-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY tsconfig.json .
COPY src/ src/
RUN npm run build
CMD ["node", "build/index.js"]
