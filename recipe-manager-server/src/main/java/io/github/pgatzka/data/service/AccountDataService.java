package io.github.pgatzka.data.service;

import io.github.pgatzka.data.AbstractDataService;
import io.github.pgatzka.data.pojo.AccountPojo;
import io.github.pgatzka.jooq.tables.records.AccountRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static io.github.pgatzka.jooq.Tables.ACCOUNT;

@Service
public class AccountDataService extends AbstractDataService<AccountRecord, AccountPojo> {

    public AccountDataService(DSLContext dslContext) {
        super(dslContext, ACCOUNT, ACCOUNT.ID);
    }

    @Override
    public AccountPojo insert(AccountPojo pojo, DSLContext dslContext) {
        UUID id = dslContext.insertInto(ACCOUNT)
                .set(ACCOUNT.EMAIL, pojo.getEmail())
                .set(ACCOUNT.PASSWORD, pojo.getPassword())
                .returningResult(ACCOUNT.ID).fetchSingleInto(UUID.class);

        return fetchSingleById(id, dslContext);
    }

    @Override
    protected AccountPojo map(AccountRecord tableRecord, DSLContext dslContext) {
        AccountPojo pojo = new AccountPojo(tableRecord.getId(), tableRecord.getCreatedAt(), tableRecord.getUpdatedAt());
        pojo.setEmail(tableRecord.getEmail());
        return pojo;
    }

}
