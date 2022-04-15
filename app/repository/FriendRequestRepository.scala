package repository

import com.google.inject.Inject
import models.FriendRequest
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class FriendRequestRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  val friendRequests = TableQuery[FriendRequestTable]

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

  class FriendRequestTable(tag: Tag) extends Table[FriendRequest](tag, "friend_request") {

    def friendRequestId = column[Int]("friend_request_id", O.PrimaryKey, O.AutoInc)
    def requestFrom = column[Int]("request_from")
    def requestTo = column[Int]("request_to")
    def status = column[String]("status")

    def * = (friendRequestId, requestFrom, requestTo, status) <> (FriendRequest.tupled, FriendRequest.unapply)

  }

}
