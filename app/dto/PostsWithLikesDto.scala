package dto

import org.joda.time.DateTime

case class PostsWithLikesDto(id: Int, userId: Int, dateTime: DateTime, title: String, text: String, likeCount: Int)
