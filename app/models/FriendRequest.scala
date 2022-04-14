package models

case class FriendRequest(friendRequestId: Int, requestFrom: Int, requestTo: Int, status: String)

case class CreateFriendRequest(requestFrom: Int, requestTo: Int)

case class AcceptDeclineFriendRequest(friendRequestId: Int)
