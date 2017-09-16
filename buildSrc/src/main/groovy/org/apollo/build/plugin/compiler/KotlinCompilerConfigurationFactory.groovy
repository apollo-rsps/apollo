package org.apollo.build.plugin.compiler

import kotlin.jvm.JvmClassMappingKt
import kotlin.reflect.KClass
import kotlin.reflect.full.KClasses
import kotlin.reflect.jvm.internal.KClassImpl
import kotlin.script.templates.ScriptTemplateDefinition
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.script.KotlinScriptDefinitionFromAnnotatedTemplate

import java.lang.management.ManagementFactory

class KotlinCompilerConfigurationFactory {

    static CompilerConfiguration create(String scriptDefinitionClassName, Collection<File> classpath, MessageCollector messageCollector) {
        def parentClassLoader = (URLClassLoader) Thread.currentThread().contextClassLoader
        if (parentClassLoader == null) {
            throw new RuntimeException("Unable to find current classloader")
        }

        URL[] classpathUrls = parentClassLoader.getURLs()

        for (classpathUrl in classpathUrls) {
            try {
                classpath.add(new File(classpathUrl.toURI()))
            } catch (ex) {
                throw new RuntimeException("URL returned by ClassLoader is invalid", ex)
            }

        }

        def runtimeBean = ManagementFactory.getRuntimeMXBean()
        if (!runtimeBean.bootClassPathSupported) {
            println("Warning!  Boot class path is not supported, must be supplied on the command line")
        } else {
            def bootClasspath = runtimeBean.bootClassPath
            classpath.addAll(bootClasspath.split(File.pathSeparatorChar.toString()).collect { new File(it) })
        }


        def classLoader = new URLClassLoader(classpath.collect { it.toURL() }.toArray(new URL[classpath.size()]))
        def configuration = new CompilerConfiguration()
        def scriptDefinitionClass = classLoader.loadClass(scriptDefinitionClassName)
        def classpathFiles = classpath.collect { it }

        def scriptDefinition = new KotlinScriptDefinitionFromAnnotatedTemplate(JvmClassMappingKt.getKotlinClass(scriptDefinitionClass),
                null, null, null, classpathFiles)

        configuration.add(JVMConfigurationKeys.SCRIPT_DEFINITIONS, scriptDefinition)
        configuration.put(JVMConfigurationKeys.CONTENT_ROOTS, classpath.collect { new JvmClasspathRoot(it) })
        configuration.put(JVMConfigurationKeys.RETAIN_OUTPUT_IN_MEMORY, true)
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
        configuration.copy()

        return configuration
    }
}
