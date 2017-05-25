package org.apollo.game.plugin.kotlin

import com.intellij.openapi.util.Disposer
import org.apollo.Server
import org.apollo.game.model.World
import org.apollo.game.plugin.PluginContext
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.codegen.CompilationException
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.addKotlinSourceRoot
import org.jetbrains.kotlin.script.KotlinScriptDefinitionFromAnnotatedTemplate
import java.io.File

class KotlinPluginCompiler(val classpath: List<File>, val messageCollector: MessageCollector) {

    private fun createCompilerConfiguration(inputPath: String): CompilerConfiguration {
        val configuration = CompilerConfiguration()
        val scriptDefinition = KotlinScriptDefinitionFromAnnotatedTemplate(KotlinPluginScript::class)

        configuration.add(JVMConfigurationKeys.SCRIPT_DEFINITIONS, scriptDefinition)
        configuration.put(JVMConfigurationKeys.CONTENT_ROOTS, classpath.map { JvmClasspathRoot(it) })
        configuration.put(JVMConfigurationKeys.RETAIN_OUTPUT_IN_MEMORY, true)
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
        configuration.put(CommonConfigurationKeys.MODULE_NAME, inputPath)
        configuration.addKotlinSourceRoot(inputPath)

        return configuration
    }

    @Throws(KotlinPluginCompilerException::class)
    fun compile(inputPath: String): Class<out KotlinPluginScript> {
        val rootDisposable = Disposer.newDisposable()
        val configuration = createCompilerConfiguration(inputPath)

        val configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
        val environment = KotlinCoreEnvironment.createForProduction(rootDisposable, configuration, configFiles)

        try {
            val clazz = KotlinToJVMBytecodeCompiler.compileScript(environment, Server::class.java.classLoader)

            if (clazz?.getConstructor(World::class.java, PluginContext::class.java) == null) {
                throw KotlinPluginCompilerException("Unable to compile $inputPath, no plugin constructor found")
            }

            return clazz as Class<out KotlinPluginScript>
        } catch (e: CompilationException) {
            throw KotlinPluginCompilerException("Compilation failed", e)
        } finally {
            Disposer.dispose(rootDisposable)
        }
    }

}

class KotlinPluginCompilerException(message: String, cause: Throwable? = null) : Exception(message, cause) {

}