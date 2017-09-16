package org.apollo.build.plugin.compiler

class KotlinScriptCompilerException extends Exception {
    KotlinScriptCompilerException(String message) {
        super(message)
    }

    KotlinScriptCompilerException(String message, Throwable cause) {
        super(message, cause)
    }
}
