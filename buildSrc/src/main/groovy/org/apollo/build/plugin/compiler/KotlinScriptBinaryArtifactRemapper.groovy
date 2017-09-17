package org.apollo.build.plugin.compiler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.commons.Remapper
import org.objectweb.asm.commons.RemappingClassAdapter
import org.objectweb.asm.tree.ClassNode

class KotlinScriptBinaryArtifactRemapper {
    final String originalSourceFileName
    final String mainClassName

    KotlinScriptBinaryArtifactRemapper(String originalSourceFileName, String mainClassName) {
        this.originalSourceFileName = originalSourceFileName
        this.mainClassName = mainClassName
    }

    KotlinScriptBinaryArtifact remapToPackage(KotlinScriptBinaryArtifact artifact, String packageName) {
        def node = new ClassNode()
        def writer = new ClassWriter(0)
        def reader = new ClassReader(new ByteArrayInputStream(artifact.data))
        reader.accept(node, ClassReader.EXPAND_FRAMES)

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
        node.accept(remappingAdapter)
        writer.visitSource(originalSourceFileName, null)
        writer.visitEnd()

        return new KotlinScriptBinaryArtifact(newClassName, writer.toByteArray())
    }
}
