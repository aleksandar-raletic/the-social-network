package services
import com.google.inject.Inject
import models.User
import repository.UserRepository

import scala.concurrent.Future

class UserService @Inject()(userRepository: UserRepository) {

  def addUser(user: User): Future[String] = {
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
