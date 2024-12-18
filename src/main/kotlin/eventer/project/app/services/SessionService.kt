package eventer.project.app.services

import com.google.inject.Inject
import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.errorhandler.SomethingWentWrongException
import eventer.project.app.models.objects.Session
import eventer.project.app.repositories.SessionRepository

class SessionService {

    var sessionRepository = SessionRepository()

    suspend fun getEventSessions(eventId: Int): List<Session> {
        return sessionRepository.getSessionListByEventId(eventId)
    }

    suspend fun addSession(session: Session): Session {
        var sessionAdded = sessionRepository.addSession(session)
        sessionAdded ?: throw SomethingWentWrongException("Error when saving session.")
        return sessionAdded
    }

    suspend fun updateSession(session: Session): Session {
        val receivedSession = sessionRepository.updateSession(session)
        receivedSession ?: throw SomethingWentWrongException("Session when updating event.")
        return receivedSession
    }

    suspend fun deleteSession(id: Int): Boolean {
        val result = sessionRepository.deleteSessionById(id)
        if(!result) throw SomethingWentWrongException("Error when deleting session.")
        return result
    }
}