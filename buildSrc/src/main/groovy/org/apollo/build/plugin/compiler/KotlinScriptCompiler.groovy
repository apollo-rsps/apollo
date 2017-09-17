package org.apollo.build.plugin.compiler

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.java.PsiPackageStatementImpl
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.KotlinSourceRoot

import java.nio.file.Path

class KotlinScriptCompiler {
    private String scriptDefinitionClass
    private Collection<File> classpath
    private MessageCollector messageCollector

    KotlinScriptCompiler(String scriptDefinitionClass, Collection<File> classpath, MessageCollector messageCollector) {
        this.scriptDefinitionClass = scriptDefinitionClass
        this.classpath = classpath
        this.messageCollector = messageCollector
    }

    KotlinScriptBinary compile(Path input) {
        def compilerConfiguration = KotlinCompilerConfigurationFactory.create(
                scriptDefinitionClass,
                classpath,
                messageCollector
        )

        def rootDisposable = Disposer.newDisposable()
        def configuration = compilerConfiguration.copy()


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

            def outputs = generationState.factory.asList()
            def artifacts = outputs.collect { new KotlinScriptBinaryArtifact(it.relativePath, it.asByteArray()) }

            return new KotlinScriptBinary(script.fqName.asString(), artifacts)
        } catch (ex) {
            throw new KotlinScriptCompilerException("Compilation failed", ex)
        } finally {
            Disposer.dispose(rootDisposable)
        }
    }
}
