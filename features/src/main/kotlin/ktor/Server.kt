package main.kotlin.ktor

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.leftIor
import com.google.gson.Gson
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.Headers
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

/**
 * Ktor server it work with multiple transport layer engine like Netty, Tomcat, Jetty, Servlet.
 * Here we have the dependency for Netty, so we use in the [embeddedServer] Netty passing also a port.
 * That create a [NettyApplicationEngine] which is a subclass of [BaseApplicationEngine]
 *
 * Inside the [Application] we can have:
 *
 * * [Features] which has multiple implementations
 *      and the possibility to create your own feature. In this case we have
 * * * [ErrorHandler] to control all exceptions in the Http server and return always a Custom error message.
 * * * [FreeMarker] to render the ftl file as html file
 *
 * * [Routing] where we can use the DSL to define our Http protocol for [get, post, put, delete]
 *      [call] is the session with all the [request, response, context]
 *      Into the [request] attribute inside the [call] we can get the [queryParams, uri, headers]
 */
fun main() {
    val server: NettyApplicationEngine = embeddedServer(Netty, port = 8080) {
        installErrorHandlerFeature()
        installFreeMarkerFeature()
        routing {
            get("/types") {
                val headers: Headers = call.request.headers
                val params: Parameters = call.request.queryParameters
                val maybeParam: Option<String?> = Option(params["foo"])
                val defferResponse: Deferred<Response> = async {
                    //We place all the async logic of application here.
                    Response("Ktor Async server with param ${maybeParam.getOrElse { "Empty" }}")
                }
                call.responseJson(defferResponse)
            }
            get("/template") {
                val params: Parameters = call.request.queryParameters
                val username: String? = params["user"]
                val email: String? = params["email"]
                val feature: String? = params["feature"]
                val defferResponse: Deferred<Pair<User, Feature>> = async {
                    User(username, email) to Feature(feature)
                }
                call.responseTemplate(defferResponse)
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
private suspend fun <T> ApplicationCall.responseJson(defferResponse: Deferred<T>) {
    respond(HttpStatusCode.OK, gson.toJson(defferResponse.await()))
}

/**
 * to render and bind the data domain and the template we use [FreeMarkerContent] where as first argument,
 * we specify the name of the template, and the second how we're going to bind the alias used in the template,
 * with our domain model
 */
private suspend fun <A, B> ApplicationCall.responseTemplate(defferResponse: Deferred<Pair<A, B>>) {
    respond(
        FreeMarkerContent(
            "user.ftl",
            mapOf(
                "user" to defferResponse.await().first,
                "feature" to defferResponse.await().second
            )
        )
    )
}

/**
 * We install the [Freemarker] feature where we need to specify the folder in the resources where the templates of freemarker
 * are. We just pass a classloader to load the templates and the folder
 */
private fun Application.installFreeMarkerFeature() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
}


/**
 * Using feature [install(StatusPages)] we can wrap all possible exceptions that it might happens in the Http Server
 * in case is not a controlled Throwable.
 * Here we control all possible Throwable and then we render the error description and error code
 */
private fun Application.installErrorHandlerFeature() {
    install(StatusPages) {
        exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }
    }
}

/**
 * Domain
 * ------
 */
data class User(val name: String?, val email: String?)

data class Feature(val name: String?)

data class Response(val message: String)