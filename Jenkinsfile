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
            description: 'Ejecutar construcción y despliegue con Docker'
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

        stage('Docker Build and Start') {
            when {
                expression {
                    return params.RUN_DOCKER && fileExists('docker-compose.yml')
                }
            }
            steps {
                bat '''
                docker --version
                docker compose version
                docker compose down --remove-orphans
                docker compose up -d --build
                docker compose ps
                '''
            }
        }

        stage('Wait for Backend') {
            when {
                expression {
                    return params.RUN_DOCKER && params.RUN_FUNCTIONAL_TESTS
                }
            }
            steps {
                bat '''
                echo Esperando backend en http://localhost:8080 ...

                for /L %%i in (1,1,30) do (
                    curl -I http://localhost:8080/ && exit /b 0
                    echo Backend aun no responde. Intento %%i de 30...
                    timeout /t 3 /nobreak > nul
                )

                echo Backend no responde en http://localhost:8080
                docker compose ps
                docker compose logs --tail=80 backend
                exit /b 1
                '''
            }
        }

        stage('Wait for Frontend') {
            when {
                expression {
                    return params.RUN_DOCKER && params.RUN_FUNCTIONAL_TESTS
                }
            }
            steps {
                bat '''
                echo Esperando frontend en http://localhost:3000 ...

                for /L %%i in (1,1,30) do (
                    curl -I http://localhost:3000/ && exit /b 0
                    echo Frontend aun no responde. Intento %%i de 30...
                    timeout /t 3 /nobreak > nul
                )

                echo Frontend no responde en http://localhost:3000
                docker compose ps
                docker compose logs --tail=80 frontend
                exit /b 1
                '''
            }
        }

        stage('Functional Tests - Selenium') {
            when {
                expression {
                    return params.RUN_DOCKER && params.RUN_FUNCTIONAL_TESTS && fileExists('functional-tests/pom.xml')
                }
            }
            steps {
                dir('functional-tests') {
                    bat 'mvn clean test'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'functional-tests/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Performance Tests - JMeter') {
            when {
                expression {
                    return params.RUN_DOCKER && params.RUN_JMETER && fileExists('jmeter/taskmanager-jmeter-final.jmx')
                }
            }
            steps {
                bat '''
                if not exist docs\\performance mkdir docs\\performance
                if exist docs\\performance\\html-report rmdir /s /q docs\\performance\\html-report

                "C:\\tools\\apache-jmeter-5.6.3\\bin\\jmeter.bat" -n -t jmeter\\taskmanager-jmeter-final.jmx -l docs\\performance\\results.jtl -e -o docs\\performance\\html-report
                '''
            }
        }
    }

    post {
        always {
            script {
                if (params.RUN_DOCKER && fileExists('docker-compose.yml')) {
                    bat '''
                    docker compose down --remove-orphans
                    '''
                }
            }
        }

        success {
            echo 'Pipeline ejecutado correctamente.'
        }

        failure {
            echo 'Pipeline falló. Revisar Console Output.'
        }
    }
}