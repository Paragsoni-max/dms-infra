def call(String DockerHubUser, String ProjectName, String ImageTag = "latest", String BuildNumber){
    if (!DockerHubUser || !ProjectName || !ImageTag || !BuildNumber) {
        error "Missing required parameters. Ensure DockerHubUser, ProjectName, ImageTag, and BuildNumber are set."
    }

    echo "Tagging and pushing Docker image:"
    echo "DockerHubUser: ${DockerHubUser}, ProjectName: ${ProjectName}, ImageTag: ${ImageTag}, BuildNumber: ${BuildNumber}"

    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials',
                     usernameVariable: 'DOCKER_USERNAME',
                     passwordVariable: 'DOCKER_PASSWORD')]) {
        sh """
            set -e
            docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
            docker tag ${DockerHubUser}/${ProjectName}:${ImageTag} ${DockerHubUser}/${ProjectName}:${BuildNumber}
            docker push ${DockerHubUser}/${ProjectName}:${BuildNumber}
            
            docker tag ${DockerHubUser}/${ProjectName}:${ImageTag} ${DockerHubUser}/${ProjectName}:latest
            docker push ${DockerHubUser}/${ProjectName}:latest
        """
    }
}
