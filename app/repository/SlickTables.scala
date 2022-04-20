package repository

import com.google.inject.Inject
import models.{FriendRequest, Like, Post, User}
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

class SlickTables @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def password = column[String]("password")
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")
    def email = column[String]("email")
    def * = (id, password, firstName, lastName, email) <> (User.tupled, User.unapply)
  }
  val users = TableQuery[UserTable]

  import com.github.tototoshi.slick.MySQLJodaSupport._
  class PostTable(tag: Tag) extends Table[Post](tag, "post") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def dateTime = column[DateTime]("date_time")
    def title = column[String]("title")
    def text = column[String]("text")
    def * = (id, userId, dateTime, title, text) <> (Post.tupled, Post.unapply)

  }
  val posts = TableQuery[PostTable]

  class LikesTable(tag: Tag) extends Table[Like](tag, "likes") {
    def userId = column[Int]("user_id")
    def postId = column[Int]("post_id")
    def * = (userId, postId) <> (Like.tupled, Like.unapply)
  }
  val likes = TableQuery[LikesTable]

  class FriendRequestTable(tag: Tag) extends Table[FriendRequest](tag, "friend_request") {
    def friendRequestId = column[Int]("friend_request_id", O.PrimaryKey, O.AutoInc)
    def requestFrom = column[Int]("request_from")
    def requestTo = column[Int]("request_to")
    def status = column[String]("status")
    def * = (friendRequestId, requestFrom, requestTo, status) <> (FriendRequest.tupled, FriendRequest.unapply)
  }
  val friendRequests = TableQuery[FriendRequestTable]

}
