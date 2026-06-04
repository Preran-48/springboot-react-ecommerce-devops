````markdown
# 🛍️ Full Stack E-commerce Web Application

A full-stack **E-commerce application** using **Spring Boot** (Java) for the backend and **ReactJS with Vite** for the frontend.

This project demonstrates a complete **DevOps implementation** including:

- Docker containerization
- Multi-container deployment using Docker Compose
- Jenkins CI/CD pipeline automation
- MySQL database integration
- Persistent storage
- Image upload functionality
- Nginx configuration
- Kubernetes deployment (in progress)

---

# 📁 Project Structure

SpringBoot-Reactjs-Ecommerce-main/
│
├── Ecommerce-Backend/         # Spring Boot REST API backend
├── Ecommerce-Frontend/        # React + Vite frontend
├── docker-compose.yml         # Multi-container orchestration
├── Jenkinsfile                # Jenkins CI/CD pipeline
├── k8s/                       # Kubernetes manifests
└── README.md

---

# 🧩 Backend - Spring Boot

## 🔧 Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

---

# 📂 Backend Directory Structure

Ecommerce-Backend/
│
├── controller/                # REST API controllers
├── model/                     # JPA entity classes
├── repo/                      # Repository interfaces
├── service/                   # Business logic
├── resources/
│   └── application.properties
├── Dockerfile
└── pom.xml

---

# ⚙️ Backend Configuration

## application.properties

```properties
spring.application.name=ecom-proj

server.port=8081

spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/ecommerce}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root123}

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
````

---

# ▶️ Run Backend Application

```bash
cd Ecommerce-Backend

mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8081
```

---

# 🛢️ MySQL Database Integration

The application uses MySQL for persistent product storage.

---

# 🗄️ MySQL Database Configuration

## Local MySQL Configuration

| Property | Value     |
| -------- | --------- |
| Host     | localhost |
| Port     | 3306      |
| Username | root      |
| Password | root123   |
| Database | ecommerce |

---

# 🛢️ Docker MySQL Configuration

When using Docker Compose:

| Property | Value     |
| -------- | --------- |
| Host     | localhost |
| Port     | 3307      |
| Username | root      |
| Password | root123   |
| Database | ecommerce |

---

# 📡 REST API Endpoints

| Method | Endpoint                  | Description         |
| ------ | ------------------------- | ------------------- |
| GET    | `/api/products`           | Fetch all products  |
| GET    | `/api/product/{id}`       | Fetch product by ID |
| POST   | `/api/product`            | Add product         |
| PUT    | `/api/product/{id}`       | Update product      |
| DELETE | `/api/product/{id}`       | Delete product      |
| GET    | `/api/product/{id}/image` | Fetch product image |
| GET    | `/api/products/search`    | Search products     |

---

# 🖼️ Product Image Handling

The application supports:

* Product image upload
* Image storage inside MySQL using `LONGBLOB`
* Optimized REST API response using `@JsonIgnore`

Image binary data is excluded from product JSON responses for better API performance.

---

# 💻 Frontend - React + Vite

## 🔧 Technologies Used

* ReactJS
* Vite
* Axios
* Bootstrap
* JavaScript (ES6+)

---

# 📂 Frontend Directory Structure

Ecommerce-Frontend/
│
├── public/
├── src/
│   ├── components/
│   ├── pages/
│   ├── App.jsx
│   └── main.jsx
├── nginx.conf
├── Dockerfile
├── package.json
└── vite.config.js

---

# ▶️ Frontend Setup Instructions

## Install Dependencies

```bash
cd Ecommerce-Frontend

npm install
```

---

## Run Frontend Application

```bash
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

# 🔗 Frontend API Integration

Frontend communicates with backend using Axios.

Example:

```javascript
axios.get("http://localhost:8081/api/products")
```

---

# 🧩 Application Features

* Product listing
* Add product
* Update product
* Delete product
* Product search
* Product image upload
* Shopping cart
* Checkout flow
* Responsive UI
* React routing
* REST API integration
* Persistent MySQL storage

---

# 🐳 Dockerization

The entire application is containerized using Docker.

---

# 🐳 Backend Dockerization

## Backend Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]
```

---

# ▶️ Build Backend Docker Image

```bash
cd Ecommerce-Backend

