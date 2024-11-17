def call(String FrontendVMIP, String SshCredentialsId, String DockerHubUser, String ProjectName, String ImageTag, String ContainerName) {
    withCredentials([sshUserPrivateKey(credentialsId: SshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$FrontendVMIP << EOF
        # Pull the latest Docker image from Docker Hub
        docker pull ${DockerHubUser}/${ProjectName}:${ImageTag}
        
        echo 'Stopping and removing all containers if they exist...'
        # Stop and remove all containers
        if [ $(docker ps -aq) ]; then
        docker stop $(docker ps -aq) || true
        docker rm $(docker ps -aq) || true
        fi
        
        # Run the new container in detached mode on port 80
        docker run -d -p 80:80 --name ${ContainerName} ${DockerHubUser}/${ProjectName}:${ImageTag}
       << EOF
        """
    }
}
