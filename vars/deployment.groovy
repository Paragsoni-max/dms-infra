def call(String VirtualMachineIP, String SshCredentialsId, String DockerHubUser, String ProjectName, String ImageTag, String ContainerName, String PortMapping, String EnvFilePath) {
        echo "SshCredentialsId: ${SshCredentialsId}"
    echo "DockerHubUser: ${DockerHubUser}"
    echo "ProjectName: ${ProjectName}"
    echo "ImageTag: ${ImageTag}"
    echo "ContainerName: ${ContainerName}"
    echo "Portmapping: ${PortMapping}"

    withCredentials([sshUserPrivateKey(credentialsId: SshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$VirtualMachineIP << EOF
        # Pull the latest Docker image from Docker Hub
        docker pull ${DockerHubUser}\\/${ProjectName}:${ImageTag}
        
        echo 'Checking if a container with the same name exists...'
        # Check if a container with the same name exists
        if [ \$(docker ps -aq -f name=${ContainerName}) ]; then
            echo 'Stopping and removing the existing container...'
            # Stop and remove the container with the same name
            docker stop ${ContainerName} || true
            docker rm ${ContainerName} || true
        fi
        
        # Check if any container is using the same port
        CONTAINER_PORT=\$(docker ps --format "{{.Ports}}" -f "name=${ContainerName}" | grep -oP "(?<=:)[0-9]{1,5}(?=->\${PortMapping%:*})")
        if [ -n "\$CONTAINER_PORT" ]; then
            echo "Stopping and removing container running on the same port (\$CONTAINER_PORT)..."
            # Stop and remove the container running on the same port
            docker stop \$(docker ps -q --filter "publish=\${PortMapping%:*}") || true
            docker rm \$(docker ps -q --filter "publish=\${PortMapping%:*}") || true
        fi

        # Run the new container in detached mode with the specified port mapping
        docker compose up -d
       << EOF
        """
    }
}
