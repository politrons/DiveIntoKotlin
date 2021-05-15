package com.politrons

import com.apurebase.kgraphql.GraphQL
import io.ktor.application.*

/**
 *
 * To make a request to the service, from graphql interface [http://localhost:1981/graphql] send query like
 *
 * {
music(band: "Depeche mode")
}
object:
{
object {a}
}

 */
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    /**
     * We install the Ktor module [GraphQL] giving us as part of the DSL the
     * possibility to create the Schema programmatically
     */
    install(GraphQL) {
        playground = true

        /**
         * We create programmatically the schema.
         */
        schema {

            type<AAA> {
                description = "blablabla"
            }

            /**
             * [Configure] method allows you customize schema behaviour.
             * By default the GraphQL service is running async using [CoroutineDispatcher=Default]
             */
            configure {
                useDefaultPrettyPrinter = true
                timeout = 5000
            }

            /**
             * Here we define the API where clients can search using queries for a specific data.
             */
            query("music") {
                resolver { band: String ->
                    println("Searching for band:$band Thread:${Thread.currentThread().name}")
                    when (band) {
                        "Depeche mode", "depeche mode" -> depecheModeAlbums
                        "Counting Crows", "counting crows" -> countingCrowsAlbums
                        else -> listOf("You need to specify a band.")
                    }
                }
            }

            query("movies") {
                resolver { genre: String ->
                    println("Searching for band:$genre Thread:${Thread.currentThread().name}")
                    when (genre) {
                        "Action", "action" -> actionMovies
                        "Sci-fi", "sci-fi" -> sciFi
                        else -> listOf("You need to specify a band.")
                    }
                }
            }

            query("object") {
                resolver { -> AAA("Hello object") }
            }

            query("services") {
                resolver { -> listOf("music", "movies") }
            }
        }
    }
}


data class AAA(val a: String)

/**
 * Music
 * ------
 */
val countingCrowsAlbums =
    listOf("August and everything after", "This desert life", "Hard Candy", "Recovering the Satellites")
val depecheModeAlbums = listOf("Sound of the Universe", "Delta Machine", "Music For The Masses", "Exciter")

/**
 * Movies
 * -------
 */
val actionMovies = listOf("Die Hard", "Breakpoint", "Speed")
val sciFi = listOf("Avatar", "StarWars", "Solaris")


