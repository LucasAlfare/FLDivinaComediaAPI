package com.lucasalfare.fldivinacomediaapi

import kotlinx.serialization.Serializable

@Serializable
data class Book(
  val id: Int,
  val bookName: String,
  val chapters: List<Chapter>
)

@Serializable
data class Chapter(
  val id: Int,
  val relatedBookName: String,
  val order: Int,
  val content: String,
  val notes: List<Note>
)

@Serializable
data class Note(
  val id: Int,
  val relatedBookName: String,
  val relatedChapterOrder: Int,
  val content: String
)