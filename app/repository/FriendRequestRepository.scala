package repository

import com.google.inject.Inject
import models.{FriendRequest, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class FriendRequestRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, slickTables: SlickTables)(
    implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  import slickTables.users
  import slickTables.friendRequests
  import slickTables.FriendRequestTable

//  val friendRequests = TableQuery[FriendRequestTable]

  def sendFriendRequest(friendRequest: FriendRequest) = {
    db.run(friendRequests += friendRequest)
  }

  def getFriendRequest(requestFrom: Int, requestTo: Int) = {
    db.run(
      friendRequests
        .filter(
          friendRequest =>
            (friendRequest.requestFrom === requestFrom && friendRequest.requestTo === requestTo) || (friendRequest.requestFrom === requestTo && friendRequest.requestTo === requestFrom)
        )
        .result
        .headOption
    )
  }

  def getId(friendRequestId: Int): Future[Option[FriendRequest]] = {
    db.run(friendRequests.filter(_.friendRequestId === friendRequestId).result.headOption)
  }

  def acceptFriendRequest(friendRequest: FriendRequest) = {
    db.run(
      friendRequests
        .filter(_.friendRequestId === friendRequest.friendRequestId)
        .update(friendRequest)
    )
  }

  def declineFriendRequest(friendRequestId: Int) = {
    db.run(friendRequests.filter(_.friendRequestId === friendRequestId).delete)
  }

//  def getFriendsForUser(userId: Int) = {
//    db.run(
//      sql"""SELECT id, password, first_name, last_name, email FROM user JOIN friend_request ON user.id=friend_request.request_from WHERE (request_to = 3 AND status = "Friend request accepted") UNION SELECT id, password, first_name, last_name, email FROM user JOIN friend_request ON user.id=friend_request.request_to WHERE (request_from = 3 AND status = "Friend request accepted")"""
//        .as[User]
//    )
//  }
//  class FriendRequestTable(tag: Tag) extends Table[FriendRequest](tag, "friend_request") {
//
//    def friendRequestId = column[Int]("friend_request_id", O.PrimaryKey, O.AutoInc)
//    def requestFrom = column[Int]("request_from")
//    def requestTo = column[Int]("request_to")
//    def status = column[String]("status")
//
//    def * = (friendRequestId, requestFrom, requestTo, status) <> (FriendRequest.tupled, FriendRequest.unapply)
//
//  }

}
