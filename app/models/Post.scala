package models

import org.joda.time._

case class Post(id: Int, userId: Int, dateTime: DateTime, title: String, text: String)

case class CreatePost(userId: Int, title: String, text: String)

case class UpdatePost(id: Int, title: String, text: String)
