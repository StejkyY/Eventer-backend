package eventer.project.app.models.dto

import eventer.project.app.models.objects.Session
import kotlinx.serialization.Serializable

@Serializable
data class EventAgendaSessionsDTO(val addedSessions: List<Session>,
                                  val updatedSessions: List<Session>,
                                  val deletedSessions: List<Session>)
