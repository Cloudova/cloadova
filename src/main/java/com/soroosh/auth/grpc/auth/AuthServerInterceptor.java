package com.soroosh.auth.grpc.auth;

import io.grpc.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthServerInterceptor implements ServerInterceptor {

    @Value("${grpc.service-access-token}")
    private String serviceAccessToken;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String accessTokens = headers.get(Metadata.Key.of("access_token", Metadata.ASCII_STRING_MARSHALLER));
        if (accessTokens == null) {
            return failedAuth(call);
        }else if(!accessTokens.equals(serviceAccessToken)){
            return failedAuth(call);
        }

        return next.startCall(call, headers);
    }

    private static <ReqT, RespT> ServerCall.Listener<ReqT> failedAuth(ServerCall<ReqT, RespT> call) {
        Status status = Status.UNAUTHENTICATED.withDescription("authentication token is invalid");
        call.close(status, new Metadata());
        return new ServerCall.Listener<>() {
        };
    }
}
