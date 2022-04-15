package services

import models.{CreateUser, User}
import repository.UserRepository
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()(userRepository: UserRepository)(implicit executionContext: ExecutionContext) {

  def addUser(createUser: CreateUser): Future[User] = {
    val user = User(
      id = 0,
      password = createUser.password,
      firstName = createUser.firstName,
      lastName = createUser.lastName,
      email = createUser.email
    )
    userRepository.addUser(user)
  }

  def getUser(id: Int): Future[Option[User]] = {
    userRepository.getUser(id).flatMap {
      case Some(_) => userRepository.getUser(id)
      case None    => throw new Exception("User with specified id doesn't exist")
    }
  }

  def updateUser(user: User): Future[Int] = {
    userRepository.getUser(user.id).flatMap {
      case Some(_) => userRepository.updateUser(user)
      case None    => throw new Exception("User with specified id doesn't exist")
    }
  }

  def deleteUser(id: Int): Future[Int] = {
    userRepository.getUser(id).flatMap {
      case Some(_) => userRepository.deleteUser(id)
      case None    => throw new Exception("User with specified id doesn't exist")
    }
  }

  def listAllUsers: Future[Seq[User]] = {
    userRepository.listAllUsers
  }

}
