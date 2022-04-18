package controllers

import models.Formats.{likeFormat, countLikeFormat}
import models.{Like, CountLike}
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

  def addLike() = Action.async(parse.json[Like]) { request =>
    val like: Like = request.body
    likeService
      .addLike(like)
      .map(createdLike => Ok(Json.toJson("Liked post")))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def removeLike() = Action.async(parse.json[Like]) { request =>
    val like: Like = request.body
    likeService
      .dislike(like)
      .map(removedLike => Ok(Json.toJson("Disliked post")))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def countLikesForPost() = Action.async(parse.json[CountLike]) { request =>
    val countLike: CountLike = request.body
    likeService
      .countLikesForPost(countLike.postId)
      .map(
        (countedLikes: Option[Int]) => Ok(Json.toJson(s"Post with id: ${countLike.postId} has " + countedLikes.get + " number of likes"))
      )
      .recover(exception => BadRequest(exception.getMessage))
  }

}
