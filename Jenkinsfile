pipeline {
    agent any
    options {
            timeout(time: 1, unit: 'MINUTES')
       }
    tools {
               gradle "gradle"
               jdk "jdk"
       }
    stages {
       stage('prepare') {
                  steps {
                      script {
                          if (env.BRANCH_NAME == 'master') {
                             sh 'echo "开始构建当前master分支!!!"'
                          } else {
                              sh 'echo "当前分支暂未支持流水线作业!!!" && exit 1'
                          }
                      }
                  }
              }

        stage('checkout code') {
            steps {
             sh 'echo "开始拉取代码!!!"'
                git branch: "${BRANCH_NAME}",
                    credentialsId: "jiabin-github",
                    url: "https://github.com/messliyan/auto.git"
                sh "ls -lat"
            }
        }

        stage('build') {
                   steps {
                       sh 'gradle -Dorg.gradle.daemon=false clean'
                       sh '''
                           echo " ->（1）构建打包 (Fat Jar)"
                           gradle -Dorg.gradle.daemon=false build -x compileTestJava
                       '''
                   }
               }
    }

    post {
        success {
            sh "echo suc!"
        }

        failure {
            sh "echo fail!"
        }
    }
}


