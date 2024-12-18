package eventer.project

import eventer.project.plugins.configureAuthentication
import eventer.project.plugins.configureLogging
import io.ktor.server.application.*
import eventer.project.plugins.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Db.init(environment.config)
    configureAuthentication()
    configureLogging()
    configureRouting()
}
