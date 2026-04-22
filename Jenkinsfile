pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                withCredentials([
                        file(credentialsId: 'SECRET_YML', variable: 'SECRET_YML')
                ]) {
                    sh "cp ${SECRET_YML} ./src/main/resources/application-secret.yml"
                }
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean test'
            }
        }

        stage('Build') {
            when {
                branch 'develop'
            }

            steps {
                sh './gradlew bootJar'
            }
        }

        stage('Deploy') {
            when {
                branch 'develop'
            }

            steps {
                withCredentials([
                        string(credentialsId: 'DBPW', variable: 'DB_PASSWORD'),
                ]) {
                    sh """
                        SPRING_PROFILE=${env.APP_PROD_PROFILE} \
                        APP_PORT=${env.APP_PROD_PORT} \
                        DB_PORT=${env.DB_PROD_PORT} \
                        REDIS_PORT=${env.REDIS_PROD_PORT} \
                        DB_NAME=${env.DB_NAME} \
                        IMAGE_PATH=${env.IMAGE_PATH_FROM_SPRING}\
                        DB_PASSWORD='${DB_PASSWORD}' \
                        RABBITMQ_PORT=${env.RABBITMQ_PROD_PORT} \
                        RABBITMQ_MANAGEMENT_PORT=${env.RABBITMQ_MANAGEMENT_PROD_PORT} \
                        RABBITMQ_USERNAME=${env.RABBITMQ_PROD_USERNAME} \
                        RABBITMQ_PASSWORD=${env.RABBITMQ_PROD_PASSWORD} \
                        docker compose up -d --build
                    """
                }
            }
        }
    }
}