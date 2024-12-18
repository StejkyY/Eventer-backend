package eventer.project.app.web.routes

import eventer.project.app.web.controllers.SessionController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.sessionRoutes() {

    val sessionController = SessionController()

    routing {
        authenticate {
            post("/sessions") {
                sessionController.addSession(call)
            }
            put("/sessions/{id}") {
                sessionController.updateSession(call)
            }
            delete("/sessions/{id}") {
                sessionController.deleteSession(call)
            }
        }
    }
}