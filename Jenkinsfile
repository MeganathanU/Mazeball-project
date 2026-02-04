pipeline {
    agent any
    stages {
        stage('Pull Code From GitHub') {
            steps {
                git 'https://github.com/vijay3639/Mazeball.git'
            }
        }
        stage('Build the Docker image') {
            steps {
                sh 'docker build -t mazeimage .'
                sh 'docker tag mazeimage vijay3639/mazeimage:latest'
                sh 'docker tag mazeimage vijay3639/mazeimage:${BUILD_NUMBER}'
            }
        }
        stage('Trivy Vulnerability Scan') {
            steps {
                script {
                    sh 'trivy image vijay3639/mazeimage:latest'
                    sh 'trivy image vijay3639/mazeimage:${BUILD_NUMBER}'
                }
            }
        }
        stage('Push the Docker image') {
            steps {
                sh 'docker image push vijay3639/mazeimage:latest'
                sh 'docker image push vijay3639/mazeimage:${BUILD_NUMBER}'
            }
        }
        stage('Deploy on Kubernetes') {
            steps {
                sh 'kubectl apply -f pod.yml'
                sh 'kubectl rollout restart deployment loadbalancer-pod'
            }
        }
    }
}
