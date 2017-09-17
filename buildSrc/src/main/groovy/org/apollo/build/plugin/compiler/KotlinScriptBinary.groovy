package org.apollo.build.plugin.compiler

import java.nio.file.Path

class KotlinScriptBinaryArtifact {
    final String relativePath
    final byte[] data

    KotlinScriptBinaryArtifact(String relativePath, byte[] data) {
        this.relativePath = relativePath
        this.data = data
    }
}

class KotlinScriptBinary {
    final String mainClassName
    final List<KotlinScriptBinaryArtifact> artifacts

    KotlinScriptBinary(String mainClassName, List<KotlinScriptBinaryArtifact> artifacts) {
        this.mainClassName = mainClassName
        this.artifacts = artifacts
    }
}
