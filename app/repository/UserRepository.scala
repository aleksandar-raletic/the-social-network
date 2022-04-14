package repository

import com.google.inject.Inject
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  val users = TableQuery[UserTable]

  def add(user: User): Future[String] = {
    db.run(users += user)
      .map(res => "User successfully added").recover {
      case ex: Exception =>
        ex.getCause.getMessage
    }
  }

//  def add(user: User) = {
//    db.run(users returning users.map(_.id) += user)
//      .map(id => user.copy(id = Some(id)))
//  }

//  val forInsert = users returning users.map(_.id) into ((user, id) => user.copy(id = Some(id)))

  def delete(id: Int): Future[Int] = {
    db.run(users.filter(_.id === id).delete)
  }

  def get(id: Int): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    db.run(users.result)
  }

  def update(user: User): Future[Int] = {
    db.run(
      users
        .filter(_.id === user.id)
        .map(x => (x.password, x.firstName, x.lastName, x.email))
        .update(user.password, user.firstName, user.lastName, user.email)
    )
  }

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def password = column[String]("password")

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def email = column[String]("email")

    override def * = (id, password, firstName, lastName, email) <> (User.tupled, User.unapply)
  }
}
