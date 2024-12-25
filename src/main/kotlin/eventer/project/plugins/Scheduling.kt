package eventer.project.plugins

import eventer.project.utils.EventStateScheduler
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureScheduling() {

    val eventStateScheduler by inject<EventStateScheduler>()

    eventStateScheduler.startScheduler()
}