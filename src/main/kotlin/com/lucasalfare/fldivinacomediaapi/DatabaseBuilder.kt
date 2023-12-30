package com.lucasalfare.fldivinacomediaapi

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

/**
 * Utility class for building and populating the database with data from raw files.
 */
object DatabaseBuilder {

  /**
   * Creates a new book entry in the database with the given name.
   *
   * @param bookName The name of the book to be created.
   */
  suspend fun createBook(bookName: String) {
    DatabaseManager.dbQuery {
      Books.insert {
        it[name] = bookName
        it[id] = when (bookName) {
          "inferno" -> BOOK_INFERNO_DEFAULT_ID
          "purgatorio" -> BOOK_PURGATORY_DEFAULT_ID
          else -> BOOK_PARADISE_DEFAULT_ID
        }
      }
    }
  }

  /**
   * Creates chapters for a specified book based on its name and ID.
   *
   * @param targetBookName The name of the target book.
   * @param targetBookId The ID of the target book.
   */
  suspend fun createChaptersForBookNameAndId(targetBookName: String, targetBookId: Int) {
    val chaptersRanges = when (targetBookId) {
      BOOK_INFERNO_DEFAULT_ID -> (1..34)
      BOOK_PURGATORY_DEFAULT_ID -> (1..33)
      else -> (1..6)
    }

    chaptersRanges.forEach { current ->
      val rawChapterContent = ResourceLoader.loadAsString("$targetBookName/$current")
      DatabaseManager.dbQuery {
        Chapters.insert {
          it[order] = current
          it[content] = rawChapterContent
          it[bookId] = targetBookId
        }
      }
    }
  }

  /**
   * Creates notes for a specified book based on its name and ID.
   *
   * @param targetBookName The name of the target book.
   * @param targetBookId The ID of the target book.
   *
   * @throws IllegalArgumentException if there is an error parsing the chapter order from a note.
   */
  suspend fun createNotesForBook(targetBookName: String, targetBookId: Int) {
    val raw = ResourceLoader.loadAsString("$targetBookName/notas")
    raw.split("\n").forEach { currentNote ->
      val parsedChapterOrder = try {
        buildString {
          if (currentNote[0] == '0') {
            append(currentNote[1])
          } else {
            append(currentNote[0])
            append(currentNote[1])
          }
        }.toInt()
      } catch (e: Exception) {
        error("Error when parsing chapter order of the following [$targetBookName] note: [$currentNote]")
      }

      val chapterIdSearch = findChapterIdByOrder(targetBookId, parsedChapterOrder)
      if (chapterIdSearch != null) {
        DatabaseManager.dbQuery {
          Notes.insert {
            it[content] = currentNote
            it[chapterId] = chapterIdSearch
          }
        }
      }
    }
  }

  /**
   * Finds the ID of a chapter in the database based on the book ID and chapter order.
   *
   * @param bookId The ID of the book.
   * @param targetChapterOrder The order of the target chapter.
   * @return The ID of the chapter if found, null otherwise.
   */
  private suspend fun findChapterIdByOrder(bookId: Int, targetChapterOrder: Int): Int? {
    return DatabaseManager.dbQuery {
      Chapters
        .slice(Chapters.id)
        .select { (Chapters.bookId eq bookId) and (Chapters.order eq targetChapterOrder) }
        .singleOrNull()?.get(Chapters.id)?.value
    }
  }
}
