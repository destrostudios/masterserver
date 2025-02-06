pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        skipDefaultCheckout()
        ansiColor('xterm')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            agent {
                docker {
                    image 'maven:3.9.9-eclipse-temurin-22-alpine'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean install'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}