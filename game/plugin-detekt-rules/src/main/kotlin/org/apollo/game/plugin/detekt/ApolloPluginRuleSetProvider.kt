package org.apollo.game.plugin.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import org.apollo.game.plugin.detekt.rules.DeclarationInScriptRule

class ApolloPluginRuleSetProvider : RuleSetProvider {
    override val ruleSetId = "apollo-plugin"

    override fun instance(config: Config): RuleSet {
        return RuleSet(ruleSetId, listOf(
            DeclarationInScriptRule()
        ))
    }
}