package com.lucasalfare.fldivinacomediaapi.routes

import com.lucasalfare.fldivinacomediaapi.BOOK_INFERNO_DEFAULT_NAME
import com.lucasalfare.fldivinacomediaapi.BOOK_PARADISE_DEFAULT_NAME
import com.lucasalfare.fldivinacomediaapi.BOOK_PURGATORY_DEFAULT_NAME
import com.lucasalfare.fldivinacomediaapi.DatabaseManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getAllBooks() {
  get("/books") {
    try {
      val books = listOf(
        DatabaseManager.getBookByName(BOOK_INFERNO_DEFAULT_NAME),
        DatabaseManager.getBookByName(BOOK_PURGATORY_DEFAULT_NAME),
        DatabaseManager.getBookByName(BOOK_PARADISE_DEFAULT_NAME)
      )

      call.respond(HttpStatusCode.OK, books)
    } catch (e: Exception) {
      call.respond(HttpStatusCode.InternalServerError)
    }
  }
}