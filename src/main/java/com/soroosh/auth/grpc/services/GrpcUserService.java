package com.soroosh.auth.grpc.services;

import com.soroosh.auth.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GrpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final com.soroosh.auth.user.services.UserService userService;

    @Autowired
    public GrpcUserService(com.soroosh.auth.user.services.UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUserInfo(com.soroosh.auth.grpc.dto.RequestUser request,
                            io.grpc.stub.StreamObserver<com.soroosh.auth.grpc.dto.User> responseObserver) {
        User foundedUser = this.userService.findById(request.getUserId());
        com.soroosh.auth.grpc.dto.User user = com.soroosh.auth.grpc.dto.User.newBuilder().
                setId(foundedUser.getId())
                .setFirstName(foundedUser.getFirstName())
                .setLastName(foundedUser.getLastName())
                .setMobile(foundedUser.getMobile())
                .build();

        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

}
