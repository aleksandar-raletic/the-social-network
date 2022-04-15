package controllers

import models.Formats.{createUserFormat, userFormat}
import models.{CreateUser, User}
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc._
import services.UserService
import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents, userService: UserService)(implicit executionContext: ExecutionContext)
    extends AbstractController(cc)
    with Logging {

  def addUser() = Action.async(parse.json[CreateUser]) { request =>
    val createUser: CreateUser = request.body
    userService
      .addUser(createUser: CreateUser)
      .map(addedUser => Ok(Json.toJson(addedUser)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def getUser(id: Int) = Action.async {
    userService
      .getUser(id)
      .map((retrievedUser: Option[User]) => {
        Ok(Json.toJson(retrievedUser.get))
      })
      .recover(exception => BadRequest(exception.getMessage))
  }

  def updateUser() = Action.async(parse.json[User]) { request =>
    val user: User = request.body
    userService
      .updateUser(user)
      .map(updatedUser => Ok(Json.toJson(user)))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def deleteUser(id: Int) = Action.async {
    userService
      .deleteUser(id)
      .map(deletedUser => Ok("User deleted"))
      .recover(exception => BadRequest(exception.getMessage))
  }

  def listAllUsers = Action.async {
    userService.listAllUsers
      .map((users: Seq[User]) => {
        Ok(Json.toJson(users))
      })
      .recover(exception => BadRequest(exception.getMessage))
  }

}
