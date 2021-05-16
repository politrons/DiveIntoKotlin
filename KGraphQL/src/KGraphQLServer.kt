package com.politrons

import com.apurebase.kgraphql.GraphQL
import io.ktor.application.*

/**
 * To make a request to the service, connect to graphql interface [http://localhost:1981/graphql]
 */
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    /**
     * We install the Ktor module [GraphQL] giving us as part of the DSL the
     * possibility to create the Schema programmatically.
     * [playground] This adds support for opening the graphql route within the browser
     */
    install(GraphQL) {
        playground = true

        /**
         * We create programmatically the schema usibng:
         * [configure] allow us configure the schema config programmatically
         */
        schema {

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
             * To search for a band albums and year by band name we do the query
             * * {music(band:"Depeche mode") {year, album}}
             * * where first part of the query is what we want to search [music(band:"Depeche mode")]
             * * And the second part is what we want to obtain from the search [{year, album}]
             */
            query("musicBand") {
                resolver { band: String ->
                    println("Searching for band:$band Thread:${Thread.currentThread().name}")
                    when (band) {
                        "Depeche mode", "depeche mode" -> depecheModeAlbums
                        "Counting Crows", "counting crows" -> countingCrowsAlbums
                        else -> listOf(Music("You need to specify a band.", 0))
                    }
                }
            }

            /**
             * To search for a band albums and year by album we do the query
             * *  music(year:1998) {year, album}
             */
            query("musicAlbum") {
                resolver { album: String ->
                    println("Searching for year:$album Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.album == album }
                }
            }

            /**
             * To search for a band albums and year by year we do the query
             * *  music(year:1998) {year, album}
             */
            query("musicYear") {
                resolver { year: Int ->
                    println("Searching for year:$year Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.year == year }
                }
            }

            query("musicAlbumYear") {
                resolver { year: Int, album: String ->
                    println("Searching for year:$year Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.year == year || music.album == album }
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

            query("services") {
                resolver { -> listOf("music", "movies") }
            }
        }
    }
}

/**
 * Music
 * ------
 */

/**
 * In kGraphQL it's mandatory use [data] class type to render resppnses.
 */
data class Music(val album: String, val year: Int)

val countingCrowsAlbums =
    listOf(
        Music("August and everything after", 1996),
        Music("Recovering the Satellites", 1998),
        Music("This desert life", 2000),
        Music("Hard Candy", 2003)
    )
val depecheModeAlbums = listOf(
    Music("Sound of the Universe", 2008),
    Music("Delta Machine", 2011),
    Music("Music For The Masses", 1987),
    Music("Exciter", 2001)
)

val allAlbums = countingCrowsAlbums + depecheModeAlbums

/**
 * Movies
 * -------
 */

val actionMovies = listOf("Die Hard", "Breakpoint", "Speed")
val sciFi = listOf("Avatar", "StarWars", "Solaris")


