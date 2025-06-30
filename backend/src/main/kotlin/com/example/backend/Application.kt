package com.example.backend

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 5000) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    routing {
        get("/") { call.respondText("Backend running") }
    }
}
