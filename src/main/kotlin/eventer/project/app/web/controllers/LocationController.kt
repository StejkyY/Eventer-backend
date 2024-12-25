package eventer.project.app.web.controllers

import eventer.project.app.errorhandler.MissingRequestBodyException
import eventer.project.app.services.LocationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class LocationController(private val locationService: LocationService) {

    /**
     * Retrieves all session locations for an event by event ID given in request.
     */
    suspend fun getEventLocations(call: ApplicationCall) {
        val id = call.parameters["id"]
        if (id != null) {
            locationService.getEventLocations(id.toInt()).apply {
                call.respond(eventer.project.app.models.dto.LocationsDTO(this))
            }
        } else throw MissingRequestBodyException("Invalid event id")
    }

    /**
     * Adds sessions location.
     */
    suspend fun addLocation(call: ApplicationCall) {
        call.receive<eventer.project.app.models.dto.LocationDTO>().also { locationDTO ->
            locationService.addLocation(locationDTO.validate()).apply {
                call.respond(eventer.project.app.models.dto.LocationDTO(this))
            }
        }
    }

    /**
     * Deletes sessions location by ID from the request.
     */
    suspend fun deleteLocation(call: ApplicationCall) {
        val id = call.parameters["id"]
        if(id != null) {
            locationService.deleteLocation(id.toInt()).apply {
                call.respond(mapOf("status" to "success", "message" to "Location deleted"))
            }
        } else throw MissingRequestBodyException("Invalid type id")
    }
}