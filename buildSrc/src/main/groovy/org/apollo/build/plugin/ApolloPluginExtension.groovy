package org.apollo.build.plugin

import org.gradle.api.Project

class ApolloPluginExtension {
    final Project project

    /**
     * The name of this plugin (defaults to the project name).
     */
    String name

    /**
     * An optional description of this plugin.
     */
    String description = "Empty description"

    /**
     * A list of other {@link ApolloPlugin}s that this plugin depends on.
     */
    List<String> dependencies = []

    /**
     * A list of others who contributed to this plugin.
     */
    List<String> authors = []

    /**
     * The directory that library files and script files are found under.
     */
    final String srcDir = "src/"

    /**
     * The directory that tests are found under.
     */
    final String testDir = "test/"

    ApolloPluginExtension(Project project) {
        this.project = project
        this.name = project.name

        init()
    }

    /**
     * Setup the {@link Project} with the correct dependencies and tasks required to build the plugin
     * and its scripts.
     */
    def init() {
        def gameProject = project.findProject(":game")
        def pluginTestingProject = project.findProject(':game:plugin-testing')

        project.plugins.apply('kotlin')
        project.dependencies {
            def transitiveGameDeps = gameProject.configurations["compile"].dependencies
            def gameSources = gameProject.sourceSets.main

            transitiveGameDeps.each { dependency ->
                compile dependency
            }

            compile gameSources.output
            testCompile pluginTestingProject
        }

        project.sourceSets {
            main.kotlin.srcDirs += this.srcDir
            test.kotlin.srcDirs += this.testDir
        }

        project.tasks["compileKotlin"].kotlinOptions.freeCompilerArgs += [
                "-script-templates", "org.apollo.game.plugin.kotlin.KotlinPluginScript"
        ]
    }

    def getDependencies() {
        return dependencies
    }

    def setDependencies(List<String> dependencies) {
        dependencies.each {
            project.dependencies.add('compile', project.findProject(":game:plugin:$it"))
        }

        this.dependencies = dependencies
    }
}
