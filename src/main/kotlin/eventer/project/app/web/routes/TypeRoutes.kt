package eventer.project.app.web.routes

import eventer.project.app.web.controllers.TypeController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.typeRoutes() {

    val typeController = TypeController()

    routing {
        authenticate {
            get("/types") {
                typeController.getAllTypesList(call)
            }
            post("/types") {
                typeController.addType(call)
            }
            delete("/types/{id}") {
                typeController.deleteType(call)
            }
        }
    }
}