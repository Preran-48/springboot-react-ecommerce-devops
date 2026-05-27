pipeline {

    agent any

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
    }
}