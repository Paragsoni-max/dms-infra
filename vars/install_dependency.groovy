def call(String NodeVersion) {
    sh '''
        export NVM_DIR="$HOME/.nvm"
        [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
        
        if ! nvm ls "${NodeVersion}" | grep -q "${NodeVersion}"; then
            echo "Node.js version ${NodeVersion} not found. Installing..."
            nvm install "${NodeVersion}"
        else
            echo "Node.js version ${NodeVersion} is already installed."
        fi

        nvm use "${NodeVersion}"
        npm install
    '''
}
