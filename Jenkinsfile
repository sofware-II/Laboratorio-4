pipeline {
    agent any

    parameters {
        booleanParam(
            name: 'RUN_FUNCTIONAL_TESTS',
            defaultValue: false,
            description: 'Ejecutar pruebas funcionales Selenium'
        )

        booleanParam(
            name: 'RUN_JMETER',
            defaultValue: false,
            description: 'Ejecutar pruebas de rendimiento con JMeter'
        )

        booleanParam(
            name: 'RUN_DOCKER',
            defaultValue: false,
            description: 'Ejecutar construcción con Docker'
        )
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Tools') {
            steps {
                bat '''
                java -version
                mvn -version
                node -v
                npm -v
                git --version
                sonar-scanner -v
                '''
            }
        }

        stage('Backend - Unit Tests') {
            steps {
                dir('backend-springboot') {
                    bat 'mvn clean test'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend-springboot/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Frontend - Build') {
            steps {
                dir('frontend-reactjs') {
                    bat 'npm install'
                    bat 'set CI=false&& npm run build'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                bat 'sonar-scanner -Dsonar.token=%SONAR_TOKEN%'
            }
        }

        stage('Functional Tests - Selenium') {
            when {
                expression {
                    return params.RUN_FUNCTIONAL_TESTS && fileExists('functional-tests/pom.xml')
                }
            }
            steps {
                dir('functional-tests') {
                    bat 'mvn test'
                }
            }
        }

        stage('Performance Tests - JMeter') {
            when {
                expression {
                    return params.RUN_JMETER && fileExists('performance-tests/task-manager-performance.jmx')
                }
            }
            steps {
                bat '''
                jmeter -n -t performance-tests/task-manager-performance.jmx -l docs/performance/results.jtl
                '''
            }
        }

        stage('Docker Build') {
            when {
                expression {
                    return params.RUN_DOCKER && fileExists('docker-compose.yml')
                }
            }
            steps {
                bat '''
                docker --version
                docker compose version
                docker compose build
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado correctamente.'
        }

        failure {
            echo 'Pipeline falló. Revisar Console Output.'
        }
    }
}