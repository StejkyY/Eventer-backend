package eventer.project.app.web.controllers

import com.google.inject.Inject
import eventer.project.app.models.dto.UserDTO
import eventer.project.app.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json

class AuthController {

    var userService = UserService()

    suspend fun userLogin(call: ApplicationCall) {
        call.receive<UserDTO>().apply {
            userService.authenticate(this.validateLogin()).apply {
                call.respond(UserDTO(this))
            }
        }
    }

    suspend fun userLogout(call: ApplicationCall) {
        call.respondRedirect("/")
    }

    suspend fun userRegister(call: ApplicationCall) {
        call.receive<eventer.project.app.models.dto.UserDTO>().also { userDTO ->
            userService.addUser(userDTO.validate()).apply {
                call.respond(UserDTO(this))
            }
        }
    }
}