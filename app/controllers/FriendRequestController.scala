package controllers
import models.Formats.{friendRequestFormat, createFriendRequestFormat, acceptDeclineFriendRequestFormat}
import models.{FriendRequest, CreateFriendRequest, AcceptDeclineFriendRequest}
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
      .map(createdFriendRequest => Ok(Json.toJson(createdFriendRequest)))
      .recover(exception => BadRequest(exception.getMessage))

  }

  def acceptFriendRequest() = Action.async(parse.json[AcceptDeclineFriendRequest]) { request =>
    val acceptFriendRequest: AcceptDeclineFriendRequest = request.body
    friendRequestService
      .acceptFriendRequest(acceptFriendRequest)
      .map(acceptedFriendRequest => Ok(Json.toJson(acceptedFriendRequest)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def declineFriendRequest() = Action.async(parse.json[AcceptDeclineFriendRequest]) { request =>
    val declineFriendRequest: AcceptDeclineFriendRequest = request.body
    friendRequestService
      .declineFriendRequest(declineFriendRequest)
      .map(declinedFriendRequest => Ok(Json.toJson(declinedFriendRequest)))
      .recover(exception => BadRequest(exception.getMessage))
  }

}
