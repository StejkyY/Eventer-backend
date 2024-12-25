package eventer.project.plugins

import eventer.project.app.repositories.EventRepository
import eventer.project.app.repositories.SessionRepository
import eventer.project.app.repositories.UserRepository
import eventer.project.app.services.*
import eventer.project.app.web.controllers.*
import eventer.project.utils.EventStateScheduler
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            singleOf(::UserService)
            singleOf(::TypeService)
            singleOf(::SessionService)
            singleOf(::LocationService)
            singleOf(::EventService)

            singleOf(::UserRepository)
            singleOf(::SessionRepository)
            singleOf(::EventRepository)

            singleOf(::UserController)
            singleOf(::TypeController)
            singleOf(::SessionController)
            singleOf(::OauthController)
            singleOf(::LocationController)
            singleOf(::EventController)
            singleOf(::AuthController)
            singleOf(::EventStateScheduler)
        })
    }
}