package io.github.pgatzka.rest.controller.impl;

import io.github.pgatzka.data.pojo.AccountPojo;
import io.github.pgatzka.rest.controller.AuthenticationController;
import io.github.pgatzka.rest.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService service;

    @Override
    public ResponseEntity<AccountPojo> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(service.register(request.email(), request.password()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(service.login(request.email(), request.password()), HttpStatus.OK);
    }


}
