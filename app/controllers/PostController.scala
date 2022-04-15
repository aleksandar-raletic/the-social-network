package controllers

import models.Formats.{createPostFormat, postFormat, updatePostFormat}
import models.{CreatePost, Post, UpdatePost}
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc._
import services.PostService
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class PostController @Inject()(cc: ControllerComponents, postService: PostService)(implicit executionContext: ExecutionContext)
    extends AbstractController(cc)
    with Logging {

  def addPost() = Action.async(parse.json[CreatePost]) { request =>
    val createPost: CreatePost = request.body
    postService
      .addPost(createPost)
      .map(addedPost => Ok(Json.toJson(addedPost)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def getPost(id: Int) = Action.async {
    postService
      .getPost(id)
      .map((retrievedPost: Option[Post]) => {
        Ok(Json.toJson(retrievedPost))
      })
      .recover(exception => BadRequest(exception.getMessage))
  }

  def updatePost() = Action.async(parse.json[UpdatePost]) { request =>
    val updatePost: UpdatePost = request.body
    postService
      .updatePost(updatePost)
      .map(updatedPost => Ok(Json.toJson(updatedPost)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def deletePost(id: Int) = Action.async {
    postService
      .deletePost(id)
      .map(deletedPost => Ok("Post deleted"))
      .recover(exception => BadRequest(exception.getMessage))
  }

}
