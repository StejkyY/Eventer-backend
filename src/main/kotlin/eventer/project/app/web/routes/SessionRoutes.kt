package eventer.project.app.web.routes

import eventer.project.app.web.controllers.SessionController
import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.sessionRoutes() {

    val sessionController by inject<SessionController>()

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