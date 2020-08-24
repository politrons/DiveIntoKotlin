package main.kotlin.ktor

import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine

fun main() {
    val gson = Gson()
    val server: NettyApplicationEngine = embeddedServer(Netty, port = 8080) {
        errorHandlerHttpServer()
        routing {
            get("/text") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/json") {
                call.respondText(
                    """
                    {
                        response:"Hello json response"
                    }
                """.trimIndent(), ContentType.Application.Json
                )
            }
            get("/types") {
                call.respond(HttpStatusCode.OK, gson.toJson(Response("Hello world with types")))
            }
        }
    }
    server.start(wait = true)
}

/**
 * Using feature [install(StatusPages)] we can wrap all possible exceptions that it might happens in the Http Server
 * in case is not a controlled Throwable.
 * Here we control all possible Throwable and then we render the error description and error code
 */
private fun Application.errorHandlerHttpServer() {
    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
}

data class Response(val message: String)