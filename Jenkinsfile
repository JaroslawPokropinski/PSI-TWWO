pipeline {
    environment {
        dockerImage = ''
    }
    agent any
    stages {
        stage('Prepare tests') {
            steps {
                git branch: 'jenkins2', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
            }
        }
        stage('Test backend') {
            tools {
                jdk 'openjdk-11'
            }
            steps {
                sh 'chmod u+x gradlew'
                sh './gradlew test'
            }
        }
        stage('Test frontend') {
            steps {
                dir('PSI-TWWO/frontend') {
                // sh 'npm run test'
                }
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
    }
}

// pipeline {
//     environment {
//         dockerImage = ''
//     }
//     agent {
//         docker {
//             image 'node:14-alpine'
//             args '-u root:root'
//         }
//     }
//     stages {
//         stage('Prepare') {
//             steps {
//                 sh 'apk add --no-cache git'
//                 sh 'apk --no-cache add openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community'
//                 git branch: 'main', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
//             }
//         }
//         stage('Test backend') {
//             steps {
//                 dir('PSI-TWWO') {
//                     sh 'chmod 777 gradlew'
//                     sh './gradlew test'
//                 }
//             }
//         }
//         stage('Test frontend') {
//             steps {
//                 dir('PSI-TWWO/frontend') {
//                 // sh 'npm run test'
//                 }
//             }
//         }
//         stage('Build image') {
//             steps {
//                 script {
//                     dockerImage = docker.build 'jaroslawpokropinski/twwo' + ":$BUILD_NUMBER"
//                 }
//             }
//         }
//         stage('Deploy to dockerhub') {
//             steps {
//                 script {
//                     docker.withRegistry('https://registry.hub.docker.com', 'docker') {
//                         app.push("${env.BUILD_NUMBER}")
//                         app.push('latest')
//                     }
//                 }
//             }
//         }
//     }
// }
