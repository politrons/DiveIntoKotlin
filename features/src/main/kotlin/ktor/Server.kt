package main.kotlin.ktor

import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.*


fun main() {
    val server: NettyApplicationEngine = embeddedServer(Netty, port = 8080) {
        errorHandlerHttpServer()
        routing {
            get("/types") {
                val params: Parameters = call.request.queryParameters
                val param: String? = params.get("foo")
                val defferResponse: Deferred<Response> = async {
                    //We place all the async logic of application here.
                    Response("Ktor Async server with param $param")
                }
                call.responseAsync(defferResponse)
            }
        }
    }
    server.start(wait = true)
}

private val gson = Gson()

/**
 * Using extension function we can implement an [ApplicationCall] function that allow us render whatever response from
 * Ktor in async way since the function is marked as suspend.
 */
private suspend fun ApplicationCall.responseAsync(defferResponse: Deferred<Response>) {
    respond(HttpStatusCode.OK, gson.toJson(defferResponse.await()))
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