package repository

import com.google.inject.Inject
import models.Post
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class PostRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._

  val posts = TableQuery[PostTable]

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

  class PostTable(tag: Tag) extends Table[Post](tag, "post") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def dateTime = column[DateTime]("date_time")
    def title = column[String]("title")
    def text = column[String]("text")

    def * = (id, userId, dateTime, title, text) <> (Post.tupled, Post.unapply)

  }
}
