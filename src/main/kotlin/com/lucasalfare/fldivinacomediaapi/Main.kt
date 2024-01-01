package com.lucasalfare.fldivinacomediaapi

import com.lucasalfare.fldivinacomediaapi.routes.getAllBooks
import com.lucasalfare.fldivinacomediaapi.routes.getBookByName
import com.lucasalfare.fldivinacomediaapi.routes.getChapter
import com.lucasalfare.fldivinacomediaapi.routes.getNotes
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
        getAllBooks()
        getBookByName()
        getChapter()
        getNotes()
      }
    }
  ).start(wait = true)
}