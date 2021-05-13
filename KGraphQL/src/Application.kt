package com.politrons

import com.apurebase.kgraphql.GraphQL
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(GraphQL) {
        playground = true

        // KGraphQL#schema { } is entry point to create KGraphQL schema
        schema {
            //configure method allows you customize schema behaviour
            configure {
                useDefaultPrettyPrinter = true
            }

            // create query "hero" which returns instance of Character
            query("hero") {
                resolver { episode: String ->
                    when (episode) {
                        "NEWHOPE", "JEDI" -> r2d2
                        "EMPIRE" -> luke
                        else -> ""
                    }
                }
            }

            // create query "heroes" which returns list of luke and r2d2
            query("heroes") {
                resolver { -> listOf(luke, r2d2) }
            }
        }
    }
}

val luke = "Luke Skywalker"

val r2d2 = "R2-D2"

//{
//    hero(episode: "JEDI")
//}

