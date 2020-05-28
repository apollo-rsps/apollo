package org.apollo.tools.dokka2yaml

import com.google.inject.Binder
import org.jetbrains.dokka.FormatService
import org.jetbrains.dokka.Formats.FormatDescriptor
import org.jetbrains.dokka.Formats.KotlinFormatDescriptorBase
import kotlin.reflect.KClass

class YamlFileFormatDescriptor : KotlinFormatDescriptorBase() {
    override val formatServiceClass = YamlFileGenerator::class
    override fun configureAnalysis(binder: Binder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun configureOutput(binder: Binder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}