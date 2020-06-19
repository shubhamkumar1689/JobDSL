


buildPipelineView('BuildPipeline') {
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
            	registryCredentials('mydocker')
		forceTag(false)
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
		sudo kubectl set image deploy myweb-deploy myweb-con=shubhamkumar98/httpdserver:latest
		else
		sudo kubectl create -f /home/jenkins/web-pvc.yml
		sudo kubectl create -f /home/jenkins/deploy.yml
		fi
		if sudo kubectl get service myweb-deploy
		then
		echo "already "
		else
		sudo kubectl create -f /home/jenkins/service.yml
		fi''')
   	}
}


job("third3"){
  	triggers{
		upstream("myjob",'SUCCESS')
 	}
	steps{
		shell('''status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.101:30036/web.html)
		if [[ $status == 200 ]]
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
    	publishers {
        	mailer('shubhaminbox061@gmail.com', true, true)
    	}
}
