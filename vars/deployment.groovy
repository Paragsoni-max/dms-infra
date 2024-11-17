def call(String frontendVMIP, String dockerImage, String sshCredentialsId) {
    withCredentials([sshUserPrivateKey(credentialsId: sshCredentialsId, keyFileVariable: 'SSH_KEY_FILE', usernameVariable: 'SSH_USER')]) {
        sh """
        ssh -i \$SSH_KEY_FILE -o StrictHostKeyChecking=no \$SSH_USER@$frontendVMIP << EOF
        # Pull the latest Docker image from Docker Hub
        docker pull ${dockerImage}
        
        echo 'Stopping and removing all containers if they exist...'
        # Stop and remove all containers
        if [ \$(docker ps -aq) ]; then
            docker stop \$(docker ps -aq) || true
            docker rm \$(docker ps -aq) || true
        fi

        # Run the new container in detached mode on port 80
        docker run -d -p 80:80 --name frontend-container ${dockerImage}
        EOF
        """
    }
}
