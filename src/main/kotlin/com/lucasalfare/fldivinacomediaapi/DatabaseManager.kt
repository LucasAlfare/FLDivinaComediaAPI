@file:Suppress("MemberVisibilityCanBePrivate")

package com.lucasalfare.fldivinacomediaapi

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


/**
 * Manages the database connection and provides utility functions for database operations.
 */
object DatabaseManager {

  // The HikariCP data source for the PostgreSQL database.
  private val db = createHikariDataSource()

  /**
   * Initializes the database by creating tables and optionally building data from raw sources.
   *
   * @param buildDataFromRaw If true, builds sample data in the database.
   */
  suspend fun initialize(buildDataFromRaw: Boolean = false) {
    transaction(Database.connect(db)) {
      // Create missing tables and columns for the specified entities.
      SchemaUtils.createMissingTablesAndColumns(Books, Chapters, Notes)
    }

    if (buildDataFromRaw) {
      // Build sample data for books, chapters, and notes.
      DatabaseBuilder.createBook(BOOK_INFERNO_DEFAULT_NAME)
      DatabaseBuilder.createBook(BOOK_PURGATORY_DEFAULT_NAME)
      DatabaseBuilder.createBook(BOOK_PARADISE_DEFAULT_NAME)

      DatabaseBuilder.createChaptersForBookNameAndId(
        BOOK_INFERNO_DEFAULT_NAME,
        BOOK_INFERNO_DEFAULT_ID
      )
      DatabaseBuilder.createChaptersForBookNameAndId(
        BOOK_PURGATORY_DEFAULT_NAME,
        BOOK_PURGATORY_DEFAULT_ID
      )
      DatabaseBuilder.createChaptersForBookNameAndId(
        BOOK_PARADISE_DEFAULT_NAME,
        BOOK_PARADISE_DEFAULT_ID
      )

      DatabaseBuilder.createNotesForBook(
        BOOK_INFERNO_DEFAULT_NAME,
        BOOK_INFERNO_DEFAULT_ID
      )
      DatabaseBuilder.createNotesForBook(
        BOOK_PURGATORY_DEFAULT_NAME,
        BOOK_PURGATORY_DEFAULT_ID
      )
      DatabaseBuilder.createNotesForBook(
        BOOK_PARADISE_DEFAULT_NAME,
        BOOK_PARADISE_DEFAULT_ID
      )
    }
  }

  /**
   * Retrieves a book by its name, along with its chapters and associated notes.
   *
   * @param targetBookName The name of the target book.
   * @return A [Book] object representing the book, its chapters, and associated notes.
   */
  suspend fun getBookByName(targetBookName: String) = dbQuery {
    // TODO: refactor to return "singleOrNull"
    Books.select { Books.name eq targetBookName }.map {
      Book(
        id = it[Books.id].value,
        bookName = targetBookName,
        chapters = getChaptersByBookName(targetBookName)
      )
    }
  }

  suspend fun getChapterByBookNameAndChapterOrder(targetBookName: String, targetChapterOrder: Int) = dbQuery {
    val res = Chapters.innerJoin(Books).select {
      (Books.name eq targetBookName) and (Chapters.order eq targetChapterOrder)
    }.singleOrNull()
    if (res != null) {
      Chapter(
        id = res[Chapters.id].value,
        relatedBookName = targetBookName,
        order = res[Chapters.order],
        content = res[Chapters.content],
        notes = getNotesByBookNameAndChapterOrder(targetBookName, targetChapterOrder)
      )
    } else {
      null
    }
  }

  /**
   * Retrieves chapters for a given book by its name, along with associated notes.
   *
   * @param targetBookName The name of the target book.
   * @return A list of [Chapter] objects representing the chapters and associated notes.
   */
  suspend fun getChaptersByBookName(targetBookName: String) = dbQuery {
    Chapters.innerJoin(Books).select {
      (Books.name eq targetBookName)
    }.map {
      Chapter(
        id = it[Chapters.id].value,
        relatedBookName = targetBookName,
        order = it[Chapters.order],
        content = it[Chapters.content],
        notes = getNotesByBookNameAndChapterOrder(targetBookName, it[Chapters.order])
      )
    }
  }

  /**
   * Retrieves notes for a given book and chapter order.
   *
   * @param targetBookName The name of the target book.
   * @param targetChapterOrder The order of the target chapter.
   * @return A list of [Note] objects representing the notes associated with the book and chapter.
   */
  suspend fun getNotesByBookNameAndChapterOrder(targetBookName: String, targetChapterOrder: Int) = dbQuery {
    Notes.innerJoin(Chapters).innerJoin(Books).select {
      (Books.name eq targetBookName) and (Chapters.order eq targetChapterOrder)
    }.map {
      Note(
        id = it[Notes.id].value,
        relatedBookName = targetBookName,
        relatedChapterOrder = targetChapterOrder,
        content = it[Notes.content]
      )
    }
  }

  /**
   * Creates and configures a HikariCP data source for the PostgreSQL database.
   */
  private fun createHikariDataSource(): HikariDataSource {
    // Retrieve PostgreSQL connection details from environment variables.
    val address = System.getenv("POSTGRES_ADDRESS")
    val user = System.getenv("POSTGRES_USER")
    val password = System.getenv("POSTGRES_PASSWORD")
    val db = System.getenv("POSTGRES_DB")

    // Configure HikariCP with PostgreSQL connection details.
    val config = HikariConfig()
    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://$address/$db"
    config.username = user
    config.password = password
    config.maximumPoolSize = 16
    config.isAutoCommit = false

    return HikariDataSource(config)
  }

  /**
   * Executes a suspended database query with the provided block.
   *
   * @param targetDb The target database connection (default is the main database).
   * @param block The block of code to execute within the transaction.
   * @return The result of the executed block.
   */
  suspend fun <T> dbQuery(targetDb: Database = Database.connect(db), block: suspend () -> T): T =
    newSuspendedTransaction(db = targetDb, context = Dispatchers.IO) { block() }
}
