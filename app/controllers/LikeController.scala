package controllers
import models.Formats.likeFormat
import models.Like
import play.api.Logging

import play.api.libs.json.Json
import play.api.mvc._
import services.LikeService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class LikeController @Inject()(cc: ControllerComponents, likeService: LikeService)(implicit executionContext: ExecutionContext)
    extends AbstractController(cc)
    with Logging {

//  def addLike() = Action.async(parse.json[Like]) { request =>
//    val like: Like = request.body
//    likeService.addLikes(like).map(createdLike => Ok(Json.toJson(createdLike)))
//  }

  def addLike() = Action.async(parse.json[Like]) { request =>
    val like: Like = request.body
    likeService
      .addLike(like)
      .map(createdLike => Ok(Json.toJson(createdLike)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def removeLike() = Action.async(parse.json[Like]) { request =>
    val like: Like = request.body
    likeService
      .dislike(like)
      .map(createdLike => Ok(Json.toJson(createdLike)))
      .recover(exception => BadRequest(exception.getMessage))
  }

//  def getLike(userId: Int, postId: Int) = Action.async {
//    likeService.getLike(userId, postId).map((maybeLike: Option[Like]) => {
//      if (maybeLike.isDefined) {
//        Ok(Json.toJson(maybeLike.get))
//      }else{
//        NotFound
//      }
//    })
//  }

}
