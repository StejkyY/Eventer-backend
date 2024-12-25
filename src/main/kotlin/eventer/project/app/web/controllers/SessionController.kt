package eventer.project.app.web.controllers

import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.services.SessionService
import eventer.project.app.services.TypeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*


class SessionController(private val sessionService: SessionService) {

    /**
     * Retrieves all sessions for an event by event ID in the request.
     */
    suspend fun getEventSessions(call: ApplicationCall) {
        val id = call.parameters["id"]
        if (id != null) {
            sessionService.getEventSessions(id.toInt()).apply {
                call.respond(eventer.project.app.models.dto.SessionsDTO(this))
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }

    /**
     * Adds an event session.
     */
    suspend fun addSession(call: ApplicationCall) {
        call.receive<eventer.project.app.models.dto.SessionDTO>().also { sessionDTO ->
            sessionService.addSession(sessionDTO.validate()).apply {
                call.respond(eventer.project.app.models.dto.SessionDTO(this))
            }
        }
    }

    /**
     * Updates an event session by ID in the request.
     */
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

    /**
     * Deletes session by ID from the request.
     */
    suspend fun deleteSession(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            sessionService.deleteSession(id.toInt()).apply {
                call.respond(mapOf("status" to "success", "message" to "Session deleted"))
            }
        } else throw MissingRequestBodyException("Invalid session id")
    }
}