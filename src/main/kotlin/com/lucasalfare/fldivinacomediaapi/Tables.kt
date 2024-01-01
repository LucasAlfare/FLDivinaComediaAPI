package com.lucasalfare.fldivinacomediaapi

import org.jetbrains.exposed.dao.id.IntIdTable

/**
 * Database table for storing information about books.
 */
object Books : IntIdTable("Books") {
  /**
   * The name of the book.
   */
  val name = text("name")
}

/**
 * Database table for storing information about chapters.
 *
 * TODO: include column for "title"
 */
object Chapters : IntIdTable("Chapters") {
  /**
   * The order of the chapter in the book.
   */
  val order = integer("order")

  /**
   * The textual content of the chapter.
   */
  val content = text("content")

  /**
   * The ID of the book to which the chapter belongs.
   */
  val bookId = integer("book_id").references(Books.id)
}

/**
 * Database table for storing notes associated with chapters.
 */
object Notes : IntIdTable("Notes") {
  /**
   * The textual content of the note.
   */
  val content = text("content")

  /**
   * The ID of the chapter to which the note is related.
   */
  val chapterId = integer("chapter_id").references(Chapters.id)
}