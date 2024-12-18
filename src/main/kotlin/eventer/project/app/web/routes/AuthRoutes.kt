package eventer.project.app.web.routes

import eventer.project.app.web.controllers.AuthController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.authRoutes() {

    val authController = AuthController()

    routing {

        post("auth/login") {
            authController.userLogin(call)
        }
        authenticate {
            post("auth/logout") {
                authController.userLogout(call)
            }
        }

        post ("auth/register"){
            authController.userRegister(call)
        }
    }
}