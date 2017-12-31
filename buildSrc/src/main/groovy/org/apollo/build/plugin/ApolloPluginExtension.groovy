package org.apollo.build.plugin

import org.apollo.build.plugin.tasks.ApolloScriptCompileTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class ApolloPluginExtension {
    final Project project

    /**
     * The name of this plugin (defaults to the project name).
     */
    String name

    /**
     * The package that plugin scripts (.kts files) will be packaged under for this plugin.
     */
    String packageName = "org.apollo.game.plugins"

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

    private def addDependencyOn(Project other, String configuration, boolean includeProject) {
        def compileConfiguration = other.configurations.getByName("compile")
        def sources = other.sourceSets.main
        def deps = compileConfiguration.dependencies

        deps.each {
            project.dependencies.add(configuration, it)
        }

        project.dependencies.add(configuration, sources.output)
        if (includeProject) {
            project.dependencies.add(configuration, other)
        }
    }

    /**
     * Setup the {@link Project} with the correct dependencies and tasks required to build the plugin
     * and its scripts.
     */
    def init() {
        def gameProject = project.findProject(":game")
        def pluginTestingProject = project.findProject(':game:plugin-testing')

        project.plugins.apply('kotlin')
        project.sourceSets {
            main {
                kotlin {
                    srcDir this.srcDir
                    exclude '**/*.kts'
                }
            }

            test {
                kotlin {
                    srcDir this.testDir
                }
            }
        }

        def mainSources = project.sourceSets.main

        addDependencyOn(gameProject, "compile",  false)
        addDependencyOn(pluginTestingProject, "testCompile", true)

        def buildTask = project.tasks['classes']

        FileTree scripts = project.fileTree(srcDir).matching {
            include '**/*.kts'
        }

        project.tasks.create('compileScripts', ApolloScriptCompileTask) {
            def outputDir = mainSources.output.classesDir

            inputs.files scripts.files
            outputsDir = outputDir

            compileClasspath = mainSources.compileClasspath + mainSources.runtimeClasspath + mainSources.output
            scriptDefinitionClass = "org.apollo.game.plugin.kotlin.KotlinPluginScript"
            mustRunAfter buildTask
        }

        buildTask.finalizedBy(project.tasks['compileScripts'])
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
