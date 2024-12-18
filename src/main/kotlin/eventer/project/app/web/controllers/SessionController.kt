package eventer.project.app.web.controllers

import com.google.inject.Inject
import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.services.SessionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


class SessionController {

   var sessionService = SessionService()

    suspend fun getEventSessions(call: ApplicationCall) {
        val id = call.parameters["id"]
        if (id != null) {
            sessionService.getEventSessions(id.toInt()).apply {
                call.respond(eventer.project.app.models.dto.SessionsDTO(this))
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }

    suspend fun addSession(call: ApplicationCall) {
        call.receive<eventer.project.app.models.dto.SessionDTO>().also { sessionDTO ->
            sessionService.addSession(sessionDTO.validate()).apply {
                call.respond(eventer.project.app.models.dto.SessionDTO(this))
            }
        }
    }

    suspend fun updateSession(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            call.receive<eventer.project.app.models.dto.SessionDTO>().also { sessionDTO ->
                sessionService.updateSession(sessionDTO.validate()).apply {
                    call.respond(eventer.project.app.models.dto.SessionDTO(this))
                }
            }
        } else throw MissingRequestBodyException("Invalid session id")
    }

    suspend fun deleteSession(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            sessionService.deleteSession(id.toInt()).apply {
                call.respond(mapOf("status" to "success", "message" to "Session deleted"))
            }
        } else throw MissingRequestBodyException("Invalid session id")
    }
}