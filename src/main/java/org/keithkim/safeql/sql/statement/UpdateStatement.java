package org.keithkim.safeql.sql.statement;

import org.keithkim.safeql.sql.expression.Registry;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;

public interface UpdateStatement extends Statement {
    Logger log = org.slf4j.LoggerFactory.getLogger(UpdateStatement.class);

    default int execute() {
        String sql = sql();
        log.info("SQL: {}", sql);

        return Registry.using(emptyList(), handle -> handle.createUpdate(sql).execute());
    }

    default CompletableFuture<Integer> executeAsync() {
        return CompletableFuture.supplyAsync(this::execute);
    }
}
