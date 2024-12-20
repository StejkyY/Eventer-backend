package eventer.project.app.web.routes

import eventer.project.app.web.controllers.OauthController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.oauthRoutes() {

    val oauthController = OauthController()

    routing {
        authenticate("auth-oauth-google") {
            get("/oauth/google/login") {
                // Redirects to 'authorizeUrl' automatically
            }
            get("/oauth/google/callback") {
                oauthController.oauthGoogleCallback(call)
            }
        }
        authenticate {
            get("/oauth/google/token-refresh") {
                oauthController.googleAccessTokenRefresh(call)
            }
        }

        authenticate("auth-oauth-microsoft") {
            get("/oauth/microsoft/login") {
                // Redirects to 'authorizeUrl' automatically
            }
            get("/oauth/microsoft/callback") {
                oauthController.oauthMicrosoftCallback(call)
            }
        }
        authenticate {
            get("/oauth/microsoft/token-refresh") {
                oauthController.microsoftAccessTokenRefresh(call)
            }
        }
    }
}