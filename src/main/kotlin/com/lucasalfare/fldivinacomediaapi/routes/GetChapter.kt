package com.lucasalfare.fldivinacomediaapi.routes

import com.lucasalfare.fldivinacomediaapi.DatabaseManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getChapter() {
  // expects a query in the url, such as: "/chapter?book=inferno&order=1
  get("/chapter") {
    val queryBookName = call.request.queryParameters["book"]
    val queryChapterOrder = call.request.queryParameters["order"]
    try {
      requireNotNull(queryBookName)
      requireNotNull(queryChapterOrder)
      val res = DatabaseManager.getChapterByBookNameAndChapterOrder(queryBookName, queryChapterOrder.toInt())

      call.respond(
        status = if (res != null) HttpStatusCode.OK else HttpStatusCode.NotFound,
        message = res
          ?: "Was not found in database any book named as [$queryBookName] and/or a chapter ordered as [$queryChapterOrder]."
      )
    } catch (e: Exception) {
      call.respond(
        status = HttpStatusCode.BadRequest,
        message = "Could not to retrieve the requested book name and/or chapter order from the query of the URL."
      )
    }
  }
}