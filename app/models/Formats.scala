package models

import play.api.libs.json.Json

object Formats {
  implicit val userFormat = Json.format[User]
}
