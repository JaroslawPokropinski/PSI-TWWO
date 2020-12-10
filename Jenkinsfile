pipeline {
  agent any
  stages {
    stage('Clone repository') {
      steps {
        git branch: 'jenkins', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
      }
    }
    stage('Build') {
      tools {
        jdk "openjdk-11"
      }
      steps {
        sh 'java -version'
        sh 'chmod u+x gradlew'
        sh './gradlew build -x test '
      }
    }
  }
}