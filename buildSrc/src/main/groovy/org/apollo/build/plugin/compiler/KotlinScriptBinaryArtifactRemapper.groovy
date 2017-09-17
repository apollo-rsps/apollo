package org.apollo.build.plugin.compiler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.commons.RemappingClassAdapter

class KotlinScriptBinaryArtifactRemapper {
    String mainClassName

    KotlinScriptBinaryArtifactRemapper(String mainClassName) {
        this.mainClassName = mainClassName
    }

    KotlinScriptBinaryArtifact remapToPackage(KotlinScriptBinaryArtifact artifact, String packageName) {
        def reader = new ClassReader(new ByteArrayInputStream(artifact.data))
        def writer = new ClassWriter(0)
        def normalizedPackageName = packageName.replace('.', '/')
        def oldClassName = reader.getClassName()
        def newClassName = artifact.relativePath.replace(oldClassName, "$normalizedPackageName/$oldClassName")

        def remapper = new Remapper() {
            @Override
            String map(String typeName) {
                if (typeName.equals(mainClassName) || typeName.startsWith("$mainClassName\$")) {
                    return "$normalizedPackageName/$typeName"
                }

                return super.map(typeName);
            }
        }

        def remappingAdapter = new RemappingClassAdapter(writer, remapper)
        reader.accept(remappingAdapter, ClassReader.EXPAND_FRAMES)
        writer.visitEnd()

        return new KotlinScriptBinaryArtifact(newClassName, writer.toByteArray())
    }
}
