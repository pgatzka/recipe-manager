package io.github.pgatzka.rest.service;

import io.github.pgatzka.data.pojo.AccountPojo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthenticationService {

    AccountPojo register(String email, String password);

    String login(String email, String password);
}
