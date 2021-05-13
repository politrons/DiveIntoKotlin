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
        schema {
            query("user") {
                resolver { -> "Hi politrons" }
            }
        }
    }
}
