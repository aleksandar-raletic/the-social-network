package repository

import com.google.inject.Inject
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, slickTables: SlickTables)(
    implicit executionContext: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._
  import slickTables._

  def addUser(user: User): Future[User] = {
    db.run((users returning users.map(_.id)) += user)
      .map(generatedId => user.copy(id = generatedId))
  }

  def getUser(id: Int): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  def updateUser(user: User): Future[Int] = {
    db.run(
      users
        .filter(_.id === user.id)
        .map(user => (user.password, user.firstName, user.lastName, user.email))
        .update(user.password, user.firstName, user.lastName, user.email)
    )
  }

  def deleteUser(id: Int): Future[Int] = {
    db.run(users.filter(_.id === id).delete)
  }

  def listAllUsers: Future[Seq[User]] = {
    db.run(users.result)
  }

}
