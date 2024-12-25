package eventer.project.app.web.routes

import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.userRoutes() {

    val userController by inject<UserController>()

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