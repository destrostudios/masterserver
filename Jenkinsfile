node {
    ansiColor('xterm') {
        try {
            stage('Checkout') {
                checkout scm
            }
            stage('Build') {
                sh 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64;mvn clean install'
            }
        } finally {
            cleanWs()
        }
    }
}