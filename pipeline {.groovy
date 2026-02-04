pipeline {
    agent any
    stages {
        stage('Pull Code From GitHub') {
            steps {
                git 'https://github.com/MeganathanU/Mazeball-project.git'
            }
        }
        stage('Build the Docker image') {
            steps {
                sh 'docker build -t mazeimage .'
                sh 'docker tag mazeimage meganathanu/mazeimage:latest'
                sh 'docker tag mazeimage meganathanu/mazeimage:${BUILD_NUMBER}'
            }
        }
        stage('Trivy Vulnerability Scan') {
            steps {
                script {
                    sh 'trivy image meganathanu/mazeimage:latest'
                    sh 'trivy image meganathanu/mazeimage:${BUILD_NUMBER}'
                }
            }
        }
        stage('Push the Docker image') {
            steps {
                sh 'docker image push meganathanu/mazeimage:latest'
                sh 'docker image push meganathanu/mazeimage:${BUILD_NUMBER}'
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
