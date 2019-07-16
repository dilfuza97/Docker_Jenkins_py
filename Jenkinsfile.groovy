node('master') {
  withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: '', credentialsId: 'aws', secretKeyVariable: '']]) {
    // some block

  stage('Clone repo') {
    git credentialsId: 'github', url: 'https://github.com/dilfuza97/Docker_Jenkins_py.git'
  }

   stage('Check awscli') {
      try {
        // Trying to run terraform command
        env.terraform  = sh returnStdout: true, script: 'aws --version'
        echo """
        echo AWS CLI is already installed version ${env.terraform}
        """
        } catch(er) {
              // if terraform does not installed in system stage will install the terraform
               stage('Installing AWS CLI') {
                 sh """
                 yum install awscli -y
                 """
               }
            }
          }
   stage('Docker build') {
           dir("${WORKSPACE}") {
             sh "docker build -t http-server ."
           }
      }

     
    stage('Docker push') {

           dir("${WORKSPACE}") {
             sh "docker tag http-server:latest 608022717509.dkr.ecr.us-east-1.amazonaws.com/http-server:latest"
      }
           dir("${WORKSPACE}") {
             sh "docker push 608022717509.dkr.ecr.us-east-1.amazonaws.com/http-server:latest"
     }
  }
    stage('Docker run') {
           dir("${WORKSPACE}") {
             sh "docker run -dti -p 85:8080 385571246247.dkr.ecr.us-east-1.amazonaws.com/task"
           }
         }
      }
    }

