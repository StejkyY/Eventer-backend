package eventer.project.app.models.dto

import eventer.project.app.models.objects.EventRole
import kotlinx.serialization.Serializable

@Serializable
data class EventRoleDTO(val eventRole: EventRole?)

@Serializable
data class EventRolesDTO(val eventRoles: List<EventRole>)