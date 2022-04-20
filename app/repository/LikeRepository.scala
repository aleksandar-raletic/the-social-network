package repository

import com.google.inject.Inject
import models.Like
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class LikeRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, slickTables: SlickTables)(
    implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import slickTables._

  def addLike(like: Like) = {
    db.run(likes += like)
  }

  def getLike(userId: Int, postId: Int): Future[Option[Like]] = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).result.headOption)
  }

  def removeLike(userId: Int, postId: Int) = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).delete)
  }
}
