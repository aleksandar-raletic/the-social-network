package repository

import com.google.inject.Inject
import models.{CreatePost, Post}
import org.joda.time.{DateTime, DateTimeZone}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile
import slick.sql.SqlProfile.ColumnOption.SqlType

class PostRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._

  val posts = TableQuery[PostTable]

//  def insert(p: PostWithoutDate): DBIO[Int] =
//    sqlu"INSERT INTO post (id, user_id, text, title, date_time) VALUES (${p.id}, ${p.userId}, ${p.text}, ${p.title}, $Calendar )"

  //radi
  def add(post: Post): Future[String] = {
    db.run(posts += post).map(res => "Post submitted").recover {
      case ex: Exception => ex.getMessage
    }
  }

//    def add(post: Post) = {
//      db.run(posts returning posts.map(_.id) += post)
//    }

//  def add(post: Post) = {
//    val postWithId = {
//      (posts returning posts.map(_.id)) += Post(post.userId, post.id, post.dateTime, post.text, post.text)
//      db.run(postWithId)
//    }
//  }

//def add(post: Post) = {
//  val postWithId =
//    (posts returning posts.map(_.id)
//      into ((post,id) => post.copy(id=(id)))
//      ) += Post(post.id, post.userId, post.dateTime, post.title, post.text)
//}



  def get(id: Int): Future[Option[Post]]={
    db.run(posts.filter(_.id === id).result.headOption)
  }

  def delete(id: Int): Future[Int] = {
    db.run(posts.filter(_.id === id).delete)
  }

  def update(post: Post): Future[Int] = {
    db.run(
      posts
        .filter(_.id === post.id)
        .update(post)
    )
}

//  def update(post: Post): Future[Int] = {
//    db.run(
//      posts
//        .filter(_.id === post.id)
//        .map(p => (p.userId, p.dateTime, p.text, p.title,))
//        .update(post.userId, post.dateTime, post.text, post.title)
//    )
//  }

  //radi
//  def update(post: Post): Future[Int] = {
//    db.run(
//      posts
//        .filter(_.id === post.id)
//        .map(p => (p.title, p.text))
//        .update(post.title, post.text)
//    )
//  }

//  def findPostinDb(post: Option[Post]) ={
//    if (post.isDefined){
//      val post: Post = db.run(
//            posts
//              .filter(_.id === post.id)
//              .map(p => (p.title, p.text))
//              .update(post.title, post.text)
//          )
//    }
//  }

  //  def add(post: Post) = {
  //    db.run(posts returning posts.map(_.id) += post)
  //      .map(id => post.copy(id = Some(id)))
  //  }


  class PostTable(tag: Tag) extends Table[Post](tag, "post") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def userId = column[Int]("user_id")
    def dateTime = column[DateTime]("date_time")
    def title = column[String]("title")
    def text = column[String]("text")

    override def * = (id, userId, dateTime, title, text) <> (Post.tupled, Post.unapply)

  }
}
