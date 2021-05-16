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
             * * {musicBand(band:"Depeche mode") {year, album}}
             * * where first part of the query is what we want to search [music(band:"Depeche mode")]
             * * And the second part is what we want to obtain from the search [{year, album}]
             */
            query("musicBand") {
                resolver { band: String ->
                    println("Searching for band:$band Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.band.equals(band, ignoreCase = true) }
                }
            }

            /**
             * To search for a band albums and year by album we do the query
             * *  musicAlbum(year:1998) {year, album}
             */
            query("musicAlbum") {
                resolver { album: String ->
                    println("Searching for year:$album Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.album == album }
                }
            }

            /**
             * To search for a band albums and year by year we do the query
             * *  musicYear(year:1998) {year, album}
             */
            query("musicYear") {
                resolver { year: Int ->
                    println("Searching for year:$year Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.year == year }
                }
            }

            /**
             * To search for a band albums and year by year we do the query
             * *  musicYear(year:1998 album:"Recovering the Satellites") {year, album}
             */
            query("musicAlbumYear") {
                resolver { year: Int, album: String ->
                    println("Searching for year:$year Thread:${Thread.currentThread().name}")
                    allAlbums.filter { music -> music.year == year || music.album == album }
                }
            }

            query("movieGenre") {
                resolver { genre: String ->
                    println("Searching movie by genre:$genre Thread:${Thread.currentThread().name}")
                    allMovies.filter { movie -> movie.genre.equals(genre, ignoreCase = true) }
                }
            }

            query("movieYear") {
                resolver { year: Int ->
                    println("Searching movie by year:$year Thread:${Thread.currentThread().name}")
                    allMovies.filter { movie -> movie.year == year }
                }
            }
        }
    }
}

/**
 * Music
 * ------
 */

/**
 * In kGraphQL it's mandatory use [data] class type to render responses.
 */
data class Music(val band: String, val album: String, val year: Int)

val countingCrowsAlbums =
    listOf(
        Music("counting crows", "August and everything after", 1996),
        Music("counting crows", "Recovering the Satellites", 1998),
        Music("counting crows", "This desert life", 2000),
        Music("counting crows", "Hard Candy", 2003)
    )
val depecheModeAlbums = listOf(
    Music("Depeche mode", "Sound of the Universe", 2008),
    Music("Depeche mode", "Delta Machine", 2011),
    Music("Depeche mode", "Music For The Masses", 1987),
    Music("Depeche mode", "Exciter", 2001)
)

val allAlbums = countingCrowsAlbums + depecheModeAlbums

/**
 * Movies
 * -------
 */
data class Movie(val name: String, val genre: String, val year: Int)

val actionMovies = listOf(
    Movie("Die Hard", "Action", 1989),
    Movie("Breakpoint", "Action", 1991),
    Movie("Speed", "Action", 1995)
)
val sciFi = listOf(
    Movie("Avatar", "sci-fi", 2010),
    Movie("StarWars", "sci-fi", 1970),
    Movie("Solaris", "sci-fi", 2005)
)

val allMovies = actionMovies + sciFi

