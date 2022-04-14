package services

import models.{CreateFriendRequest, AcceptDeclineFriendRequest, FriendRequest}
import repository.FriendRequestRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FriendRequestService @Inject()(protected val friendRequestRepository: FriendRequestRepository)(
    implicit executionContext: ExecutionContext
) {

  //radi
//  def sendFriendRequest(createFriendRequest: CreateFriendRequest) = {
//    val friendRequest: FriendRequest = FriendRequest(
//      friendRequestId = 0,
//      requestFrom = createFriendRequest.requestFrom,
//      requestTo = createFriendRequest.requestTo,
//      status = "Pending"
//    )
//    friendRequestRepository.sendFriendRequest(friendRequest)
//  }

  //radi
//  def sendFriendRequest(createFriendRequest: CreateFriendRequest) = {
//    getFriendRequest(createFriendRequest.requestFrom, createFriendRequest.requestTo).flatMap {
//      case Some(_) => throw new Exception("Friend request already sent")
//      case None =>
//        val friendRequest: FriendRequest = FriendRequest(
//          friendRequestId = 0,
//          requestFrom = createFriendRequest.requestFrom,
//          requestTo = createFriendRequest.requestTo,
//          status = "Pending"
//        )
//        friendRequestRepository.sendFriendRequest(friendRequest)
//    }
//  }

  //radi
//  def sendFriendRequest(createFriendRequest: CreateFriendRequest) = {
//    getFriendRequest(createFriendRequest.requestFrom, createFriendRequest.requestTo).flatMap {
//      case Some(_) => throw new Exception("Friend request already sent")
//      case None =>
//        val x: Int = createFriendRequest.requestFrom
//        val y: Int = createFriendRequest.requestTo
//        getFriendRequest(y, x).flatMap {
//          case Some(_) => throw new Exception("Friend request already sent TEST")
//          case None =>
//            val friendRequest: FriendRequest = FriendRequest(
//              friendRequestId = 0,
//              requestFrom = createFriendRequest.requestFrom,
//              requestTo = createFriendRequest.requestTo,
//              status = "Pending"
//            )
//            friendRequestRepository.sendFriendRequest(friendRequest)
//        }
//    }
//  }

  def sendFriendRequest(createFriendRequest: CreateFriendRequest) = {
    getFriendRequest(createFriendRequest.requestFrom, createFriendRequest.requestTo).flatMap {
      case Some(_) => throw new Exception("Friend request already sent")
      case None =>
        if (createFriendRequest.requestFrom == createFriendRequest.requestTo) {
          throw new Exception("Error")
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

  def getFriendRequest(requestFrom: Int, requestTo: Int): Future[Option[FriendRequest]] = {
    friendRequestRepository.getFriendRequest(requestFrom, requestTo)
  }

  def getId(friendRequestId: Int) = {
    friendRequestRepository.getId(friendRequestId)
  }

//  def acceptFriendRequest(acceptFriendRequest: AcceptFriendRequest) = {
//    val friendRequest: FriendRequest = FriendRequest(
//      friendRequestId = acceptFriendRequest.friendRequestId,
//      requestFrom = ???,
//      requestTo = ???,
//      status = "Accepted"
//    )
//    friendRequestRepository.acceptFriendRequest(friendRequest)
//  }

//  def acceptFriendRequest(acceptFriendRequest: AcceptFriendRequest) = {
//    getId(acceptFriendRequest.friendRequestId).flatMap { maybeFriendRequest =>
//      if (maybeFriendRequest.isDefined) {
//        val friendRequest = maybeFriendRequest.get
//        val updatedFriendRequest = friendRequest.copy(friendRequest.requestTo, friendRequest.requestTo, friendRequest.status)
//        friendRequestRepository.acceptFriendRequest(updatedFriendRequest).map(_ => updatedFriendRequest)
//      } else {
//        throw new Exception("Test test")
//      }
//    }
//  }

  def acceptFriendRequest(acceptDeclineFriendRequest: AcceptDeclineFriendRequest) = {
    getId(acceptDeclineFriendRequest.friendRequestId).flatMap { maybeFriendRequest =>
      if (maybeFriendRequest.isDefined) {
        val friendRequest: FriendRequest = maybeFriendRequest.get
        val updatedFriendRequest = friendRequest.copy(
          friendRequestId = friendRequest.friendRequestId,
//        acceptFriendRequest.friendRequestId,
          requestFrom = friendRequest.requestFrom,
          requestTo = friendRequest.requestTo,
          status = "Friend request accepted"
        )
        friendRequestRepository.acceptFriendRequest(updatedFriendRequest)
      } else {
        throw new Exception("Error")
      }
    }
  }

  def declineFriendRequest(acceptDeclineFriendRequest: AcceptDeclineFriendRequest) = {
    getId(acceptDeclineFriendRequest.friendRequestId).flatMap {
      case Some(_) => friendRequestRepository.declineFriendRequest(acceptDeclineFriendRequest.friendRequestId)
      case None    => throw new Exception("Error")
    }
  }

}
