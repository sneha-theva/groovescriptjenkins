pipeline{
    agent { node { label "$env.label"}}
    stages{
        stage('Code checkout'){
            steps{
                echo 'GIT PULL START'
                git branch: 'main', credentialsId: 'Git_cred', url: 'https://github.com/sneha-theva/groovescriptjenkins.git'
                echo 'GIT PULL END'
                echo '$WORKSPACE'

            }
        }

        stage('Create package'){
            steps{
                sh 'zip -r jenkins.zip Jenkins_pipeline/index.html'
            }
        }

        stage('EC2 Instance'){
            steps{
                sshagent(['AWSKey']) {
                    sh script:'''
                            
                            ssh -o StrictHostKeyChecking=no ec2-user@100.26.230.113 '
                                sudo su
                                sudo yum update -y
                                sudo yum install httpd -y
                                sudo service httpd start
                                sudo chkconfig httpd on
                                sudo cd /var/www/html
                                sudo chmod 777 /var/www/html
                            '
                            scp jenkins.zip ec2-user@100.26.230.113:/var/www/html
                            ssh ec2-user@100.26.230.113 '
                                cd /var/www/html
                                unzip jenkins.zip -d .
                            '
                            '''
                }
            }
        }
    }
}