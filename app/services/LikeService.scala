package services

import models.Like
import repository.LikeRepository
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class LikeService @Inject()(likesRepository: LikeRepository)(implicit executionContext: ExecutionContext) {

  def addLike(like: Like) = {
    likesRepository.getLike(like.userId, like.postId).flatMap {
      case Some(_) => throw new Exception("Already liked")
      case None    => likesRepository.addLike(like)
    }
  }

  def getLike(userId: Int, postId: Int): Future[Option[Like]] = {
    likesRepository.getLike(userId, postId)
  }

  def dislike(like: Like) = {
    getLike(like.userId, like.postId).flatMap {
      case Some(_) => likesRepository.removeLike(like.userId, like.postId)
      case None =>
        throw new Exception("Post isn't liked")
    }
  }

//  def countLikesForPost(postId: Int) = {
//    likesRepository.countLikesForPost(postId)
//  }

  def countLikesForPost(postId: Int) = {
    likesRepository.countLikesForPost(postId)
  }

}
