


buildPipelineView('myBuild') {
    title('Project A CI Pipeline')
    selectedJob('mygit')
}




job("mygit"){
  scm{
	github('shubhamkumar1689/DevOps1','master')
  }
  triggers{
	scm('* * * * *')
  }
  steps{
	  shell("sudo cp -rvf * /home/")
  }
  steps {
        dockerBuildAndPublish {
	    buildContext('/home/task6/')
            repositoryName('shubhamkumar98/httpdserver')
            tag('${BUILD_TIMESTAMP}-${GIT_REVISION,length=7}')
            registryCredentials('shubhamkumar98')
            forcePull(false)
            createFingerprints(false)
            skipDecorate()
        }
    }

}


job("myjob"){
  triggers{
	upstream("mygit", 'SUCCESS')
 }
  steps{
    shell('''if sudo kubectl get deploy myweb-deploy
then
sudo kubectl set image deploy myweb-deploy myweb-con=shubhamkumar98/myhttpserver:latest
else
sudo kubectl create -f /home/jenkins/deploy.yml
fi
if sudo kubectl get service myweb-deploy
then
echo "already "
else
sudo kubectl expose deploy myweb-deploy --port=80  --type=NodePort
fi''')
   }
}


job("third3"){
  triggers{
	upstream("myjob",'SUCCESS')
 }
}
job("fourth4"){
  triggers{
	upstream("third3",'FAILURE')
 }
}
