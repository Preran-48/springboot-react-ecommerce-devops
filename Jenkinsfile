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

            when { 
                changeset "Ecommerce-Backend/**" 
            }
            steps {
                dir('Ecommerce-Backend') {
                    bat 'mvn clean package'
                }
            }
        }

        stage('Build Frontend') {

            when { 
                changeset "Ecommerce-Frontend/**" 
            }
            steps {
                dir('Ecommerce-Frontend') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('Build Backend Docker Image') {

            when { 
                changeset "Ecommerce-Backend/**" 
            }
            steps {
                dir('Ecommerce-Backend') {

                    bat "docker build -t %BACKEND_IMAGE%:%IMAGE_TAG% ."

                    bat "docker tag %BACKEND_IMAGE%:%IMAGE_TAG% %BACKEND_IMAGE%:latest"
                }
            }
        }

        stage('Build Frontend Docker Image') {

            when { 
                changeset "Ecommerce-Frontend/**" 
            }
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
            when { 
                changeset "Ecommerce-Backend/**" 
            }
            steps {

                bat "docker push %BACKEND_IMAGE%:%IMAGE_TAG%"

                bat "docker push %BACKEND_IMAGE%:latest"
            }
        }

        stage('Push Frontend Docker Images') {
            when { 
                changeset "Ecommerce-Frontend/**" 
            }
            steps {

                bat "docker push %FRONTEND_IMAGE%:%IMAGE_TAG%"

                bat "docker push %FRONTEND_IMAGE%:latest"
            }
        }

        stage('Deploy Application') {

           when { 
              anyOf { 
                changeset "Ecommerce-Backend/**" 
                changeset "Ecommerce-Frontend/**" 
                changeset "docker-compose.yml" 
              } 
           }
           steps {

               bat 'docker compose down'

               bat 'docker compose pull'

               bat 'docker compose up -d'
            }
        }
    }
}