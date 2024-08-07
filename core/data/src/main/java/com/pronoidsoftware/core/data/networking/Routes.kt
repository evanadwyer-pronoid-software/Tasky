package com.pronoidsoftware.core.data.networking

object AuthRoutes {
    const val REGISTER = "/register"
    const val LOGIN = "/login"
    const val ACCESS_TOKEN = "/accessToken"
}

object AgendaRoutes {
    const val REMINDER = "/reminder"
    const val TASK = "/task"
    const val FULL_AGENDA = "/fullAgenda" // for syncing
}
