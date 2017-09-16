package org.apollo.build.plugin

import org.apollo.build.plugin.tasks.ApolloScriptCompileTask
import org.gradle.api.Project
import org.gradle.api.file.FileTree

class ApolloPluginExtension {
    private Project project

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
                    exclude '*.kts'
                }
            }

            test {
                kotlin {
                    srcDir this.testDir
                }
            }
        }

        project.dependencies.add('compile', gameProject)
        project.dependencies.add('testCompile', pluginTestingProject.sourceSets.test.output)

        dependencies.each {
            project.dependencies.add('compile', project.findProject(":game:plugin:$it"))
        }

        project.tasks.create('compileScripts', ApolloScriptCompileTask) {
            def mainSources = project.sourceSets.main
            def outputDir = mainSources.output.classesDir

            FileTree filtered = project.fileTree(srcDir).matching {
                include '*.kts'
            }

            inputs.files filtered.files
            outputsDir = outputDir

            compileClasspath = mainSources.compileClasspath +
                    mainSources.runtimeClasspath

            scriptDefinitionClass = "org.apollo.game.plugin.kotlin.KotlinPluginScript"
            mustRunAfter project.tasks['classes']
        }
    }
}
