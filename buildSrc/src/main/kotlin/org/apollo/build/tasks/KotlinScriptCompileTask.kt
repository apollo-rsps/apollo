package org.apollo.build.tasks

import org.apollo.build.compile.KotlinMessageCollector
import org.apollo.build.compile.KotlinScriptCompiler
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

open class KotlinScriptCompileTask : DefaultTask() {
    @OutputDirectory
    var outputsDir: File? = null

    @Input
    var compileClasspath: FileCollection? = null

    @Input
    var scriptDefinitionClass: String? = null

    @TaskAction
    fun execute(inputs: IncrementalTaskInputs) {
        if (scriptDefinitionClass == null) {
            throw Exception("No script definition class given")
        }

        if (compileClasspath == null) {
            throw Exception("No compile classpath given")
        }

        val classpath = compileClasspath!!.files
        val compiler = KotlinScriptCompiler(scriptDefinitionClass!!, classpath, KotlinMessageCollector())

        inputs.outOfDate {
            removeBinariesFor(it.file)
            compiler.compile(it.file.toPath(), outputsDir!!.toPath())
        }

        inputs.removed {
            removeBinariesFor(it.file)
        }
    }

    private fun removeBinariesFor(file: File) {
        val normalizedFilename = file.name.replace("[^A-Z_]", "_")
        val normalizedPrefix = normalizedFilename.subSequence(0, normalizedFilename.lastIndexOf('.'))

        val binaries = outputsDir!!.listFiles { dir, name -> name.startsWith(normalizedPrefix) }

        binaries.forEach {
            it.delete()
        }
    }
}
