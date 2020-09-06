node {
    ansiColor('xterm') {
        try {
            stage('Checkout') {
                checkout scm
            }
            stage('Build') {
                sh 'cp /home/destroflyer/destrostudios/application-prod.properties src/main/resources/application-prod.properties'
                sh 'mvn clean install'
            }
        } finally {
            cleanWs()
        }
    }
}