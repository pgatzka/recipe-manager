package io.github.pgatzka.data.service;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AccountDataServiceTest {

    @Mock
    private DSLContext dslContext;

    @InjectMocks
    private AccountDataService accountDataService;

    @Test
    public void test(){
        log.error("test");
    }

}