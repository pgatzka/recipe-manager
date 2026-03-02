package io.github.pgatzka.data;

import lombok.RequiredArgsConstructor;
import org.jooq.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractDataService<R extends UpdatableRecord<R>, P extends AbstractPojo> {

    private final DSLContext dslContext;

    private final Table<R> table;

    private final TableField<R, UUID> idField;

    public boolean fetchExists(Condition condition) {
        return fetchExists(condition, dslContext);
    }

    public boolean fetchExists(Condition condition, DSLContext dslContext) {
        return dslContext.fetchExists(table, condition);
    }

    public abstract P insert(P pojo, DSLContext dslContext);

    public P insert(P pojo) {
        return insert(pojo, dslContext);
    }

    public P fetchSingle(Condition condition, DSLContext dslContext) {
        return map(dslContext.fetchSingle(table, condition), dslContext);
    }

    public P fetchSingle(Condition condition) {
        return fetchSingle(condition, dslContext);
    }

    public P fetchSingleById(UUID id) {
        return fetchSingleById(id, dslContext);
    }

    public P fetchSingleById(UUID id, DSLContext dslContext) {
        return fetchSingle(idField.eq(id), dslContext);
    }

    protected abstract P map(R tableRecord, DSLContext dslContext);

    protected P map(R tableRecord) {
        return map(tableRecord, dslContext);
    }

    public Optional<P> fetchOptional(Condition condition) {
        return fetchOptional(condition, dslContext);
    }

    public Optional<P> fetchOptional(Condition condition, DSLContext dslContext){
        return dslContext.fetchOptional(table, condition).map(this::map);
    }
}
