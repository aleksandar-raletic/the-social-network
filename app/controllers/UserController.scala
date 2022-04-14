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

//  def addUser() = Action.async(parse.json[CreateUser]) { request =>
//    val createUser: CreateUser = request.body
//    userService.addUser(createUser: CreateUser).map(message => Ok(message))
//  }

  def addUser() = Action.async(parse.json[CreateUser]) { request =>
    val createUser: CreateUser = request.body
    userService.addUser(createUser: CreateUser).map(message => Ok(message))
  }

  def updateUser() = Action.async(parse.json[User]) { request =>
    val user: User = request.body
    userService.update(user).map(message => Ok("User updated"))
  }


  def deleteUser(id: Int) = Action.async {
    userService.deleteUser(id).map(message => Ok("User deleted"))
  }

  def getUser(id: Int) = Action.async {
    userService
      .getUser(id)
      .map((maybeUser: Option[User]) => {
        if (maybeUser.isDefined) {
          Ok(Json.toJson(maybeUser.get))
        }else {
          NotFound
        }
      })
  }

  def listAllUsers = Action.async {
    userService.listAllUsers.map((users: Seq[User]) => {
      Ok(Json.toJson(users))
    })
  }
}
