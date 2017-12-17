#!groovy

def repository = 'ssh://git@github.com:flavioaiello/continuous.git'

// Project and Component A
dslJob('projects/software-a/deploy/component-a/latest', true, repository, 'master', 'Jenkinsfile')
dslJob('projects/software-a/deploy/component-a/testing', true, repository, 'master', 'Jenkinsfile')
dslJob('projects/software-a/deploy/component-a/production', true, repository, 'master', 'Jenkinsfile')


// *** Base functions below ***
def dslJob(jobName, watchUpstream, remoteRepository, remoteBranch, jenkinsfile) {

    // create folder if not already present
    jobName.findIndexValues { it =~ /\// }.each {folder(jobName.substring(0,jobName.indexOf('/', (int)it)))}


    // create pipeline job
    pipelineJob(jobName) {

        definition getJobDefinition(remoteRepository, remoteBranch, jenkinsfile) 

        if (watchUpstream) {
            triggers {
                upstream(jobName.indexOf("dev") > -1 ? jobName.replaceAll(/.*\/(.*)\/.*/,'../../build-branches/$1') : jobName.replaceAll(/.*\/(.*)\/.*/,'../../build-releases/$1'), 'SUCCESS')
            }
        }
    }
}

def getJobDefinition(remoteRepository, remoteBranch, jenkinsfile) {
    return {
        cpsScm {
            scm {
                git {
                    remote {
                        url(remoteRepository)
                        credentials ('git')
                    }
                    branch(remoteBranch)
                }
            }
            scriptPath(jenkinsfile)
        }
    }
}
