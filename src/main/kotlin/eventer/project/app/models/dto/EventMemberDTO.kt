package eventer.project.app.models.dto

import eventer.project.app.models.objects.EventMember
import kotlinx.serialization.Serializable

@Serializable
data class EventMemberDTO(val eventMember: EventMember?)
