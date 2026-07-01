pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Descargando código fuente desde GitHub'
                checkout scm
            }
        }

        stage('Backend - Build and Unit Tests') {
            steps {
                echo 'Compilando backend y ejecutando pruebas unitarias'
                dir('backend-springboot') {
                    bat 'mvn clean test'
                    bat 'mvn clean package'
                }
            }
        }

        stage('Frontend - Install and Build') {
            steps {
                echo 'Instalando dependencias y compilando frontend'
                dir('frontend-reactjs') {
                    bat 'npm install'
                    bat 'npm run build'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Ejecutando análisis estático con SonarQube'
                bat 'sonar-scanner'
            }
        }

        stage('Functional Tests - Selenium') {
            steps {
                echo 'Ejecutando pruebas funcionales con Selenium'
                dir('functional-tests') {
                    bat 'mvn test'
                }
            }
        }

        stage('Performance Tests - JMeter') {
            steps {
                script {
                    if (fileExists('performance-tests/task-manager-performance.jmx')) {
                        echo 'Ejecutando pruebas de rendimiento con JMeter'
                        bat 'jmeter -n -t performance-tests\\task-manager-performance.jmx -l docs\\performance\\jmeter-results.jtl'
                    } else {
                        echo 'Archivo JMeter pendiente: performance-tests/task-manager-performance.jmx'
                    }
                }
            }
        }

        stage('Security Tests - OWASP ZAP') {
            steps {
                script {
                    if (fileExists('security-tests/zap-report.html')) {
                        echo 'Reporte OWASP ZAP encontrado'
                    } else {
                        echo 'Reporte OWASP ZAP pendiente de integrar al repositorio'
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    if (fileExists('docker-compose.yml')) {
                        echo 'Construyendo contenedores Docker'
                        bat 'docker compose build'
                    } else {
                        echo 'docker-compose.yml pendiente de implementar'
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado correctamente.'
        }

        failure {
            echo 'El pipeline falló. Revisar los logs de Jenkins.'
        }

        always {
            echo 'Finalizó la ejecución del pipeline CI/CD.'
        }
    }
}