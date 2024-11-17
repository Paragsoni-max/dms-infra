def call(String FrontendVMIP,String SshCredentialsId, String DockerHubUser, String ProjectName, String ImageTag, String ContainerName) {
    withCredentials([sshUserPrivateKey(credentialsId: SshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$FrontendVMIP << EOF
        # Pull the latest Docker image from Docker Hub
        docker pull ${DockerHubUser}/${ProjectName}:${ImageTag}
        
        echo 'Stopping and removing all containers if they exist...'
      # Check if port 80 is in use and stop/remove the container
                    echo "Checking if port 80 is in use..."
                    container_id=$(sudo docker ps -q --filter "publish=80")
                    if [ ! -z "$container_id" ]; then
                        echo "Stopping and removing container using port 80: $container_id"
                        sudo docker stop $container_id
                        sudo docker rm $container_id
                    fi
                    
      # Run the new container in detached mode on port 80
        docker run -d -p 80:80 --name ${ContainerName} ${DockerHubUser}/${ProjectName}:${ImageTag}
       << EOF
        """
    }
}
