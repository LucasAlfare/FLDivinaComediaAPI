package com.lucasalfare.fldivinacomediaapi

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import kotlin.system.measureTimeMillis

suspend fun main() {
  val buildDataFromRaw = System.getenv("API_BUILD_DATA_FROM_RAW") == "1"

  val elapsedTime = measureTimeMillis {
    DatabaseManager.initialize(buildDataFromRaw)
  }

  println("Took ${SimpleDateFormat("mm:ss.SSS").format(elapsedTime)} to initialize/build all data.")

  embeddedServer(
    factory = Netty,
    port = 9999,
    module = {
      install(ContentNegotiation) {
        json(json = Json {
          isLenient = false
        })
      }

      routing {
        get("/") {
          call.respond(mapOf("greet" to "Bem vindo à API da Divina Comédia!! :D"))
        }

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

        // example: /book_by_name?book_name=inferno
        get("/book_by_name") {
          val queryBookName = call.request.queryParameters["book_name"]
          try {
            requireNotNull(queryBookName)
            val res = DatabaseManager.getBookByName(queryBookName)
            call.respond(HttpStatusCode.OK, res)
          } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
          }
        }

        // example: /chapter_by_book_name_and_order?book_name=inferno&order=1
        get("/chapter_by_book_name_and_order") {
          val queryBookName = call.request.queryParameters["book_name"]
          val queryChapterOrder = call.request.queryParameters["order"]
          try {
            requireNotNull(queryBookName)
            requireNotNull(queryChapterOrder)
            val res = DatabaseManager.getChapterByBookNameAndChapterOrder(queryBookName, queryChapterOrder.toInt())
            if (res != null) {
              call.respond(HttpStatusCode.OK, res)
            } else {
              call.respond(HttpStatusCode.NotFound)
            }
          } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
          }
        }

        // example: /notes_by_book_and_chapter?book_name=inferno&chapter_order=1
        get("/notes_by_book_and_chapter") {
          val queryBookName = call.request.queryParameters["book_name"]
          val queryChapterOrder = call.request.queryParameters["chapter_order"]
          try {
            requireNotNull(queryBookName)
            requireNotNull(queryChapterOrder)
            val res = DatabaseManager.getNotesByBookNameAndChapterOrder(queryBookName, queryChapterOrder.toInt())
            call.respond(HttpStatusCode.OK, res)
          } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest)
          }
        }
      }
    }
  ).start(wait = true)
}