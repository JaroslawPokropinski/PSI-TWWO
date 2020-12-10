pipeline {
  agent any
  stages {
    stage('Clone repository') {
      steps {
        git branch: 'jenkins', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
      }
    }
    stage('Build') {
      agent {
        docker { image 'openjdk:11-jdk' }
      }
      steps {
        sh 'chmod u+x gradlew'
        sh './gradlew build'
      }
    }
  }
}