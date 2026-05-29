# 🛍️ Full Stack E-commerce Web Application

A full-stack **E-commerce application** using **Spring Boot** (Java) for the backend and **ReactJS with Vite** for the frontend. This project demonstrates complete end-to-end DevOps implementation including containerization, Docker Compose orchestration, Nginx configuration, and Jenkins CI/CD automation.

---

# 📁 Project Structure

```text
SpringBoot-Reactjs-Ecommerce-main/
│
├── Ecommerce-Backend/        # Spring Boot REST API backend
├── Ecommerce-Frontend/       # React + Vite frontend application
├── docker-compose.yml        # Multi-container deployment
├── Jenkinsfile               # Jenkins CI/CD pipeline
└── README.md
```

---

# 🧩 Backend - Spring Boot

## 🔧 Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* H2 Database
* Maven

---

## 📂 Backend Directory Structure

```text
Ecommerce-Backend/
│
├── controller/               # REST API controllers
├── model/                    # JPA entity classes
├── repo/                     # Repository interfaces
├── service/                  # Business logic
├── resources/
│   ├── application.properties
│   └── data.sql
├── Dockerfile
└── pom.xml
```

---

## ⚙️ Backend Setup Instructions

### Run Backend Application

```bash
cd Ecommerce-Backend

mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8081
```

---

## 🗄️ H2 Database Configuration

### application.properties

```properties
spring.application.name=ecom-proj

server.port=8081

spring.datasource.url=jdbc:h2:mem:Ecommerce
spring.datasource.password=project1
spring.datasource.driverClassName=org.h2.Driver

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

spring.jpa.defer-datasource-initialization=true
```

---

## 🛢️ H2 Console Access

```text
http://localhost:8081/h2-console
```

### Database Credentials

| Property | Value                 |
| -------- | --------------------- |
| JDBC URL | jdbc:h2:mem:Ecommerce |
| Username | sa                    |
| Password | project1              |

---

## 📡 REST API Endpoints

| Method | Endpoint               | Description         |
| ------ | ---------------------- | ------------------- |
| GET    | `/api/products`        | Fetch all products  |
| GET    | `/api/product/{id}`    | Fetch product by ID |
| POST   | `/api/product`         | Add product         |
| PUT    | `/api/product/{id}`    | Update product      |
| DELETE | `/api/product/{id}`    | Delete product      |
| GET    | `/api/products/search` | Search products     |

---

# 💻 Frontend - React + Vite

## 🔧 Technologies Used

* ReactJS
* Vite
* Axios
* Bootstrap
* JavaScript (ES6+)

---

## 📂 Frontend Directory Structure

```text
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
```

---

## ▶️ Frontend Setup Instructions

### Install Dependencies

```bash
cd Ecommerce-Frontend

npm install
```

### Run Frontend Application

```bash
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

## 🔗 Frontend API Integration

Frontend connects to backend using Axios.

Example:

```javascript
axios.get("http://localhost:8081/api/products")
```

---

## 🧩 Application Features

* Product listing
* Add product
* Update product
* Delete product
* Product search
* Product image upload
* Responsive UI
* React routing
* API integration using Axios

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

## Build Backend Docker Image

```bash
cd Ecommerce-Backend

docker build -t preran1966/springboot-ecommerce-backend:v1 .
```

---

## Run Backend Container

```bash
docker run -d -p 8081:8081 --name backend-container preran1966/springboot-ecommerce-backend:v1
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

## Build Frontend Docker Image

```bash
cd Ecommerce-Frontend

docker build -t preran1966/react-ecommerce-frontend:v1 .
```

---

## Run Frontend Container

```bash
docker run -d -p 3000:80 --name frontend-container preran1966/react-ecommerce-frontend:v1
```

---

# 🌐 Nginx SPA Routing Configuration

React is a Single Page Application (SPA).

Direct routes such as:

```text
/add_product
/product/1
/cart
```

require special Nginx configuration.

---

## nginx.conf

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

## Why try_files is Required

If the requested route/file is not found, Nginx serves:

```text
index.html
```

so React Router handles frontend routing correctly.

---

# 🐳 Docker Compose Setup

Docker Compose is used for multi-container deployment.

---

## docker-compose.yml

```yaml
version: '3.8'

services:

  backend:
    image: preran1966/springboot-ecommerce-backend:latest
    container_name: backend-container

    ports:
      - "8081:8081"

  frontend:
    image: preran1966/react-ecommerce-frontend:latest
    container_name: frontend-container

    ports:
      - "3000:80"

    depends_on:
      - backend
```

---

## Run Entire Application

```bash
docker compose up -d
```

---

## Stop Containers

```bash
docker compose down
```

---

# 🔄 Jenkins CI/CD Pipeline

A complete Jenkins Declarative Pipeline is implemented for Continuous Integration and Docker automation.

---

# ⚙️ Jenkins Pipeline Features

* Clone source code from GitHub
* Build Spring Boot application
* Build React frontend
* Build Docker images
* Push Docker images to Docker Hub
* Docker image versioning using Jenkins build number
* latest image maintenance

---

## Jenkinsfile

```groovy
pipeline {

    agent any

    environment {
        DOCKERHUB_CREDS = credentials('dockerhub-creds')

        BACKEND_IMAGE = "preran1966/springboot-ecommerce-backend"
        FRONTEND_IMAGE = "preran1966/react-ecommerce-frontend"

        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Clone Repository') {
            steps {
                git 'https://github.com/Preran-48/springboot-react-ecommerce-devops.git'
            }
        }

        stage('Build Backend') {
            steps {
                dir('Ecommerce-Backend') {
                    bat 'mvn clean package'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('Ecommerce-Frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('Ecommerce-Backend') {

                    bat "docker build -t %BACKEND_IMAGE%:%IMAGE_TAG% ."

                    bat "docker tag %BACKEND_IMAGE%:%IMAGE_TAG% %BACKEND_IMAGE%:latest"
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('Ecommerce-Frontend') {

                    bat "docker build -t %FRONTEND_IMAGE%:%IMAGE_TAG% ."

                    bat "docker tag %FRONTEND_IMAGE%:%IMAGE_TAG% %FRONTEND_IMAGE%:latest"
                }
            }
        }

        stage('Docker Login') {
            steps {
                bat 'docker login -u %DOCKERHUB_CREDS_USR% -p %DOCKERHUB_CREDS_PSW%'
            }
        }

        stage('Push Backend Docker Images') {
            steps {

                bat "docker push %BACKEND_IMAGE%:%IMAGE_TAG%"

                bat "docker push %BACKEND_IMAGE%:latest"
            }
        }

        stage('Push Frontend Docker Images') {
            steps {

                bat "docker push %FRONTEND_IMAGE%:%IMAGE_TAG%"

                bat "docker push %FRONTEND_IMAGE%:latest"
            }
        }
    }
}
```

---

# 🚀 Jenkins CI/CD Flow

```text
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
```

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
| H2 Database    | In-memory Database          |

---

# 🚀 Future Enhancements

* Kubernetes Deployment
* Helm Charts
* SonarQube Integration
* Nexus Artifact Repository
* Prometheus Monitoring
* Grafana Dashboards
* ArgoCD GitOps Deployment
* AWS EKS Deployment

---

# 👨‍💻 Author

Prerankumar

DevOps Engineer | Cloud & Automation Enthusiast