docker build -t preran1966/springboot-ecommerce-backend:latest .
```

---

# ▶️ Run Backend Container

```bash
docker run -d -p 8081:8081 --name backend-container preran1966/springboot-ecommerce-backend:latest
```

---

# 🌐 Frontend Dockerization

## Frontend Dockerfile

```dockerfile
# Build Stage
FROM node:20 AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# Production Stage
FROM nginx:latest

COPY --from=build /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
```

---

# ▶️ Build Frontend Docker Image

```bash
cd Ecommerce-Frontend

docker build -t preran1966/react-ecommerce-frontend:latest .
```

---

# ▶️ Run Frontend Container

```bash
docker run -d -p 3000:80 --name frontend-container preran1966/react-ecommerce-frontend:latest
```

---

# 🌐 Nginx SPA Routing Configuration

React is a Single Page Application (SPA).

Routes such as:

```text
/add_product
/product/1
/cart
```

require Nginx routing configuration.

---

# nginx.conf

```nginx
server {

    listen 80;

    location / {

        root /usr/share/nginx/html;

        index index.html;

        try_files $uri /index.html;
    }
}
```

---

# Why try_files is Required

If requested frontend route is not found, Nginx serves:

```text
index.html
```

allowing React Router to handle routing.

---

# 🐳 Docker Compose Setup

Docker Compose is used for multi-container deployment.

---

# docker-compose.yml

```yaml
services:

  mysql:
    image: mysql:8
    container_name: mysql-container

    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: ecommerce

    ports:
      - "3307:3306"

    volumes:
      - mysql-data:/var/lib/mysql

  backend:
    image: preran1966/springboot-ecommerce-backend:latest
    container_name: backend-container

    ports:
      - "8081:8081"

    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/ecommerce
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root123

    depends_on:
      - mysql

  frontend:
    image: preran1966/react-ecommerce-frontend:latest
    container_name: frontend-container

    ports:
      - "3000:80"

    depends_on:
      - backend

volumes:
  mysql-data:
```

---

# ▶️ Run Entire Application

```bash
docker compose up -d
```

---

# ▶️ Stop Containers

```bash
docker compose down
```

---

# 🌐 Application Architecture

Browser
↓
Frontend Container (React + Nginx)
↓
Backend Container (Spring Boot)
↓
MySQL Container

---

# 🔄 Jenkins CI/CD Pipeline

A complete Jenkins Declarative Pipeline is implemented for CI/CD automation.

---

# ⚙️ Jenkins Pipeline Features

* Clone source code from GitHub
* Build Spring Boot backend
* Build React frontend
* Build Docker images
* Push Docker images to Docker Hub
* Docker image versioning using Jenkins build number
* latest image maintenance
* Automated deployment using Docker Compose

---

# 🚀 Jenkins CI/CD Flow

GitHub Repository
↓
Jenkins Pipeline
↓
Backend Build
↓
Frontend Build
↓
Docker Image Build
↓
Docker Hub Push
↓
Docker Compose Deployment

---

# 📦 Docker Hub Repositories

## Backend Repository

```text
preran1966/springboot-ecommerce-backend
```

---

## Frontend Repository

```text
preran1966/react-ecommerce-frontend
```

---

# ☸️ Kubernetes Deployment (In Progress)

Kubernetes deployment implementation has been started using Minikube.

Implemented:

* Backend Deployment
* Backend Service

Planned:

* Frontend Deployment
* MySQL Deployment
* ConfigMaps
* Secrets
* Persistent Volumes
* Ingress Controller

---

# 🛠️ DevOps Tools Used

| Tool           | Purpose                     |
| -------------- | --------------------------- |
| Git            | Version Control             |
| GitHub         | Source Code Hosting         |
| Docker         | Containerization            |
| Docker Compose | Multi-container Deployment  |
| Jenkins        | CI/CD Automation            |
| Maven          | Backend Build Tool          |
| npm            | Frontend Package Management |
| Nginx          | Frontend Web Server         |
| MySQL          | Persistent Database         |
| Kubernetes     | Container Orchestration     |
| Minikube       | Local Kubernetes Cluster    |

---

# 🚀 Future Enhancements

* Kubernetes Full Stack Deployment
* Helm Charts
* SonarQube Integration
* Nexus Artifact Repository
* Prometheus Monitoring
* Grafana Dashboards
* ArgoCD GitOps Deployment
* AWS EKS Deployment
* Terraform Infrastructure Automation

---

# 👨‍💻 Author

Prerankumar

DevOps Engineer | Cloud & Automation Enthusiast

```
```
