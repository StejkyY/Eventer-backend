package eventer.project.app.web.controllers

import com.google.inject.Inject
import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.models.objects.Session
import eventer.project.app.models.objects.User
import eventer.project.app.services.EventService
import eventer.project.app.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class EventController {

    var eventService = EventService()

    suspend fun getAllEventsList(call: ApplicationCall) {
        val user: User? = call.authentication.principal()
        eventService.getUserEventsList(user?.id!!).apply {
            call.respond(eventer.project.app.models.dto.EventsDTO(this))
        }
//        eventService.getAllEventsList(user?.id!!, null).apply {
//            call.respond(eventer.project.app.models.dto.EventsDTO(this))
//        }
    }

    suspend fun getUserEventsList(call: ApplicationCall) {
        val user: User? = call.authentication.principal()
        eventService.getUserEventsList(user?.id!!).apply {
            call.respond(eventer.project.app.models.dto.EventsDTO(this))
        }
    }

    suspend fun getEventRoles(call: ApplicationCall) {
        eventService.getEventRolesList().apply {
            call.respond(eventer.project.app.models.dto.EventRolesDTO(this))
        }
    }

    suspend fun getEventById(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            val user: User? = call.authentication.principal()
            var event = eventService.getEvent(id.toInt())
            event.userEventRole = eventService.getUserEventRole(user?.id!!, event.id!!)
            call.respond(eventer.project.app.models.dto.EventDTO(event))
        } else throw MissingRequestBodyException("Invalid event id")
    }

    suspend fun addEvent(call: ApplicationCall) {
        val user: User? = call.authentication.principal()
        call.receive<eventer.project.app.models.dto.EventDTO>().also { eventDTO ->
            eventService.addEvent(user?.id!!, eventDTO.validate()).apply {
                call.respond(eventer.project.app.models.dto.EventDTO(this))
            }
        }
    }

    suspend fun updateEvent(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            val user: User? = call.authentication.principal()
            call.receive<eventer.project.app.models.dto.EventDTO>().also { eventDTO ->
                eventService.updateEvent(user?.id!!, eventDTO.validate()).apply {
                    call.respond(eventer.project.app.models.dto.EventDTO(this))
                }
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }

    suspend fun deleteEvent(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            eventService.deleteEvent(id.toInt()).apply {
                call.respond(mapOf("status" to "success", "message" to "Event deleted"))
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }

    suspend fun updateEventAgendaSessions(call:ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            call.receive<eventer.project.app.models.dto.EventAgendaSessionsDTO>().also { eventAgendaDTO ->
                eventService.updateEventAgendaSessions(
                    eventAgendaDTO.addedSessions,
                    eventAgendaDTO.updatedSessions,
                    eventAgendaDTO.deletedSessions).apply {
                    call.respond(mapOf("status" to "success", "message" to "Agenda sessions saved"))
                }
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }
}