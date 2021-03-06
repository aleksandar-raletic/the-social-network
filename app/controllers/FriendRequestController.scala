package controllers

import models.Formats.{acceptDeclineFriendRequestFormat, createFriendRequestFormat, friendRequestsWithUsersFormat, showUserFormat}
import models.{AcceptDeclineFriendRequest, CreateFriendRequest, ShowUser}
import dto.FriendRequestsWithUsersDto
import play.api.libs.json.Json
import play.api.Logging
import play.api.mvc.{AbstractController, ControllerComponents}
import services.FriendRequestService
import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class FriendRequestController @Inject()(cc: ControllerComponents, friendRequestService: FriendRequestService)(
    implicit executionContext: ExecutionContext
) extends AbstractController(cc)
    with Logging {

  def sendFriendRequest() = Action.async(parse.json[CreateFriendRequest]) { request =>
    val createFriendRequest: CreateFriendRequest = request.body
    friendRequestService
      .sendFriendRequest(createFriendRequest)
      .map(createdFriendRequest => Ok(Json.toJson("Friend request sent")))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def acceptFriendRequest() = Action.async(parse.json[AcceptDeclineFriendRequest]) { request =>
    val acceptFriendRequest: AcceptDeclineFriendRequest = request.body
    friendRequestService
      .acceptFriendRequest(acceptFriendRequest)
      .map(acceptedFriendRequest => Ok(Json.toJson("Friend request accepted")))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def declineFriendRequest() = Action.async(parse.json[AcceptDeclineFriendRequest]) { request =>
    val declineFriendRequest: AcceptDeclineFriendRequest = request.body
    friendRequestService
      .declineFriendRequest(declineFriendRequest)
      .map(declinedFriendRequest => Ok(Json.toJson("Friend request declined")))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def getFriendsForUser(userId: Int) = Action.async {
    friendRequestService
      .getFriendsForUser(userId)
      .map((friends: Vector[ShowUser]) => Ok(Json.toJson(friends)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def getFriendRequestsWithUsers(userId: Int) = Action.async {
    friendRequestService
      .getFriendRequestsWithUsers(userId)
      .map((pendingFriends: Vector[FriendRequestsWithUsersDto]) => Ok(Json.toJson(pendingFriends)))
      .recover(exception => BadRequest(exception.getMessage))
  }

}
