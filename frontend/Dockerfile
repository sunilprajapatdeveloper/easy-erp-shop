# ============================================
# MULTI-STAGE BUILD FOR VUE.JS
# ============================================

FROM node:18-alpine AS build-stage
WORKDIR /app

COPY package*.json ./
RUN npm ci --legacy-peer-deps

COPY . .

ENV NODE_ENV=production
ENV NODE_OPTIONS=--max-old-space-size=1024

# Build MUST succeed
RUN npm run build

# ================== NGINX ==================

FROM nginx:alpine
WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build-stage /app/dist ./
COPY nginx.conf /etc/nginx/conf.d/default.conf

RUN chmod -R 755 .

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
