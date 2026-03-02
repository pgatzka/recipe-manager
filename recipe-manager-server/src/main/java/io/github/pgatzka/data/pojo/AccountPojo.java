package io.github.pgatzka.data.pojo;

import io.github.pgatzka.data.AbstractPojo;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class AccountPojo extends AbstractPojo {

    private String email;

    private String password;

    public AccountPojo() {
        super();
    }

    public AccountPojo(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        super(id, createdAt, updatedAt);
    }

}
