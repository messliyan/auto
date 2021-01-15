pipeline {
    agent any
    options {
            timeout(time: 15, unit: 'MINUTES')
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
                    sh 'gradle -v'
                       sh 'gradle -Dorg.gradle.daemon=false clean'
                       sh '''
                           if gradle tasks --all | grep "publishToMavenLocal"
                           then
                              echo '->（1）发布到本地maven仓库'
                              gradle -Dorg.gradle.daemon=false publishToMavenLocal
                           else
                              echo 'gradle没有找到 publishToMavenLocal 任务'
                           fi
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


