package eventer.project.app.models.dto

import eventer.project.app.errorhandler.NotFoundException
import eventer.project.app.models.objects.Session
import kotlinx.serialization.Serializable
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

@Serializable
data class SessionDTO(val session: Session?) {
    fun validate(): Session {
        if(session != null) {
            return org.valiktor.validate(session) {
                validate(Session::name).isNotBlank()
                validate(Session::date).isNotNull()
                validate(Session::startTime).isNotNull()
                validate(Session::duration).isNotNull()
                validate(Session::dayOrder).isGreaterThan(0)
                validate(Session::type).isNotNull()
                validate(Session::location).isNotNull()
                validate(Session::eventId).isNotNull()
            }
        } else throw NotFoundException("Session not received.")
    }
}

@Serializable
data class SessionsDTO(val sessions: List<Session>)
