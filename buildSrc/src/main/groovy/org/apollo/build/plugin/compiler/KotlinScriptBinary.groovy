package org.apollo.build.plugin.compiler

import java.nio.file.Path

class KotlinScriptBinary {
    final String fullyQualifiedName
    final Path output

    KotlinScriptBinary(String fullyQualifiedName, Path output) {
        this.output = output
        this.fullyQualifiedName = fullyQualifiedName
    }
}
