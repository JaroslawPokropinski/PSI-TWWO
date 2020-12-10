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
        sh 'sudo apt-get install default-jdk'
        sh 'java -version'
        sh 'chmod u+x gradlew'
        sh './gradlew build'
      }
    }
  }
}