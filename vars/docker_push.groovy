def call(String DockerHubUser, String ProjectName, String ImageTag, Number BuildNumber){
   withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                            docker tag ${DockerHubUser}/${ProjectName}:${ImageTag} ${DockerHubUser}/${ProjectName}:${BuildNumber}
                            docker push ${DockerHubUser}/${ProjectName}:${BuildNumber}

                            # Also push the latest tag
                            docker tag ${DockerHubUser}/${ProjectName}:latest ${DockerHubUser}/${ProjectName}:latest
                            docker push ${DockerHubUser}/${ProjectName}:latest
                        '''
    }
}
