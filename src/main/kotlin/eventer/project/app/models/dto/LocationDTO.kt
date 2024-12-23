package eventer.project.app.models.dto

import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.Location
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

@Serializable
data class LocationDTO(val location: Location?) {
    fun validate(): Location {
        if(location != null) {
            return org.valiktor.validate(location) {
                validate(Location::name).isNotBlank().hasSize(1, 50)
                validate(Location::eventId).isNotNull()
            }
        } else throw NotFoundException("Location not received.")
    }
}

@Serializable
data class LocationsDTO(val locations: List<Location>)
