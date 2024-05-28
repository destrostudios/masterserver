pipeline {
    agent any
    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '10'))
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
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
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