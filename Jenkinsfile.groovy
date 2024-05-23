pipeline {
    agent any

    tools{
        nodejs 'node'
    }

    stages {

        stage('Preparation') {
            steps {
                script {
                    sh 'rm -f results/TEST-postman-results.xml'
                }
            }
        }

        stage('Run Postman Tests') {
            steps { 
                script{
                    sh 'newman run tests/API-coffee.json --reporters cli,junit,htmlextra --reporter-junit-export results/TEST-postman-results.xml --reporter-htmlextra-export results/TEST-postman-results.html --suppress-exit-code'
                }
            }
        }

        stage('Publish HTML Report') {
            steps {
                publishHTML(target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'results',
                        reportFiles: 'TEST-postman-results.html',
                        reportName: 'Postman Report'
                ])
            }
        }

    }

    post {
        always {
            archiveArtifacts 'results/*.xml'
        }
    }

}


