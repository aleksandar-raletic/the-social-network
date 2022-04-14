package services

import models.{User, CreateUser}
import repository.UserRepository
import javax.inject.Inject
import scala.concurrent.Future

class UserService @Inject()(userRepository: UserRepository) {

//  def addUser(user: User): Future[User] = {
//    userRepository.add(user)
//  }

  def addUser(createUser: CreateUser): Future[String] = {
    val user = User(
      id = 0,
      password = createUser.password,
      firstName = createUser.firstName,
      lastName = createUser.lastName,
      email = createUser.email
    )
    userRepository.add(user)
  }

  def deleteUser(id: Int): Future[Int] = {
    userRepository.delete(id)
  }

  def getUser(id: Int): Future[Option[User]] = {
    userRepository.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    userRepository.listAll
  }

  def update(user: User): Future[Int] = {
    userRepository.update(user)
  }
}
