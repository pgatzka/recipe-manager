package io.github.pgatzka.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPojo {

    private final UUID id;

    private final OffsetDateTime createdAt;

    private final OffsetDateTime updatedAt;

    protected AbstractPojo() {
        this(null, null, null);
    }

}
