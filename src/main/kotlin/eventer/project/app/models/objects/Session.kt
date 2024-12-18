package eventer.project.app.models.objects

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable


@Serializable
data class Session(
    val id: Int? = null,
    val name: String? = null,
    val date: LocalDate? = null,
    val startTime: LocalTime? = null,
    val duration: Int? = null,
    val description: String? = null,
    val dayOrder: Int? = null,
    val type: Type? = null,
    val location: Location? = null,
    val eventId: Int? = null
)