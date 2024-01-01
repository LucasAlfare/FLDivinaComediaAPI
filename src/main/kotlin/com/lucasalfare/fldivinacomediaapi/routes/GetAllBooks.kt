package com.lucasalfare.fldivinacomediaapi.routes

import com.lucasalfare.fldivinacomediaapi.DatabaseManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAllBooks() {
  get("/books") {
    try {
      val books = listOf(
        DatabaseManager.getBookByName("inferno"),
        DatabaseManager.getBookByName("purgatorio"),
        DatabaseManager.getBookByName("paraiso")
      )

      call.respond(HttpStatusCode.OK, books)
    } catch (e: Exception) {
      call.respond(HttpStatusCode.InternalServerError)
    }
  }
}