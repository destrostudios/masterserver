node {
    ansiColor('xterm') {
        try {
            stage('Checkout') {
                checkout scm
            }
            stage('Build') {
                sh 'mvn clean install'
            }
        } finally {
            cleanWs()
        }
    }
}