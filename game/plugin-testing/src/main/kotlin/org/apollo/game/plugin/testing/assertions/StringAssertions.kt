package org.apollo.game.plugin.testing.assertions

import io.mockk.MockKMatcherScope

fun MockKMatcherScope.contains(search: String) = match<String> { it.contains(search) }
fun MockKMatcherScope.startsWith(search: String) = match<String> { it.startsWith(search) }
fun MockKMatcherScope.endsWith(search: String) = match<String> { it.endsWith(search) }