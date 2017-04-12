pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew clean assemble'
      }
    }
    stage('Finish') {
      steps {
        archiveArtifacts '**/outputs/aar/*.aar'
      }
    }
  }
}