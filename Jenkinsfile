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
        stage('Build app') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17-alpine'
                }
            }
            steps {
                sh 'mvn clean install'
                stash includes: 'target/masterserver-1.0.0.jar', name: 'target'
            }
        }
        stage('Build') {
            steps {
                unstash 'target'
                sh 'mv target docker'
                dir ('docker') {
                    withCredentials([
                        string(credentialsId: 'keystore-password', variable: 'KEYSTORE_PASSWORD'),
                        string(credentialsId: 'auth-private-key-passphrase', variable: 'AUTH_PRIVATE_KEY_PASSPHRASE'),
                        file(credentialsId: 'auth-private-key', variable: 'AUTH_PRIVATE_KEY'),
                    ]) {
                        sh 'openssl rsa -in $AUTH_PRIVATE_KEY -passin pass:$AUTH_PRIVATE_KEY_PASSPHRASE -outform DER -out private.der'
                        sh 'openssl rsa -in $AUTH_PRIVATE_KEY -passin pass:$AUTH_PRIVATE_KEY_PASSPHRASE -pubout -outform PEM -out public.pem'
                        sh 'openssl pkcs12 -export -in /etc/letsencrypt/live/anselm-kuesters.de/cert.pem -inkey /etc/letsencrypt/live/anselm-kuesters.de/privkey.pem -certfile /etc/letsencrypt/live/anselm-kuesters.de/chain.pem -out keystore.p12 -passout pass:$KEYSTORE_PASSWORD -name destrostudios'
                        sh 'docker compose build --no-cache'
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}