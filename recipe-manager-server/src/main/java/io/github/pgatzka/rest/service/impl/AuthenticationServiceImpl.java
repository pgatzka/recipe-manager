package io.github.pgatzka.rest.service.impl;

import io.github.pgatzka.data.pojo.AccountPojo;
import io.github.pgatzka.data.service.AccountDataService;
import io.github.pgatzka.exception.ConflictException;
import io.github.pgatzka.exception.UnauthorizedException;
import io.github.pgatzka.rest.service.AuthenticationService;
import io.github.pgatzka.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static io.github.pgatzka.jooq.Tables.ACCOUNT;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountDataService accountDataService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jWTService;

    @Override
    public AccountPojo register(String email, String password) {
        if (accountDataService.fetchExists(ACCOUNT.EMAIL.equalIgnoreCase(email))) {
            throw new ConflictException("Email is already registered");
        }

        AccountPojo account = new AccountPojo();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));

        return accountDataService.insert(account);
    }

    @Override
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if (authentication.isAuthenticated()){
            return jWTService.generateToken(email);
        }

        throw new UnauthorizedException("Login failed");
    }

}
