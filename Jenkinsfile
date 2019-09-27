pipeline {
    agent {
        node{
            label 'uhost'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }

        }

        stage('BuildImage') {
            build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
            sh "sudo docker build -t uhub.ucloud.cn/gary/helloword:${build_tag} ."
        }
    }
}