# build
FROM node:22-alpine3.19 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install -g @angular/cli
RUN npm install --force
COPY . .
RUN ng build

# run
FROM nginx:alpine
COPY --from=builder /app/dist/sitpass-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]