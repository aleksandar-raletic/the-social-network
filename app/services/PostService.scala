package services

import dto.PostsWithLikesDto
import models.{CreatePost, Post, UpdatePost, ShowPost}
import org.joda.time.DateTime
import repository.PostRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PostService @Inject()(postRepository: PostRepository)(implicit executionContext: ExecutionContext) {

  def addPost(createPost: CreatePost) = {
    val post = Post(id = 0, userId = createPost.userId, DateTime.now(), createPost.title, createPost.text)
    postRepository.addPost(post)
  }

  def getPost(id: Int): Future[Option[Post]] = {
    postRepository.getPost(id).flatMap {
      case Some(_) => postRepository.getPost(id)
      case None    => throw new Exception("Post with specified id doesn't exist")
    }
  }

  def updatePost(updatePost: UpdatePost) = {
    postRepository.getPost(updatePost.id).flatMap {
      case Some(post) =>
        val updatedPost = post.copy(title = updatePost.title, text = updatePost.text)
        postRepository.updatePost(updatedPost).map(_ => updatedPost)
      case None => throw new Exception("Post with specified id doesn't exist")
    }
  }

  def deletePost(id: Int): Future[Int] = {
    postRepository.getPost(id).flatMap {
      case Some(_) => postRepository.deletePost(id)
      case None    => throw new Exception("Post with specified id doesn't exist")
    }
  }

  def countLikesForPost(id: Int) = {
    postRepository.getPost(id).flatMap {
      case Some(post) =>
        postRepository
          .countLikesForPost(id)
          .map(
            likesCount =>
              PostsWithLikesDto(
                id = post.id,
                userId = post.userId,
                dateTime = post.dateTime,
                title = post.title,
                text = post.text,
                likeCount = likesCount.getOrElse(-1)
              )
          )
      case None => throw new Exception("Error")
    }
  }

  def listPostsForUser(userId: Int) = {
    postRepository.listPostsForUser(userId)
  }

}
