package eventer.project.plugins

import eventer.project.utils.EventStateScheduler
import io.ktor.server.application.*

fun Application.configureScheduling() {
    val eventStateScheduler = EventStateScheduler()
    eventStateScheduler.startScheduler()
}