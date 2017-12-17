#!groovy

// Based on folder structure
def ENV = env.JOB_NAME.substring(env.JOB_NAME.lastIndexOf('/') + 1)
def PROJECT = env.JOB_NAME.split('/')[1]
def STACK = PROJECT + "-" + ENV

// Presets and maps
def DOCKER_CREDENTIALSID = 'docker'
def REGISTRY = "localhost:5000/default"

node {
    stage('Env') {
        echo '*** Show env variables: ***' + \
             '\n Environment: ' + ENV + \
             '\n Project: ' + PROJECT + \
             '\n Stack: ' + STACK + \
             '\n Registry: ' + REGISTRY + \
             '\n DockerCredentialsId: ' + DOCKER_CREDENTIALSID
    }

    stage('Checkout') {
        checkout scm
    }

    stage('Deploy') {
        withEnv(["REGISTRY=${REGISTRY}", "PROJECT=${PROJECT}", "ENV=${ENV}", "STACK=${STACK}", "CREDENTIALSID=${DOCKER_CREDENTIALSID}"]) {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: CREDENTIALSID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                sh '''
                    echo "*** Set recipe values for placeholders ***"
                    sed -i -e "s#\\${REGISTRY}#${REGISTRY}#g" -e "s#\\${PROJECT}#${PROJECT}#g" -e "s#\\${ENV}#${ENV}#g" -e "s#\\${STACK}#${STACK}#g" stacks/${STACK}.yml
                    echo "*** Select tags for images according to release definition ***"
                    export RELEASE=$(cat instances/${STACK}.${ENV})
                    while read IMAGE; do \\
                        sed -i \"s@${IMAGE%:*}:\\${TAG}@${IMAGE%:*}:${IMAGE#*:}@g\" stacks/${STACK}.yml
                    done < releases/${STACK}.${RELEASE}
                        
                    echo "*** Show resulting docker-compose.yml file ***"
                    cat stacks/${STACK}.yml

                    echo "*** Login to registry ***"
                    docker login -u ${USERNAME} -p ${PASSWORD} ${REGISTRY}
                    
                    echo "*** spin-up all containers ***"
                    docker stack deploy -c stacks/${STACK}.yml ${STACK} --with-registry-auth
                '''
            }
        }
    }
}
