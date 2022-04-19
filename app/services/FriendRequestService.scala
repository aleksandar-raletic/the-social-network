package services

import models.{CreateFriendRequest, AcceptDeclineFriendRequest, FriendRequest}
import repository.FriendRequestRepository
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FriendRequestService @Inject()(protected val friendRequestRepository: FriendRequestRepository)(
    implicit executionContext: ExecutionContext
) {

  def sendFriendRequest(createFriendRequest: CreateFriendRequest) = {
    friendRequestRepository.getFriendRequest(createFriendRequest.requestFrom, createFriendRequest.requestTo).flatMap {
      case Some(_) => throw new Exception("Friend request already sent")
      case None =>
        if (createFriendRequest.requestFrom == createFriendRequest.requestTo) {
          throw new Exception("Can't send friend request to yourself")
        } else {
          val friendRequest: FriendRequest = FriendRequest(
            friendRequestId = 0,
            requestFrom = createFriendRequest.requestFrom,
            requestTo = createFriendRequest.requestTo,
            status = "Pending"
          )
          friendRequestRepository.sendFriendRequest(friendRequest)
        }
    }
  }

  def acceptFriendRequest(acceptDeclineFriendRequest: AcceptDeclineFriendRequest) = {
    friendRequestRepository.getId(acceptDeclineFriendRequest.friendRequestId).flatMap {
      case Some(friendRequest) =>
        if (friendRequest.status == "Friend request accepted") {
          throw new Exception("Friend request already accepted")
        } else {
          val updatedFriendRequest = friendRequest.copy(
            friendRequestId = friendRequest.friendRequestId,
            requestFrom = friendRequest.requestFrom,
            requestTo = friendRequest.requestTo,
            status = "Friend request accepted"
          )
          friendRequestRepository.acceptFriendRequest(updatedFriendRequest)
        }
      case None => throw new Exception("Wrong id request or it doesn't exist")
    }
  }

  def declineFriendRequest(acceptDeclineFriendRequest: AcceptDeclineFriendRequest) = {
    friendRequestRepository.getId(acceptDeclineFriendRequest.friendRequestId).flatMap {
      case Some(friendRequest) =>
        if (friendRequest.status == "Pending") {
          friendRequestRepository.declineFriendRequest(acceptDeclineFriendRequest.friendRequestId)
        } else {
          throw new Exception("Can't decline already accepted friend request")
        }
      case None => throw new Exception("Wrong id request or it doesn't exist")
    }
  }

  def getFriendRequest(requestFrom: Int, requestTo: Int): Future[Option[FriendRequest]] = {
    friendRequestRepository.getFriendRequest(requestFrom, requestTo)
  }

  def getId(friendRequestId: Int) = {
    friendRequestRepository.getId(friendRequestId)
  }

//  def sumNumberOfFriends(userId: Int) = {
//    friendRequestRepository.sumNumberOfFriends(userId)
//  }
//
//  def numberOfFriends(userId: Int) = {
//    friendRequestRepository.getId(userId).flatMap {
//      case Some(friendrequest) =>
//        if (friendrequest.requestFrom == userId) {
//          //
//        }
//    }
//  }

}
