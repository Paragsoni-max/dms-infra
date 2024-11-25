def call(String BackendVMIP, String SshCredentialsId, String DockerHubUser, String ProjectName, String ImageTag, String ContainerName) {
    withCredentials([sshUserPrivateKey(credentialsId: SshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$BackendVMIP << EOF
        # Pull the latest Docker image from Docker Hub
        docker pull ${DockerHubUser}\\/${ProjectName}:${ImageTag}
        
        # Run the new container in detached mode on port 5000
        docker run --restart always --name ${ContainerName} -p 5000:5000 ${DockerHubUser}\\/${ProjectName}:${ImageTag}
        << EOF
        """
    }
}
