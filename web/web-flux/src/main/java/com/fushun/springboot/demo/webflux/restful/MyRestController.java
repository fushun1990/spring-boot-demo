package com.fushun.springboot.demo.webflux.restful;

public class MyRestController {

    public Mono<User> getUser(Long user) {
        return null;
    }

    public Flux<Customer> getUserCustomers(Long user) {
        return null;
    }

    public Mono<User> deleteUser(Long user) {
        return null;
    }
}
