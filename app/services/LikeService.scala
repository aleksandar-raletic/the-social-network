package services

import models.Like
import repository.LikeRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class LikeService @Inject()(likesRepository: LikeRepository)(implicit executionContext: ExecutionContext) {

//def addLikes(like: Like) = {
//  likesRepository.addLike(like)
//}

//def addLikes(like: Like) = {
//  getLike(like.userId).flatMap{maybeLike =>
//    if (maybeLike.isDefined){
//      throw new Exception}
//    else{
//      likesRepository.addLike(like)
//    }
//  }
//}

  def getLike(userId: Int, postId: Int): Future[Option[Like]] = {
    likesRepository.getLike(userId, postId)
  }

//  def removeLike(userId: Int, postId: Int) = {
//    likesRepository.removeLike(userId, postId)
//  }

  def addLike(like: Like) = {
    getLike(like.userId, like.postId).flatMap {
      case Some(_) => throw new Exception("Already liked")
      case None    => likesRepository.addLike(like)
    }
  }

  def dislike(like: Like) = {
    getLike(like.userId, like.postId).flatMap {
      case Some(_) => likesRepository.removeLike(like.userId, like.postId)
      case None    => throw new Exception("Post isn't liked")
    }
  }

//  def dislike(like: Like) = {
//    removeLike(like.userId, like.postId).map((maybeDislike: Option[Like]) => {
//      case Some => likesRepository.removeLike(like)
//      case None => throw new Exception("Post isn't liked")
//    })
//  }

//  def getPost(id: Int) = Action.async {
//    postService
//      .getPost(id)
//      .map((maybePost: Option[Post]) => {
//        if (maybePost.isDefined) {
//          Ok(Json.toJson(maybePost.get))
//        } else {
//          NotFound
//        }
//      })
//  }

}
