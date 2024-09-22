pipeline {
    agent { label "master" }
    environment {
        PATH = "/usr/bin:${PATH}"
    }
    stages {
        stage("Build") {
            steps {
                sh "docker-compose -f docker-compose.yml up -d --build"
            }
        }
    }
    post {
        always {
             sh """
                   curl -s -X POST "https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage" \
                   -d "chat_id=${CHAT_ID}" \
                   -d "text=[image-service] ${env.JOB_NAME} – Build number ${env.BUILD_NUMBER} – ${currentBuild.currentResult}!"
               """
        }
    }
}