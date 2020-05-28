package org.apollo.game.plugin.detekt.rules

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration

class DeclarationInScriptRule : Rule() {
    override val issue = Issue(
        "DeclarationInScript",
        Severity.CodeSmell,
        "This rule reports a plugin file containing class or object declarations.",
        Debt.FIVE_MINS
    )

    override fun visit(root: KtFile) {
        super.visit(root)

        val script = root.script ?: return
        val declarations = script.declarations.filter { it is KtClass || it is KtObjectDeclaration }

        declarations
            .forEach {
                report(CodeSmell(
                    issue,
                    Entity.from(it),
                    message = "Declaration of ${it.name} should live in a top-level file, not a script"
                ))
            }
    }
}