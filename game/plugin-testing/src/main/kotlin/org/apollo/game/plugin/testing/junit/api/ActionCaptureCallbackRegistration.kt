package org.apollo.game.plugin.testing.junit.api

typealias Function = () -> Unit

class ActionCaptureCallbackRegistration(var function: Function? = null, var description: String? = null)