package eventer.project.app.web.controllers

import com.google.inject.Inject
import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.services.TypeService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class TypeController {

   var typeService = TypeService()

    /**
     * Retrieves all session types in a list.
     */
    suspend fun getAllTypesList(call: ApplicationCall) {
        typeService.getTypesList().apply {
            call.respond(eventer.project.app.models.dto.TypesDTO(this))
        }
    }

    /**
     * Adds an event type.
     */
    suspend fun addType(call: ApplicationCall) {
        call.receive<eventer.project.app.models.dto.TypeDTO>().also { typeDTO ->
            typeService.addType(typeDTO.validate()).apply {
                call.respond(eventer.project.app.models.dto.TypeDTO(this))
            }
        }
    }


    suspend fun deleteType(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            typeService.deleteType(id.toInt()).apply {
                call.respond(mapOf("status" to "success", "message" to "Type deleted"))
            }
        } else throw MissingRequestBodyException("Invalid type id")
    }

}