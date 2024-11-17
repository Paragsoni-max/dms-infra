def call(String url, String branch, String credentials){
  echo ("Started cloning the code")
    git branch: branch,
        url: url,
        credentialsId: credentials
}
