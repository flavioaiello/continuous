# Toolstack for build and deploy automation 

This is the starting point to ramp up your software automation.

## Tooling
- [JenkinsCI](http://jenkins.vcap.me) DSL based on fully declarative jobs with pipelines and builtin bootstrap
- [Nexus3](http://nexus.vcap.me) as artifact repository and Docker Registry
- [HAProxy](http://haproxy.vcap.me:1111) offering human readable urls for above tooling
- [Portainer](http://portainer.vcap.me) offering docker management
- OpenVPN offering secure access to development team

## Getting started
It's very easy to quickly get started:
- Configure first your domain into the environment config file `.env` before you spin up the containers.
  For a local deployment on your workstation you can start with the default entry `vcap.me`.
- Configure your root directory on your host to map to the exposed volumes into the environment config file `.env` before you spin up the containers.
  For a local deployment on your workstation you can start with the default entry `/tmp`.
- Adapt the docker-compose.yml to your needs:
  - openvpn.CLIENTS: name of the vpn users for which keys and certs should be generated
  - openvpn.SERVICE_URL: if you already have a dns alias
  - jenkins.JENKINS_BOOTSTRAP_REPOSITORY: uri of your forked git repo
  - jenkins.REGISTRY_PASSWORD: must be the one from nexus
  - nexus.DOCKER_REPOSITORY_NAME: change, and be sure to use this name in your Jenkinsfiles
  - nexus.NEXUS_PASSWORD: change

Head over to your server and execute the required steps to get started:
```
cd continuous
docker-compose up -d --build --remove-orphans
```
The Jenkins seed job is already bundled into the JenkinsDsl repository, thus Jenkins will startup having a lookup on the `config` subdirectory of this repository. The seed job called "bootstrap" can be scheduled or executed ad-hoc and will generate jobs and views. 

## Pipelines

#### Configuration
Add the SSH key from src/root/.ssh/id_rsa.pub to your git project's or repsitory's access keys.
Please define your pipeline or multibranch pipeline jobs directly into `config/build.groovy` eg. `config/deploy.groovy`. If you wish to organize your jobs you can configure views directly in `config/views.groovy`. Use a `Jenkinsfile` on each software repository you wish to build. This setup is built in on jenkins using the plugins as described below.

#### JenkinsCI [Job DSL Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin)
Please refer to the [official tutorial](https://github.com/jenkinsci/job-dsl-plugin/wiki), [apidoc](https://jenkinsci.github.io/job-dsl-plugin/) and the [Job DSL Playground](http://job-dsl.herokuapp.com/). Not all of the 1000+ Jenkins plugins are supported by the built-in DSL. If the API Viewer does not list support for a certain plugin, the Automatically Generated DSL can be used to fill the gap.

#### JenkinsCI [Pipeline Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Pipeline+Plugin)
Please refer to the [official tutorial](https://github.com/jenkinsci/pipeline-plugin/blob/master/TUTORIAL.md).

Browse the Jenkins issue tracker to see any open issues on Jenkins and according plugins!
