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

//  def addPost() = Action.async(parse.json[CreatePost]) { request =>
//    val createPost: CreatePost = request.body
//    postService.addPost(createPost).map(createdPost => Ok(Json.toJson(createdPost)))
//  }

    def addPost() = Action.async(parse.json[CreatePost]) { request =>
      val createPost: CreatePost = request.body
      postService.addPost(createPost).map(createdPost => Ok(Json.toJson(createdPost)))
    }

  def getPost(id: Int) = Action.async {
    postService
      .getPost(id)
      .map((maybePost: Option[Post]) => {
        if (maybePost.isDefined) {
          Ok(Json.toJson(maybePost.get))
        } else {
          NotFound
        }
      })
  }

  def deletePost(id: Int) = Action.async {
    postService.deletePost(id).map(message => Ok("Post deleted"))
  }

  def updatePost() = Action.async(parse.json[UpdatePost]) { request =>
    val updatePost: UpdatePost = request.body
    postService.updatePost(updatePost).map(updatedPost => Ok(Json.toJson(updatedPost)))
  }

}
