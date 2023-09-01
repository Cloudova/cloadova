package com.soroosh.auth;

import com.soroosh.auth.commons.Env;
import com.soroosh.auth.grpc.auth.AuthServerInterceptor;
import com.soroosh.auth.grpc.services.GrpcUserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GRPCRunner implements CommandLineRunner {

    @Value("${grpc.port}")
    private int port;

    private final GrpcUserService userService;
    private final AuthServerInterceptor authServerInterceptor;

    private final Env env;

    public GRPCRunner(GrpcUserService userService, AuthServerInterceptor authServerInterceptor, Env env) {
        this.userService = userService;
        this.authServerInterceptor = authServerInterceptor;
        this.env = env;
    }

    @Override
    public void run(String... args) throws RuntimeException {
        Thread grpc = new Thread(() -> {
            Server server = ServerBuilder
                    .forPort(this.port)
                    .intercept(this.authServerInterceptor)
                    .addService(this.userService).build();

            try {
                if (!this.env.isTestingMode()) {
                    server.start();
                    server.awaitTermination();
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        grpc.start();
    }
}
