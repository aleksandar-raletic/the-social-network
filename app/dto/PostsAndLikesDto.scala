package dto

import org.joda.time.DateTime

case class PostsAndLikesDto(id: Int, userId: Int, dateTime: DateTime, title: String, text: String, likeCount: Int)
