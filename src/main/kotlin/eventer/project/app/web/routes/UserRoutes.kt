package eventer.project.app.web.routes

import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.userRoutes() {

    val userController = UserController()

    routing {
        authenticate {
            get("/users/current") {
                userController.getCurrentUser(call)
            }
            put("/users/{id}") {
                userController.updateUserById(call)
            }
            delete("/users/current") {
                userController.deleteCurrentUser(call)
            }
            post("/users/current/change-password") {
                userController.changeCurrentUserPassword(call)
            }
        }
    }
}