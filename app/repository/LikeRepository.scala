package repository

import com.google.inject.Inject
import models.Like
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class LikeRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val likes = TableQuery[LikesTable]

  def addLike(like: Like): Future[String] = {
    db.run(likes += like).map(res => "Liked post").recover {
      case ex: Exception => ex.getMessage
    }
  }

//  def getLike(userId: Int): Future[Option[Like]] = {
//    db.run(likes.filter(_.userId === userId).result.headOption)
//  }

//  def getLikeDB(userId: Int, postId: Int): Future[Option[Like]] = {
//    db.run(likes.filter(_.userId === userId).result.headOption)
//    db.run(likes.filter(_.postId === postId).result.headOption)
//
//  }

  //db.run(likes.filter(_.userId === userId ).result.headOption)
  //db.run(likes.filter(x => (x.userId, x.postId)).result.headOption)
  //db.run(likes.filter(_.userId === userId).filter(_.postId === postId).result.headOption)
  //db.run(likes.filter(_.userId === userId).map(x => (x.userId, x.postId)).result.headOption)

  def getLike(userId: Int, postId: Int): Future[Option[Like]] = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).result.headOption)
  }

  def removeLike(userId: Int, postId: Int) = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).delete).map(res => "Unliked post").recover {
      case ex: Exception => ex.getMessage
    }
  }

  class LikesTable(tag: Tag) extends Table[Like](tag, "likes") {

    def userId = column[Int]("user_id")
    def postId = column[Int]("post_id")

    def * = (userId, postId) <> (Like.tupled, Like.unapply)
  }
}
