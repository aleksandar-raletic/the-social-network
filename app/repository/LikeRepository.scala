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

  def addLike(like: Like) = {
    db.run(likes += like)
  }

  def getLike(userId: Int, postId: Int): Future[Option[Like]] = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).result.headOption)
  }

  def removeLike(userId: Int, postId: Int) = {
    db.run(likes.filter(like => like.userId === userId && like.postId === postId).delete)
  }

  class LikesTable(tag: Tag) extends Table[Like](tag, "likes") {
    def userId = column[Int]("user_id")
    def postId = column[Int]("post_id")

    def * = (userId, postId) <> (Like.tupled, Like.unapply)
  }

  //val q1 = likes.length

//  def countRow: DBIO[Int] =
//    sqlu"""SELECT COUNT(post_id) FROM likes WHERE post_id = 4"""

//  def randomMethod(postId: Int): Future[Seq[Int]] = {
//    db.run(sql"""SELECT COUNT(post_id) FROM likes WHERE post_id = postId""".as(Int))
//  }

  def countLikesForPost(postId: Int): Future[Option[Int]] = {
    db.run(sql"""SELECT COUNT(post_id) FROM likes WHERE post_id = $postId""".as[Int]).map(counts => counts.headOption)
  }
}
