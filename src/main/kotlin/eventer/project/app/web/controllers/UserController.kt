package eventer.project.app.web.controllers

import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.models.dto.UserDTO
import eventer.project.app.models.objects.User
import eventer.project.app.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserController(private val userService: UserService) {

    /**
     * Retrieves the currently authenticated user.
     */
    suspend fun getCurrentUser(call: ApplicationCall) {
        call.respond(eventer.project.app.models.dto.UserDTO(call.authentication.principal()))
    }

    /**
     * Retrieves user by given email address.
     */
    suspend fun getUserByEmail(email: String): User {
        return userService.getUserByEmail(email)
    }

    /**
     * Updates user by ID in the request.
     */
    suspend fun updateUserById(call: ApplicationCall) {
            val id = call.parameters["id"]
            if(id != null) {
                call.receive<eventer.project.app.models.dto.UserDTO>().also { userDTO ->
                    userService.updateUser(userDTO.validate()).apply {
                        call.respond(eventer.project.app.models.dto.UserDTO(this))
                    }
                }
            } else throw MissingRequestBodyException("Invalid user id")
    }

    /**
     * Deletes the currently authenticated user.
     */
    suspend fun deleteCurrentUser(call: ApplicationCall) {
        val user = UserDTO(call.authentication.principal())
        userService.deleteUserById(user.user?.id!!). apply {
            call.respond(mapOf("status" to "success", "message" to "User deleted"))
        }
    }

    /**
     * Changes the currently authenticated user's password.
     */
    suspend fun changeCurrentUserPassword(call: ApplicationCall) {
        val user = UserDTO(call.authentication.principal())
            call.receive<eventer.project.app.models.dto.UserPasswordDTO>().also { passwordDTO ->
                userService.changeUserPasswordById(user.user?.id!!, passwordDTO.validate()).apply {
                    call.respond(mapOf("status" to "success", "message" to "User password changed"))
                }
            }
    }
}