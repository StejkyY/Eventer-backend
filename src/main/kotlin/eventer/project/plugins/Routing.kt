package eventer.project.plugins

import eventer.project.app.errorhandler.AuthorizationException
import eventer.project.app.web.routes.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.valiktor.ConstraintViolationException

fun Application.configureRouting() {
    install(ContentNegotiation){
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
    install(StatusPages) {
        exception<ConstraintViolationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                cause.constraintViolations.map { "${it.property}: ${it.constraint.name}" }.toString()
            )
        }
        exception<RuntimeException> { call, cause ->
            println(cause.message.toString())
            call.respond(
                HttpStatusCode.InternalServerError, cause.message.toString()
            )
        }
    }
    install(CORS) {
        anyHost()
        allowCredentials = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
    }

    routing {
        authRoutes()
        oauthRoutes()
        eventRoutes()
        userRoutes()
        sessionRoutes()
        typeRoutes()
        locationRoutes()
    }
}
