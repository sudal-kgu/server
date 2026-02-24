pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test & Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean bootJar'
            }
        }

        stage('Deploy') {
            when {
                branch 'develop'
            }

            steps {
                withCredentials([
                        string(credentialsId: 'DBPW', variable: 'DB_PASSWORD'),
                        file(credentialsId: 'SECRET_YML', variable: 'SECRET_YML')
                ]) {
                    sh "cp ${SECRET_YML} ./src/main/resources/application-secret.yml"
                    sh """
                        SPRING_PROFILE=${env.APP_PROD_PROFILE} \
                        APP_PORT=${env.APP_PROD_PORT} \
                        DB_PORT=${env.DB_PROD_PORT} \
                        REDIS_PORT=${env.REDIS_PROD_PORT} \
                        DB_NAME=${env.DB_NAME} \
                        IMAGE_PATH=${env.IMAGE_PATH_FROM_SPRING}\
                        DB_PASSWORD='${DB_PASSWORD}' \
                        docker compose up -d --build
                    """
                }
            }
        }
    }
}