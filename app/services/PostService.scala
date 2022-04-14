package services

import models.{CreatePost, Post, UpdatePost}
import org.joda.time.DateTime
import repository.PostRepository

import java.sql.Timestamp
import java.util.Date
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.internal.NoPhase

class PostService @Inject()(postRepository: PostRepository)(implicit executionContext: ExecutionContext) {

//  def addPost(createPost: CreatePost): Future[String] = {
//    val post = Post(id = 0, userId = createPost.userId, DateTime.now(), createPost.title, createPost.text)
//    postRepository.add(post)
//  }

  def addPost(createPost: CreatePost) = {
    val post = Post(id = 0, userId = createPost.userId, DateTime.now(), createPost.title, createPost.text)
    postRepository.add(post).map(_ => post)
  }

//  def addPost(createPost: CreatePost) = {
//    val post = Post(id = 0, userId = createPost.userId, DateTime.now(), createPost.title, createPost.text)
//    postRepository.add(post).map( => post)
//  }

  def getPost(id: Int): Future[Option[Post]] = {
    postRepository.get(id)
  }

  def deletePost(id: Int): Future[Int] = {
    postRepository.delete(id)
  }

  def updatePost(updatePost: UpdatePost) = {
    getPost(updatePost.id).flatMap { maybePost =>
      if (maybePost.isDefined) {
        val post = maybePost.get
        val updatedPost = post.copy(title = updatePost.title, text = updatePost.text)
        postRepository.update(updatedPost).map(_ => updatedPost)
      } else {
        throw new Exception("Post doesn't exist")
      }
    }
  }

}
