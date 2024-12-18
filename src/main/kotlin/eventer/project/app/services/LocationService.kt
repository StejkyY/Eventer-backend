package eventer.project.app.services

import com.google.inject.Inject
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.errorhandler.SomethingWentWrongException
import eventer.project.app.models.objects.Location
import eventer.project.app.repositories.SessionRepository

class LocationService {

    var sessionRepository = SessionRepository()

    suspend fun getEventLocations(eventId: Int): List<Location> {
        return sessionRepository.getEventLocationsList(eventId)
    }

    suspend fun addLocation(location: Location): Location {
        var locationAdded = sessionRepository.addLocation(location)
        locationAdded ?: throw SomethingWentWrongException("Error when saving location.")
        return locationAdded
    }

    suspend fun deleteLocation(id: Int): Boolean {
        val result = sessionRepository.deleteLocationById(id)
        if(!result) throw SomethingWentWrongException("Error when deleting location.")
        return result
    }
}