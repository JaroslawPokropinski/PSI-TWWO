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
        jdk "jdk-11"
      }
      steps {
        sh 'chmod u+x gradlew'
        sh './gradlew build'
      }
    }
  }
}