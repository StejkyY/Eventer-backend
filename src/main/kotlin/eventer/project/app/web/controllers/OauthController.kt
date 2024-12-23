package eventer.project.app.web.controllers

import eventer.project.plugins.applicationHttpClient
import eventer.project.plugins.redirects
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class OauthController {

    suspend fun oauthGoogleCallback(call: ApplicationCall) {
        val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
        currentPrincipal?.let { principal ->
            principal.state?.let { state ->
                redirects[state]?.let { redirect ->
                    call.response.cookies.append(
                        Cookie(
                            name = "GOOGLE_REFRESH_TOKEN",
                            value = principal.refreshToken.toString(),
                            httpOnly = true,
                            secure = true,
                            path = "/",
                            maxAge = 60 * 60 * 24 * 7
                        )
                    )
                    call.respondRedirect("$redirect#access_token=${principal.accessToken}&")
                }
            }
        }
    }

    suspend fun receiveNewAccessToken(call: ApplicationCall,
                                      refreshToken: String,
                                      tokenUrl: String,
                                      clientId: String,
                                      clientSecret: String) {
        try {
            val response: Map<String, Any> = applicationHttpClient.post(tokenUrl) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    listOf(
                        "client_id" to clientId,
                        "client_secret" to clientSecret,
                        "refresh_token" to refreshToken,
                        "grant_type" to "refresh_token"
                    ).formUrlEncode()
                )
            }.body()
            val newAccessToken = response["access_token"] as? String
            if (newAccessToken != null) {
                call.respond(HttpStatusCode.OK, mapOf("access_token" to newAccessToken))
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }

    suspend fun googleAccessTokenRefresh(call: ApplicationCall) {
        val refreshToken = call.request.cookies["GOOGLE_REFRESH_TOKEN"]
            ?: return call.respond(HttpStatusCode.Unauthorized, "Refresh token not found")

        val tokenUrl = "https://accounts.google.com/o/oauth2/token"
        val clientId = System.getenv("GOOGLE_CLIENT_ID")
        val clientSecret = System.getenv("GOOGLE_CLIENT_SECRET")

        receiveNewAccessToken(call, refreshToken, tokenUrl, clientId, clientSecret)
    }

    suspend fun microsoftAccessTokenRefresh(call: ApplicationCall) {
        val refreshToken = call.request.cookies["MICROSOFT_REFRESH_TOKEN"]
            ?: return call.respond(HttpStatusCode.Unauthorized, "Refresh token not found")

        val tokenUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/token"
        val clientId = System.getenv("MICROSOFT_CLIENT_ID")
        val clientSecret = System.getenv("MICROSOFT_CLIENT_SECRET")

        receiveNewAccessToken(call, refreshToken, tokenUrl, clientId, clientSecret)
    }

    suspend fun oauthMicrosoftCallback(call: ApplicationCall) {
        val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()

        currentPrincipal?.let { principal ->
            principal.state?.let { state ->
                redirects[state]?.let { redirect ->
                    call.response.cookies.append(
                        Cookie(
                            name = "MICROSOFT_REFRESH_TOKEN",
                            value = principal.refreshToken.toString(),
                            httpOnly = true,
                            secure = true,
                            path = "/",
                            maxAge = 60 * 60 * 24 * 90
                        )
                    )
                    call.respondRedirect("$redirect#access_token=${principal.accessToken}")
                }
            }
        }
    }
}