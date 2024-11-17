def call(){
   withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''
                            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
                            docker tag ${DOCKER_REPO}:latest ${DOCKER_REPO}:${BUILD_NUMBER}
                            docker push ${DOCKER_REPO}:${BUILD_NUMBER}

                            # Also push the latest tag
                            docker tag ${DOCKER_REPO}:latest ${DOCKER_REPO}:latest
                            docker push ${DOCKER_REPO}:latest
                        '''
    }
}
