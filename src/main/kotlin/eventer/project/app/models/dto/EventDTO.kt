package eventer.project.app.models.dto

import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.Event
import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

@Serializable
data class EventDTO(val event: Event? = null) {
    fun validate(): Event {
        if(event != null) {
            return org.valiktor.validate(event) {
                validate(Event::name).isNotBlank()
                validate(Event::location).isNotNull()
                validate(Event::startDate).isNotNull()
                validate(Event::endDate).isNotNull()
                validate(Event::type).isNotNull()
                validate(Event::state).isNotNull()
                validate(Event::privacy).isNotNull()
            }
        } else throw NotFoundException("Event not received.")
    }
}

@Serializable
data class EventsDTO(val events: List<Event>)
