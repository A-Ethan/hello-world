pipeline {
    agent {
        node{
            label 'SLAVE'
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
            steps {
                     script {
                         build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                     }
                     sh "export"
                     sh "sudo docker build -t uhub.ucloud.cn/gary/helloword:${build_tag} ."
                     sh "sudo docker push  uhub.ucloud.cn/gary/helloword:${build_tag} "
            }
        }
    }
}