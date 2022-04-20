package dto

case class FriendRequestsWithUsersDto(
    id: Int,
    firstName: String,
    lastName: String,
    email: String,
    requestFrom: Int,
    requestTo: Int,
    status: String
)
