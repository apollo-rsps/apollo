package org.apollo.build.compile

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.*
import org.jetbrains.kotlin.cli.jvm.compiler.*
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.codegen.CompilationException
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.script.KotlinScriptDefinitionFromAnnotatedTemplate
import java.io.File
import java.lang.management.ManagementFactory
import java.net.URISyntaxException
import java.net.URLClassLoader
import java.nio.file.*
import java.util.*


class KotlinMessageCollector : MessageCollector {

    override fun clear() {
    }

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation) {
        if (severity.isError) {
            println("${location.path}:${location.line}-${location.column}: $message")
            println(">>> ${location.lineContent}")
        }
    }

    override fun hasErrors(): Boolean {
        return false
    }

}

data class KotlinCompilerResult(val fqName: String, val outputPath: Path)

class KotlinScriptCompiler {

    val classpath: List<File>
    val messageCollector: MessageCollector
    val compilerConfiguration: CompilerConfiguration

    constructor(scriptDefinitionClassName: String, classpath: Collection<File>, messageCollector: MessageCollector) {
        this.classpath = classpath + currentClasspath()
        this.messageCollector = messageCollector
        this.compilerConfiguration = createCompilerConfiguration(scriptDefinitionClassName)
    }

    companion object {

        fun currentClasspath(): List<File> {
            val classLoader = Thread.currentThread().contextClassLoader as? URLClassLoader ?:
                    throw RuntimeException("Unable to resolve classpath for current ClassLoader")

            val classpathUrls = classLoader.urLs
            val classpath = ArrayList<File>()

            for (classpathUrl in classpathUrls) {
                try {
                    classpath.add(File(classpathUrl.toURI()))
                } catch (e: URISyntaxException) {
                    throw RuntimeException("URL returned by ClassLoader is invalid")
                }

            }

            val runtimeBean = ManagementFactory.getRuntimeMXBean()
            if (!runtimeBean.isBootClassPathSupported) {
                println("Warning!  Boot class path is not supported, must be supplied on the command line")
            } else {
                val bootClasspath = runtimeBean.bootClassPath
                classpath.addAll(bootClasspath.split(File.pathSeparatorChar).map { File(it) })
            }

            return classpath
        }
    }

    private fun createCompilerConfiguration(scriptDefinitionClassName: String): CompilerConfiguration {
        val classLoader = URLClassLoader(classpath.map { it.toURL() }.toTypedArray())
        val configuration = CompilerConfiguration()
        val scriptDefinitionClass = classLoader.loadClass(scriptDefinitionClassName)
        val scriptDefinition = KotlinScriptDefinitionFromAnnotatedTemplate(scriptDefinitionClass.kotlin)

        configuration.add(JVMConfigurationKeys.SCRIPT_DEFINITIONS, scriptDefinition)
        configuration.put(JVMConfigurationKeys.CONTENT_ROOTS, classpath.map { JvmClasspathRoot(it) })
        configuration.put(JVMConfigurationKeys.RETAIN_OUTPUT_IN_MEMORY, true)
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, KotlinMessageCollector())
        configuration.copy()

        return configuration
    }

    @Throws(KotlinPluginCompilerException::class)
    fun compile(inputPath: Path, outputPath: Path): KotlinCompilerResult {
        val rootDisposable = Disposer.newDisposable()
        val configuration = compilerConfiguration.copy()

        configuration.put(CommonConfigurationKeys.MODULE_NAME, inputPath.toString())
        configuration.addKotlinSourceRoot(inputPath.toAbsolutePath().toString())

        val configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
        val environment = KotlinCoreEnvironment.createForProduction(rootDisposable, configuration, configFiles)

        try {
            val generationState = KotlinToJVMBytecodeCompiler.analyzeAndGenerate(environment)
            if (generationState == null) {
                throw KotlinPluginCompilerException("Failed to generate bytecode for kotlin script")
            }

            val sourceFiles = environment.getSourceFiles()
            val script = sourceFiles[0].script ?: throw KotlinPluginCompilerException("Main script file isnt a script")

            val scriptFilePath = script.fqName.asString().replace('.', '/') + ".class"
            val scriptFileClass = generationState.factory.get(scriptFilePath)

            if (scriptFileClass == null) {
                throw KotlinPluginCompilerException("Unable to find compiled plugin class file $scriptFilePath")
            }

            generationState.factory.asList().forEach {
                Files.write(outputPath.resolve(it.relativePath), it.asByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
            }

            return KotlinCompilerResult(script.fqName.asString(), outputPath.resolve(scriptFileClass.relativePath))
        } catch (e: CompilationException) {
            throw KotlinPluginCompilerException("Compilation failed", e)
        } finally {
            Disposer.dispose(rootDisposable)
        }
    }

}

class KotlinPluginCompilerException(message: String, cause: Throwable? = null) : Exception(message, cause) {

}
