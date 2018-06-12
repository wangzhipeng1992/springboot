// Declarative //
pipeline {
    agent any
    
    tools {
        maven 'maven-3.5.3'
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                   echo "开始编译打包过程"
                   echo "PATH = ${PATH}"
                   echo "M2_HOME = ${M2_HOME}"
                   mvn clean && mvn package -DskipTests=true
                '''
            }
        }
        stage('Test'){
            steps {
                sh 'echo "Test stage"'
                junit 'reports/**/*.xml'
            }
        }
        stage('Deploy') {
            steps {
                sh '''
                    echo "开始远程部署应用包"
                    jarfile=$(ls target/*.jar)
                    scp "${jarfile}" root@${remoteIp}:/opt/adm/${appName}/
                    ssh root@${remoteIp} "cd /opt/adm/${appName}/; ./run.sh start test"
                '''
            }
        }
        
        stage('Check') {
            steps {
                sh '''
                    echo "查看日志是否启动完成"
                    log_file="/opt/adm/${appName}/temp.log"
                    c=0
                    while [[ "$c" < 15 ]]; do
                        ssh root@${remoteIp} "grep 'Started Application in' ${log_file} >/dev/null"
                        if [[ "$?" != "0" ]]; then
                            echo "开始睡觉2秒钟..."
                            sleep 2
                            (( c++ ))
                        else
                            echo "启动完成！"
                            exit 0
                        fi
                    done
                    echo "超过30秒，启动失败了！"
                    exit 1
                '''
            }
        }
    }
}
