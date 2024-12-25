package eventer.project.app.web.routes

import eventer.project.app.web.controllers.LocationController
import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.locationRoutes() {

    val locationController by inject<LocationController>()

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