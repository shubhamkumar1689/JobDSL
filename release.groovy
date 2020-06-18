


buildPipelineView('myild') {
    filterBuildQueue()
    filterExecutors()
    title('Project A CI Pipeline')
    displayedBuilds(5)
    selectedJob('mygit')
    alwaysAllowManualTrigger(false)
    showPipelineParameters()
    refreshFrequency(60)
}



job("mygit"){
  scm{
	github('shubhamkumar1689/DevOps1','master')
  }
  triggers{
	scm('* * * * *')
  }
  steps {
        dockerBuildAndPublish {
            repositoryName('shubhamkumar98/httpdserver')
            tag('latest')
            registryCredentials('shubhamkumar98')
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
sudo kubectl create -f /home/jenkins/web-pvc1.yml
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
	steps{
		shell('''status = $(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.101:32/web.html
		if [[ $status ==200 ]]
		then
		exit 0
		else
		exit 1
		fi
		''')
	}
}
job("fourth4"){
  triggers{
	upstream("third3",'FAILURE')
 }
	post{
		failure {  
             		mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "foo@foomail.com";  
         	} 
	}
}
