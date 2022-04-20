package repository

import com.google.inject.Inject
import models.{FriendRequest, ShowUser, User}
import dto.FriendRequestsWithUsersDto
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

class FriendRequestRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, slickTables: SlickTables)(
    implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import slickTables._

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

  implicit val getFriendsForUserResult = GetResult(r => ShowUser(r.<<, r.<<, r.<<, r.<<))
  def getFriendsForUser(userId: Int) = {
    db.run(
      sql"""SELECT id, first_name, last_name, email FROM user JOIN friend_request ON user.id=friend_request.request_from WHERE (request_to = $userId AND status = "Friend request accepted") UNION SELECT id, first_name, last_name, email FROM user JOIN friend_request ON user.id=friend_request.request_to WHERE (request_from = $userId AND status = "Friend request accepted")"""
        .as[ShowUser]
    )
  }

  implicit val getFriendRequestsWithUsersDtoResult = GetResult(r => FriendRequestsWithUsersDto(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))
  def getFriendRequestsWithUsers(userId: Int) = {
    db.run(
      sql"""SELECT id, first_name, last_name, email, request_from, request_to, status FROM user JOIN friend_request ON user.id=friend_request.request_from WHERE (request_to = $userId AND status = "Pending") UNION SELECT id, first_name, last_name, email, request_from, request_to, status FROM user JOIN friend_request ON user.id=friend_request.request_to WHERE (request_from = $userId AND status = "Pending")"""
        .as[FriendRequestsWithUsersDto]
    )
  }

}
