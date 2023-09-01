package com.soroosh.auth.grpc.services;

import com.soroosh.auth.grpc.dto.CreateUserRequest;
import com.soroosh.auth.user.models.User;
import com.soroosh.auth.user.models.UserDto;
import com.soroosh.auth.user.services.UserService;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GrpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final com.soroosh.auth.user.services.UserService userService;
    private final Validator validator;

    @Autowired
    public GrpcUserService(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }


    @Override
    public void createUser(CreateUserRequest request, StreamObserver<com.soroosh.auth.grpc.dto.User> responseObserver) {
        var dto = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .password(request.getPassword()).build();
        Set<ConstraintViolation<UserDto>> violations = this.validator.validate(dto);
        if (!violations.isEmpty()) {
            Metadata m = new Metadata();
            violations.forEach(v -> m.put(Metadata.Key.of(v.getPropertyPath().toString(), Metadata.ASCII_STRING_MARSHALLER), v.getMessage()));
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid input")
                    .asRuntimeException(m));
            return;
        }

        if (this.userService.findByEmail(dto.email()).isPresent()) {
            Metadata m = new Metadata();
            m.put(Metadata.Key.of("email", Metadata.ASCII_STRING_MARSHALLER), "email is duplicated");
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid input")
                    .asRuntimeException(m));
            return;
        }

        if (this.userService.findByMobile(dto.mobile()).isPresent()) {
            Metadata m = new Metadata();
            m.put(Metadata.Key.of("mobile", Metadata.ASCII_STRING_MARSHALLER), "mobile is duplicated");
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Invalid input")
                    .asRuntimeException(m));
            return;
        }

        User user = this.userService.createUser(dto);
        responseObserver.onNext(com.soroosh.auth.grpc.dto.User.newBuilder()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setMobile(user.getMobile())
                .setEmail(user.getEmail())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserInfo(com.soroosh.auth.grpc.dto.RequestUser request,
                            io.grpc.stub.StreamObserver<com.soroosh.auth.grpc.dto.User> responseObserver) {
        User foundedUser;
        try {
            foundedUser = this.userService.findById(request.getUserId());
        } catch (UsernameNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
            return;
        }

        com.soroosh.auth.grpc.dto.User user = com.soroosh.auth.grpc.dto.User.newBuilder().
                setId(foundedUser.getId())
                .setFirstName(foundedUser.getFirstName())
                .setLastName(foundedUser.getLastName())
                .setMobile(foundedUser.getMobile())
                .setEmail(foundedUser.getEmail())
                .build();
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

}
