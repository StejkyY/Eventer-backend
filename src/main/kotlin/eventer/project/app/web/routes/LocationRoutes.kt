package eventer.project.app.web.routes

import eventer.project.app.web.controllers.LocationController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.locationRoutes() {

    val locationController = LocationController()

    routing {
        authenticate {
            post("/locations") {
                locationController.addLocation(call)
            }
            delete("/locations/{id}") {
                locationController.deleteLocation(call)
            }
        }
    }
}