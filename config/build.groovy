#!groovy

// Add your projects below
dslJob('project/software-a/build-master/component-a', 'git@github.com:project/component-a.git', 'master', 'Jenkinsfile')
dslJob('project/software-a/build-latest/component-a', 'git@github.com:project/component-a.git', ':origin/tags/v(\\d+.\\d+.\\d+)', 'Jenkinsfile')


// *** Base functions below ***
def dslJob(jobName, remoteRepository, remoteBranch, jenkinsfile) {

    // create folder if not already present
    jobName.findIndexValues { it =~ /\// }.each {folder(jobName.substring(0,jobName.indexOf('/', (int)it)))}

    // create pipeline job
    pipelineJob(jobName) {
        definition getJobDefinition(remoteRepository, remoteBranch, jenkinsfile)
        triggers {
            scm('H/5 * * * *') /* Every 5 minutes */
        }
        logRotator {
            numToKeep(7)
            artifactNumToKeep(7)
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
                        if (remoteBranch == ':origin/tags/v(\\d+.\\d+.\\d+)') {
                            name('origin')
                            refspec('+refs/tags/*:refs/remotes/origin/tags/*')
                        }
                    }
                    branch(remoteBranch)
                }
            }
            scriptPath(jenkinsfile)
        }
    }
}

