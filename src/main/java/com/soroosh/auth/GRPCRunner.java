package com.soroosh.auth;

import com.soroosh.auth.grpc.services.GrpcUserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GRPCRunner implements CommandLineRunner {

    private final GrpcUserService userService;

    public GRPCRunner(GrpcUserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws RuntimeException {
        Thread grpc = new Thread(() -> {
            Server server = ServerBuilder
                    .forPort(8096)
                    .addService(this.userService).build();

            try {
                server.start();
                server.awaitTermination();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        grpc.start();
    }
}
