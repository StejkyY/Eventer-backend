package eventer.project.app.models.objects

import kotlinx.serialization.Serializable

@Serializable
enum class EventPrivacy {
    Private, Public
}