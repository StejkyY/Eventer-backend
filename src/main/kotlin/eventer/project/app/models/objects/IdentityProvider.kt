package eventer.project.app.models.objects

import kotlinx.serialization.Serializable

@Serializable
enum class IdentityProvider {
    Local, Google, Microsoft
}