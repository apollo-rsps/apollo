package org.apollo.build.plugin.compiler

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.KotlinSourceRoot

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class KotlinScriptCompiler {
    private String scriptDefinitionClass
    private Collection<File> classpath
    private MessageCollector messageCollector

    KotlinScriptCompiler(String scriptDefinitionClass, Collection<File> classpath, MessageCollector messageCollector) {
        this.scriptDefinitionClass = scriptDefinitionClass
        this.classpath = classpath
        this.messageCollector = messageCollector
    }

    KotlinScriptBinary compile(Path input, Path output) {
        def compilerConfiguration = KotlinCompilerConfigurationFactory.create(
                scriptDefinitionClass,
                classpath,
                messageCollector
        )

        def rootDisposable = Disposer.newDisposable()
        def configuration = compilerConfiguration.copy()

        output.toFile().mkdirs()

        configuration.put(CommonConfigurationKeys.MODULE_NAME, input.toString())
        configuration.add(JVMConfigurationKeys.CONTENT_ROOTS, new KotlinSourceRoot(input.toAbsolutePath().toString()))

        def configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
        def environment = KotlinCoreEnvironment.createForProduction(rootDisposable, configuration, configFiles)

        try {
            def generationState = KotlinToJVMBytecodeCompiler.INSTANCE.analyzeAndGenerate(environment)
            if (generationState == null) {
                throw new KotlinScriptCompilerException("Failed to generate bytecode for kotlin script")
            }

            def sourceFiles = environment.getSourceFiles()
            def script = sourceFiles[0].script
            if (script == null) {
                throw new KotlinScriptCompilerException("Main source file is not a script")
            }

            def scriptFilePath = script.fqName.asString().replace('.', '/') + ".class"
            def scriptFileClass = generationState.factory.get(scriptFilePath)

            if (scriptFileClass == null) {
                throw new KotlinScriptCompilerException("Unable to find compiled plugin class file $scriptFilePath")
            }

            generationState.factory.asList().forEach {
                Files.write(output.resolve(it.relativePath), it.asByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
            }

            return new KotlinScriptBinary(script.fqName.asString(), output.resolve(scriptFileClass.relativePath))
        } catch (ex) {
            throw new KotlinScriptCompilerException("Compilation failed", ex)
        } finally {
            Disposer.dispose(rootDisposable)
        }
    }
}
