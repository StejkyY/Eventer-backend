package eventer.project.app.models.objects

import kotlinx.serialization.Serializable

@Serializable
data class Type(
    val id: Int? = null,
    val name: String? = null
)