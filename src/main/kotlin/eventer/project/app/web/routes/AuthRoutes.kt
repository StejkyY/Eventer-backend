package eventer.project.app.web.routes

import eventer.project.app.web.controllers.AuthController
import eventer.project.app.web.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authRoutes() {

    val authController by inject<AuthController>()

    routing {

        post("/auth/login") {
            authController.userLogin(call)
        }
        authenticate {
            post("/auth/logout") {
                authController.userLogout(call)
            }
        }

        post ("/auth/register"){
            authController.userRegister(call)
        }
    }
}