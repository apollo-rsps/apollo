package org.apollo.build.plugin.tasks

import org.apollo.build.plugin.compiler.KotlinScriptCompiler
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector

class ApolloScriptCompileTask extends DefaultTask {
    File outputsDir

    @Input
    FileCollection compileClasspath

    @Input
    String scriptDefinitionClass

    @TaskAction
    def execute(IncrementalTaskInputs inputs) {
        if (scriptDefinitionClass == null) {
            throw new Exception("No script definition class given")
        }

        if (compileClasspath == null) {
            throw new Exception("No compile classpath given")
        }

        def classpath = compileClasspath.files
        def messageCollector = new PrintingMessageCollector(System.err, MessageRenderer.PLAIN_RELATIVE_PATHS, true);
        def compiler = new KotlinScriptCompiler(scriptDefinitionClass, classpath, messageCollector)

        inputs.outOfDate {
            removeBinariesFor(it.file)
            compiler.compile(it.file.toPath(), outputsDir.toPath())
        }

        inputs.removed {
            removeBinariesFor(it.file)
        }
    }

    def removeBinariesFor(File file) {
        def normalizedFilename = file.name.replace("[^A-Z_]", "_")
        def normalizedPrefix = normalizedFilename.subSequence(0, normalizedFilename.lastIndexOf('.'))

        FileFilter filter = {
            it.name.startsWith(normalizedPrefix)
        }

        def binaries = outputsDir.listFiles FileFilter { dir, name -> name.startsWith(normalizedPrefix) }

        binaries.forEach {
            it.delete()
        }
    }
}
