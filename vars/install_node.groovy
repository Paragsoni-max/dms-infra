def call(String NvmDirectory) {
    sh """
        mkdir -p ${NvmDirectory}
        if [ ! -s "${NvmDirectory}/nvm.sh" ]; then
            echo "Installing NVM..."
            curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.4/install.sh | bash
        fi
    """
    
    // Source nvm.sh and install Node.js
    sh """
        export NVM_DIR="${NvmDirectory}"
        [ -s "${NvmDirectory}/nvm.sh" ] && . "${NvmDirectory}/nvm.sh"
        nvm install 20
        nvm use 20
        node --version
    """
}
