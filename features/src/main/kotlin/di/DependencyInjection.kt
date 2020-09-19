package main.kotlin.di

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

/**
 * Service to be injected
 * ------------------------
 * Service and repository example to show how they can be injected with Koin
 */
interface CalendarService {
    fun dayOfYear(): String
}

class CalendarServiceImpl(private val repository: CalendarRepository) : CalendarService {
    override fun dayOfYear() = "Hi today is ${repository.calcDayOfYear()} !"
}

class CalendarRepository {
    fun calcDayOfYear(): String = LocalDateTime.now().toString()
}

/**
 * Declaration of DI module
 * -------------------------
 * We declare inside the module all dependencies.
 * If we define an instance as single without expose an interface <I> means is an internal
 * dependency to be used for another that it will need for his construction.
 * it can be reference using [get()] from another dependency declaration, that has
 * also that dependency.
 * In case we define a single<I> I is the dependency that can be injected outside.
 */
val calendarModule = module {
    single<CalendarService> { CalendarServiceImpl(get()) }
    single { CalendarRepository() }
}

/**
 * Program with Dependency injection
 * ----------------------------------
 * To use Koin in Ktor we need to install [Koin] feature, and then use [module] passing
 * the dependency declaration done before [calendarModule].
 *
 * Then we define a lazy reference of the dependency service, using [by inject()]
 * Once we have the dependency it can be used in our program.
 * Important to mention, this DI is done Ahead in time and not Just in time with reflection.
 */
fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

fun Application.main() {
    install(Koin) {
        modules(calendarModule)
    }
    val service: CalendarService by inject()
    routing {
        get("/dayOfYear") {
            call.respondText(service.dayOfYear())
        }
    }
}