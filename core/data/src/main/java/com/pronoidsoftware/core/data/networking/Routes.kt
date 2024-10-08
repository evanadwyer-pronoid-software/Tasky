package com.pronoidsoftware.core.data.networking

object AuthRoutes {
    const val REGISTER = "/register"
    const val LOGIN = "/login"
    const val ACCESS_TOKEN = "/accessToken"
    const val LOGOUT = "/logout"
}

object AgendaRoutes {
    const val REMINDER = "/reminder"
    const val TASK = "/task"
    const val EVENT = "/event"
    const val FULL_AGENDA = "/fullAgenda" // for syncing
}

object AttendeeRoutes {
    const val ATTENDEE = "/attendee"
}
