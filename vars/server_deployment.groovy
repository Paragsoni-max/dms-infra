def call(String BackendVMIP, String SshCredentialsId, String DockerHubUser, String ProjectName, String ImageTag, String ContainerName) {
    // Log parameters for debugging
    echo "BackendVMIP: ${BackendVMIP}"
    echo "SshCredentialsId: ${SshCredentialsId}"
    echo "DockerHubUser: ${DockerHubUser}"
    echo "ProjectName: ${ProjectName}"
    echo "ImageTag: ${ImageTag}"
    echo "ContainerName: ${ContainerName}"
    
    withCredentials([sshUserPrivateKey(credentialsId: SshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        echo "SSH_USER: \$SSH_USER"
        echo "SSH_KEY_FILE: \$SSH_KEY_FILE"
        
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$BackendVMIP << EOF
        # Log connection status
        echo "Connected to ${BackendVMIP}"
        
        // # Stop and remove existing container
        // docker stop ${ContainerName} || true
        // docker rm ${ContainerName} || true
        
        // # Pull the latest Docker image from Docker Hub
        // echo "Pulling image: ${DockerHubUser}/${ProjectName}:${ImageTag}"
        // docker pull ${DockerHubUser}/${ProjectName}:${ImageTag}
        
        // # Run the new container in detached mode on port 5000
        // echo "Starting container: ${ContainerName}"
        // docker run --restart always --name ${ContainerName} -p 5000:5000 ${DockerHubUser}/${ProjectName}:${ImageTag}
       << EOF
        """
    } 
} 

