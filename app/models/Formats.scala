package models

import dto.{FriendRequestsWithUsersDto, PostsWithLikesDto}
import play.api.libs.json.Json
import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._

object Formats {
  implicit val userFormat = Json.format[User]
  implicit val createUserFormat = Json.format[CreateUser]
  implicit val showUserFormat = Json.format[ShowUser]

  implicit val postFormat = Json.format[Post]
  implicit val createPostFormat = Json.format[CreatePost]
  implicit val updatePostFormat = Json.format[UpdatePost]
  implicit val showPostFormat = Json.format[ShowPost]

  implicit val likeFormat = Json.format[Like]
  implicit val countLikeFormat = Json.format[CountLike]

  implicit val friendRequestFormat = Json.format[FriendRequest]
  implicit val createFriendRequestFormat = Json.format[CreateFriendRequest]
  implicit val acceptDeclineFriendRequestFormat = Json.format[AcceptDeclineFriendRequest]

  implicit val postsWithLikesFormat = Json.format[PostsWithLikesDto]
  implicit val friendRequestsWithUsersFormat = Json.format[FriendRequestsWithUsersDto]
}
