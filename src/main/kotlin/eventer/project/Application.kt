package eventer.project

import eventer.project.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Db.init(environment.config)
    configureAuthentication(config = environment.config)
    configureLogging()
    configureDependencyInjection()
    configureRouting()
    configureScheduling()
}
