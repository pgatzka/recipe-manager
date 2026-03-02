package io.github.pgatzka.rest.controller;

import io.github.pgatzka.data.pojo.AccountPojo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationController {

    @PostMapping("/register")
    ResponseEntity<AccountPojo> register(@Valid @RequestBody RegisterRequest request);

    @PostMapping("/login")
    ResponseEntity<String> login(@Valid @RequestBody LoginRequest request);

    record RegisterRequest(@NotBlank @Email String email, @NotBlank String password) {

    }

    record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {

    }

}
