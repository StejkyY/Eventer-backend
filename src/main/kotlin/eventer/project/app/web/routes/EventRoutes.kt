package eventer.project.app.web.routes

import eventer.project.app.web.controllers.EventController
import eventer.project.app.web.controllers.LocationController
import eventer.project.app.web.controllers.SessionController
import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.eventRoutes() {

    val eventController by inject<EventController>()
    val sessionController by inject<SessionController>()
    val locationController by inject<LocationController>()

    routing {
        authenticate {
            get("/events") {
                eventController.getUserEventsList(call)
            }
            get("/events/{id}") {
                eventController.getEventById(call)
            }
            get("/events/{id}/sessions") {
                sessionController.getEventSessions(call)
            }
            get("/events/{id}/locations") {
                locationController.getEventLocations(call)
            }
            get("/events/roles") {
                eventController.getEventRoles(call)
            }
            post("/events") {
                eventController.addEvent(call)
            }
            post("/events/{id}/sessions") {
                eventController.updateEventAgendaSessions(call)
            }
            put("/events/{id}") {
                eventController.updateEvent(call)
            }
            delete("/events/{id}") {
                eventController.deleteEvent(call)
            }
        }
    }
}