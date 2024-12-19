package eventer.project.app.services

import com.google.inject.Inject
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.errorhandler.SomethingWentWrongException
import eventer.project.app.models.objects.Event
import eventer.project.app.models.objects.EventMember
import eventer.project.app.models.objects.EventRole
import eventer.project.app.models.objects.Session
import eventer.project.app.repositories.EventRepository
import eventer.project.app.repositories.SessionRepository

class EventService() {

    var eventRepository = EventRepository()
    var sessionRepository = SessionRepository()

    suspend fun getAllEventsList(userId: Int, search: String?): List<Event> {
        val eventList = eventRepository.getEventsList()
        for(e in eventList) {
            try {
                e.userEventRole = getUserEventRole(userId, e.id!!)
            } catch (e: NotFoundException) {
                continue
            }
        }
        return eventList
    }

    suspend fun getAllEventsList(): List<Event> {
        val eventList = eventRepository.getEventsList()
        return eventList
    }

    suspend fun getUserEventsList(userId: Int): List<Event> {
        return eventRepository.getUserEventsList(userId)
    }

    suspend fun getEventRolesList(): List<EventRole> {
        return eventRepository.getEventRolesList()
    }

    suspend fun getEvent(eventId: Int): Event {
        val event = eventRepository.getEventById(eventId)
        event ?: throw NotFoundException("Event not found.")
        return event
    }

    suspend fun addEvent(userId: Int, event: Event): Event {
        println(userId)
        println(event)
        var eventAdded = eventRepository.addEvent(userId, event)
        eventAdded ?: throw SomethingWentWrongException("Error when saving event.")
        return eventAdded
    }

    suspend fun updateEvent(userId: Int? = null, event: Event): Event {
        val receivedEvent = eventRepository.updateEvent(userId, event)
        receivedEvent ?: throw SomethingWentWrongException("Error when updating event.")
        return receivedEvent
    }

    suspend fun deleteEvent(id: Int): Boolean {
        val result = eventRepository.deleteEventById(id)
        if(!result) throw SomethingWentWrongException("Error when deleting event.")
        return result
    }

    suspend fun getUserEventRole(userId: Int, eventId: Int): EventRole? {
        return eventRepository.getUserRoleInEvent(userId, eventId)
    }

    suspend fun updateEventAgendaSessions(addedSessions: List<Session>,
                                          updatedSessions: List<Session>,
                                          deletedSessions: List<Session>) {
        sessionRepository.updateEventSessionAgenda(addedSessions, updatedSessions, deletedSessions)
    }
}