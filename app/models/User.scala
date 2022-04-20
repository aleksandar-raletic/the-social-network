package models

case class User(id: Int, password: String, firstName: String, lastName: String, email: String)

case class CreateUser(password: String, firstName: String, lastName: String, email: String)

case class ShowUser(id: Int, firstName: String, lastName: String, email: String)
