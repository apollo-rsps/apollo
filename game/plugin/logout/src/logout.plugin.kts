val LOGOUT_BUTTON_ID = 2458

on_button(LOGOUT_BUTTON_ID)
    .where { widgetId == LOGOUT_BUTTON_ID }
    .then { it.logout() }