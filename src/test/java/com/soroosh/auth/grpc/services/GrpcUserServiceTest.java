package com.soroosh.auth.grpc.services;

import com.soroosh.auth.BaseTest;
import com.soroosh.auth.grpc.dto.CreateUserRequest;
import com.soroosh.auth.grpc.dto.RequestUser;
import com.soroosh.auth.grpc.dto.User;
import com.soroosh.auth.user.models.UserDto;
import com.soroosh.auth.user.services.UserService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@SpringBootTest
@ActiveProfiles("testing")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GrpcUserServiceTest extends BaseTest {

    private GrpcUserService grpcUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() {
        grpcUserService = new GrpcUserService(userService, validator);
    }

    @Test
    void test_CreateUser_Success() {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("soroosh@gmail.com")
                .setMobile("+989378889999")
                .setPassword(this.faker.internet().password())
                .build();

        StreamObserver<User> responseObserver = mock(StreamObserver.class);

        this.grpcUserService.createUser(request, responseObserver);

        verify(responseObserver, times(1)).onNext(any(User.class));
        verify(responseObserver, times(1)).onCompleted();
        verify(responseObserver, never()).onError(any(StatusRuntimeException.class));
        Assertions.assertTrue(this.userService.findByEmail(request.getEmail()).isPresent());
    }

    @Test
    void test_CreateUser_InvalidInput() {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("")
                .setLastName("Doe")
                .setEmail("invalid-email")
                .setMobile("1234567890")
                .setPassword("password")
                .build();
        StreamObserver<User> responseObserver = mock(StreamObserver.class);

        grpcUserService.createUser(request, responseObserver);

        verify(responseObserver, never()).onNext(any(User.class));
        verify(responseObserver, times(1)).onError(argThat(statusRuntimeException -> ((StatusRuntimeException) statusRuntimeException).getStatus().getCode() == Status.Code.INVALID_ARGUMENT));
    }

    @Test
    void test_createUser_duplicateEmail() {
        String email = "soroosh@gmail.com";
        this.userService.createUser(UserDto.builder()
                .email(email)
                .firstName(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .mobile("+989378889696")
                .password("random").build());
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail(email)
                .setMobile("1234567890")
                .setPassword("password")
                .build();

        StreamObserver<User> responseObserver = mock(StreamObserver.class);

        grpcUserService.createUser(request, responseObserver);

        verify(responseObserver, never()).onNext(any(User.class));
        verify(responseObserver, never()).onCompleted();
        verify(responseObserver, times(1)).onError(argThat(statusRuntimeException -> ((StatusRuntimeException) statusRuntimeException).getStatus().getCode() == Status.Code.INVALID_ARGUMENT));
    }

    @Test
    void test_createUser_duplicateMobile() {
        String mobile = "+989378889696";
        this.userService.createUser(UserDto.builder().email(this.faker.internet().emailAddress()).firstName(this.faker.name()
                .firstName()).lastName(this.faker.name().lastName()).mobile(mobile).password("random").build());
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("soroosh@gmail.com")
                .setMobile(mobile)
                .setPassword("password")
                .build();

        StreamObserver<User> responseObserver = mock(StreamObserver.class);

        grpcUserService.createUser(request, responseObserver);

        verify(responseObserver, never()).onNext(any(User.class));
        verify(responseObserver, never()).onCompleted();
        verify(responseObserver, times(1)).onError(argThat(statusRuntimeException -> ((StatusRuntimeException) statusRuntimeException).getStatus().getCode() == Status.Code.INVALID_ARGUMENT));
    }

    @Test
    void testGetUserInfo_UserNotFound() {
        RequestUser request = RequestUser.newBuilder()
                .setUserId(123)
                .build();

        StreamObserver<User> responseObserver = mock(StreamObserver.class);

        grpcUserService.getUserInfo(request, responseObserver);

        verify(responseObserver, never()).onNext(any(User.class));
        verify(responseObserver, never()).onCompleted();
        verify(responseObserver, times(1)).onError(argThat(statusRuntimeException -> ((StatusRuntimeException) statusRuntimeException).getStatus().getCode() == Status.Code.NOT_FOUND));
    }

}