package repository

import com.google.inject.Inject
import models.{Post, ShowPost}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{ExecutionContext, Future}

class PostRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, slickTables: SlickTables)(
    implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import slickTables._

  def addPost(post: Post) = {
    db.run(posts returning posts.map(_.id) += post)
      .map(generatedId => post.copy(id = generatedId))
  }

  def getPost(id: Int): Future[Option[Post]] = {
    db.run(posts.filter(_.id === id).result.headOption)
  }

  def updatePost(post: Post): Future[Int] = {
    db.run(
      posts
        .filter(_.id === post.id)
        .update(post)
    )
  }

  def deletePost(id: Int) = {
    db.run(posts.filter(_.id === id).delete)
  }

  def countLikesForPost(id: Int): Future[Option[Int]] = {
    db.run(sql"""SELECT COUNT(id) FROM post JOIN likes ON post.id=likes.post_id WHERE post_id = $id""".as[Int])
      .map(counts => counts.headOption)
  }

  import com.github.tototoshi.slick.MySQLJodaSupport._
  implicit val ListPostsForUserResult = GetResult(r => ShowPost(r.<<, r.<<, r.<<, r.<<))
  def listPostsForUser(userId: Int) = {
    db.run(sql"""SELECT id, date_time, title, text FROM post WHERE user_id = $userId ORDER BY date_time""".as[ShowPost])
  }

}
