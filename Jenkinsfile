pipeline {
  agent any
  stages {
    stage('Clone repository') {
      steps {
        git branch: 'jenkins', url: 'https://github.com/JaroslawPokropinski/PSI-TWWO.git'
      }
    }
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
  }
}