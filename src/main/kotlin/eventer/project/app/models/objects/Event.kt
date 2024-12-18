package eventer.project.app.models.objects

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int? = null,
    val name: String? = null,
    val location: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val description: String? = null,
    val type: EventType? = null,
    val state: EventState? = null,
    var userEventRole: EventRole? = null,
    val privacy: EventPrivacy? = null
)