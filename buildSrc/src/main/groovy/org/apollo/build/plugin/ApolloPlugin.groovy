package org.apollo.build.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ApolloPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('apolloPlugin', ApolloPluginExtension, project)
    }
}
