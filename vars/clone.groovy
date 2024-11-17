def call(String GitUrl, String GitBranch, String GitCredentials){
  echo ("Started cloning the code")
    git branch: "${GitBranch}",
        url: "${GitUrl}",
        credentialsId: "${GitCredentials}"
}
