pipeline {
    environment {
        dockerImage = ''
    }
    agent any
    stages {
        stage('Prepare tests') {
            steps {
                git branch: 'main', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
            }
        }
        stage('Test backend') {
            steps {
                sh 'chmod u+x gradlew'
                sh './gradlew test'
            }
        }
        stage('Build image') {
            steps {
                script {
                    dockerImage = docker.build 'jaroslawpokropinski/twwo' + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Deploy to dockerhub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker') {
                        dockerImage.push("${env.BUILD_NUMBER}")
                        dockerImage.push('latest')
                    }
                }
            }
        }

        stage('Deploy to kubernetes') {
            steps {
                script {
                    withKubeConfig([credentialsId: 'kubeconfig']) {
                        sh 'kubectl rollout restart deployment.apps/twwo'
                    }
                }
            }
        }
    }
}
