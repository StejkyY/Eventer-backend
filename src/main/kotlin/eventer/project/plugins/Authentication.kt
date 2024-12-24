package eventer.project.plugins

import com.auth0.jwt.JWT
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import eventer.project.utils.Cipher
import eventer.project.utils.JwtProvider
import eventer.project.utils.JwtProvider.issuer
import eventer.project.app.web.controllers.UserController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import kotlinx.serialization.json.Json

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}

val redirects = mutableMapOf<String, String>()

fun Application.configureAuthentication(httpClient: HttpClient = applicationHttpClient, config: ApplicationConfig) {

    val userController = UserController()

    install(Authentication) {
        jwt {
            verifier(JWT
                .require(Cipher.algorithm)
                .withIssuer(issuer)
                .build())

            validate { credential ->
                if (credential.payload.audience.contains(JwtProvider.audience)) {
                    userController.getUserByEmail(credential.payload.subject)
                } else null
            }
        }
        oauth("auth-oauth-google") {
            urlProvider = { config.propertyOrNull("oauth.google.callbackUrl")?.getString() ?: "http://localhost:8080/oauth/google/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/calendar.events.owned"),
                    extraAuthParameters = listOf(
                        "access_type" to "offline",
                        "prompt" to "consent"
                    ),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    }
                )
            }
            client = httpClient
        }
        oauth("auth-oauth-microsoft") {
            urlProvider = { config.propertyOrNull("oauth.microsoft.callbackUrl")?.getString() ?: "http://localhost:8080/oauth/microsoft/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "microsoft",
                    authorizeUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize",
                    accessTokenUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("MICROSOFT_CLIENT_ID"),
                    clientSecret = System.getenv("MICROSOFT_CLIENT_SECRET"),
                    defaultScopes = listOf(
                        "https://graph.microsoft.com/Calendars.ReadWrite",
                        "https://graph.microsoft.com/User.Read",
                        "offline_access"
                    ),
                    extraAuthParameters = listOf(
                        "response_mode" to "query",
                        "prompt" to "select_account"
                    ),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    }
                )
            }
            client = httpClient
        }
    }
}