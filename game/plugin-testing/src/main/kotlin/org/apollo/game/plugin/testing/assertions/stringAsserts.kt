package org.apollo.game.plugin.testing.assertions

import io.mockk.MockKMatcherScope

inline fun MockKMatcherScope.contains(search: String) = match<String> { it.contains(search) }
inline fun MockKMatcherScope.startsWith(search: String) = match<String> { it.startsWith(search) }
inline fun MockKMatcherScope.endsWith(search: String) = match<String> { it.endsWith(search) }