package com.lucasalfare.fldivinacomediaapi.routes

import com.lucasalfare.fldivinacomediaapi.DatabaseManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getNotes() {
  // expects a query in the url, such as: /notes?book=inferno&chapter=1
  get("/notes") {
    val queryBookName = call.request.queryParameters["book"]
    val queryChapterOrder = call.request.queryParameters["chapter"]
    try {
      requireNotNull(queryBookName)
      requireNotNull(queryChapterOrder)
      val res = DatabaseManager.getNotesByBookNameAndChapterOrder(queryBookName, queryChapterOrder.toInt())
      call.respond(HttpStatusCode.OK, res)
    } catch (e: Exception) {
      call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Could not to retrieve the requested book name and/or chapter order from the query of the URL."
      )
    }
  }
}