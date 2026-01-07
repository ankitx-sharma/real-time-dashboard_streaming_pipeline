# ---- build stage ----
FROM node:20-alpine AS build
WORKDIR /app

COPY frontend/package*.json /app/
RUN npm ci

COPY frontend /app/

ARG VITE_API_BASE_URL=""
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

RUN npm run build

# ---- run stage ----
FROM nginx:alpine
WORKDIR /usr/share/nginx/html

COPY --from=build /app/dist /usr/share/nginx/html
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80