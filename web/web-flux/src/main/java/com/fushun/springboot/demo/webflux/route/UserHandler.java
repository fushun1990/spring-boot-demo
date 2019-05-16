package com.fushun.springboot.demo.webflux.route;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class UserHandler {

    public Mono<ServerResponse> getUser(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> getUserCustomers(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        return null;
    }
}
