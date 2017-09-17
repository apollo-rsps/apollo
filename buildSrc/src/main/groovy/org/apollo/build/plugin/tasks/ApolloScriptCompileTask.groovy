package org.apollo.build.plugin.tasks

import org.apollo.build.plugin.ApolloPluginExtension
import org.apollo.build.plugin.compiler.KotlinScriptBinaryArtifactRemapper
import org.apollo.build.plugin.compiler.KotlinScriptCompiler
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector

import java.nio.file.Files
import java.nio.file.StandardOpenOption

class ApolloScriptCompileTask extends DefaultTask {
    @OutputDirectory
    File outputsDir

    @Input
    FileCollection compileClasspath

    @Input
    String scriptDefinitionClass

    @TaskAction
    def execute(IncrementalTaskInputs inputs) {
        def extension = getProject().getExtensions().getByType(ApolloPluginExtension.class);
        def packageName = extension.packageName

        if (scriptDefinitionClass == null) {
            throw new Exception("No script definition class given")
        }

        if (compileClasspath == null) {
            throw new Exception("No compile classpath given")
        }

        def classpath = compileClasspath.files
        def messageCollector = new PrintingMessageCollector(System.err, MessageRenderer.PLAIN_RELATIVE_PATHS, true);
        def compiler = new KotlinScriptCompiler(scriptDefinitionClass, classpath, messageCollector)

        outputsDir.mkdirs()

        inputs.outOfDate {
            removeBinariesFor(it.file)

            def binary = compiler.compile(it.file.toPath())
            def binaryArtifactRemapper = new KotlinScriptBinaryArtifactRemapper(binary.mainClassName)
            def artifacts = binary.artifacts.collect { binaryArtifactRemapper.remapToPackage(it, packageName) }

            artifacts.each {
                def artifactOutput = outputsDir.toPath().resolve(it.relativePath)

                Files.createDirectories(artifactOutput.getParent())
                Files.write(artifactOutput, it.data,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.TRUNCATE_EXISTING
                )
            }
        }

        inputs.removed {
            removeBinariesFor(it.file)
        }
    }

    def removeBinariesFor(File file) {
        def normalizedFilename = file.name.replace("[^A-Z_]", "_")
        def normalizedPrefix = normalizedFilename.subSequence(0, normalizedFilename.lastIndexOf('.'))

        FileFilter filter = {
            return it.name.startsWith(normalizedPrefix)
        }

        def binaries = outputsDir.listFiles(filter)
        binaries.each {
            it.delete()
        }
    }
}
