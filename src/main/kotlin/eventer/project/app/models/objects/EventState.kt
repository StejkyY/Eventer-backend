package eventer.project.app.models.objects

import kotlinx.serialization.Serializable

@Serializable
enum class EventState {
    Upcoming, InProgress, Elapsed
}