pipeline  { 
    agent { node { label "$env.label"}}
    stages{
        stage('code checkout'){
            steps{
                git branch: 'main', 
            }
        }
    }
}