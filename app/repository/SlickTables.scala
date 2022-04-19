package repository

import com.google.inject.Inject
import models.{FriendRequest, User}
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

  class FriendRequestTable(tag: Tag) extends Table[FriendRequest](tag, "friend_request") {

    def friendRequestId = column[Int]("friend_request_id", O.PrimaryKey, O.AutoInc)
    def requestFrom = column[Int]("request_from")
    def requestTo = column[Int]("request_to")
    def status = column[String]("status")

    def * = (friendRequestId, requestFrom, requestTo, status) <> (FriendRequest.tupled, FriendRequest.unapply)
  }

  val friendRequests = TableQuery[FriendRequestTable]

}
