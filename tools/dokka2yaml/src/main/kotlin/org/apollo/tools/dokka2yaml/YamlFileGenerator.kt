package org.apollo.tools.dokka2yaml

import org.jetbrains.dokka.DocumentationNode
import org.jetbrains.dokka.FormatService
import org.jetbrains.dokka.FormattedOutputBuilder
import org.jetbrains.dokka.Location

class YamlFileGenerator(override val extension: String = ".yaml") : FormatService {
    override fun createOutputBuilder(to: StringBuilder, location: Location): FormattedOutputBuilder {
        return YamlFileOutputBuilder()
    }
}

class YamlFileOutputBuilder : FormattedOutputBuilder {
    override fun appendNodes(nodes: Iterable<DocumentationNode>) {
        for (node in nodes) {
            appendNode(node)
        }
    }

    fun appendNode(node: DocumentationNode) {

    }
}