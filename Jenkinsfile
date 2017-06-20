node {
  stage 'Stage Checkout'
  checkout scm

  stage 'Stage Build'
  gradle 'clean assemble'

  stage 'Stage Test'
  gradle 'check'
}

def gradle(command) {
    sh "${tool name: 'gradle', type: 'hudson.plugins.gradle.GradleInstallation'}/bin/gradle ${command}"
}