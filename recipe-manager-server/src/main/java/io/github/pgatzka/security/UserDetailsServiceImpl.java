package io.github.pgatzka.security;

import io.github.pgatzka.data.pojo.AccountPojo;
import io.github.pgatzka.data.service.AccountDataService;
import io.github.pgatzka.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static io.github.pgatzka.jooq.Tables.ACCOUNT;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountDataService accountDataService;
    private final DSLContext dslContext;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        AccountPojo account = accountDataService.fetchOptional(ACCOUNT.EMAIL.equalIgnoreCase(username)).orElseThrow(() -> new NotFoundException("Email is not registered"));

        String password = dslContext.select(ACCOUNT.PASSWORD).from(ACCOUNT).where(ACCOUNT.ID.eq(account.getId())).fetchSingleInto(String.class);

        return new User(account.getEmail(), password, Collections.emptyList());
    }

}
