package com.lucasalfare.fldivinacomediaapi.routes

import com.lucasalfare.fldivinacomediaapi.DatabaseManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getBookByName() {
  // expects a query in the url, such as: "/book?name=inferno"
  get("/book") {
    val queryBookName = call.request.queryParameters["name"]
    try {
      requireNotNull(queryBookName)
      val res = DatabaseManager.getBookByName(queryBookName)
      call.respond(HttpStatusCode.OK, res)
    } catch (e: Exception) {
      call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Could not to retrieve the requested book name from the query of the URL."
      )
    }
  }
}