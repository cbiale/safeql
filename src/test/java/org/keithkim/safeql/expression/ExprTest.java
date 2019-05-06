package org.keithkim.safeql.expression;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.SortedMap;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class ExprTest {
    @Test
    void nobinds_sql_returnsLiteral() {
        Expr<String> subject = new Expr<>("SELECT * FROM account");

        assertEquals("SELECT * FROM account", subject.sql());
    }

    @Test
    void withBinds_sql_returnsLiteral() {
        Expr<String> subject = new Expr<>("SELECT * FROM account WHERE id BETWEEN :min_id AND :max_id LIMIT :limit OFFSET :offset");
        subject.bind("min_id", 1000);
        subject.bind("max_id", 2000);
        subject.bind("limit", 10);
        subject.bind("offset", 5);

        assertEquals("SELECT * FROM account WHERE id BETWEEN :min_id AND :max_id LIMIT :limit OFFSET :offset", subject.sql());
    }

    @Test
    void nobinds_binds_returnsEmptyMap() {
        Expr<String> subject = new Expr<>("SELECT * FROM account");

        assertEquals(emptyMap(), subject.binds());
    }

    @Test
    void withMultiplebinds_binds_returnsSortedMapWithAllbinds() {
        Expr<String> subject = new Expr<>("SELECT * FROM account WHERE id BETWEEN :min_id AND :max_id LIMIT :limit OFFSET :offset");
        subject.bind("min_id", 1000);
        subject.bind("max_id", 2000);
        subject.bind("limit", 10);
        subject.bind("offset", 5);

        assertTrue(subject.binds() instanceof SortedMap);
        assertEquals(ImmutableMap.of("min_id", 1000, "max_id", 2000, "limit", 10, "offset", 5), subject.binds());
    }

    @Test
    void nobinds_toString_shouldFormatInAngleQuotes() {
        Expr<String> subject = new Expr<>("SELECT * FROM account");

        assertEquals("<SQL: SELECT * FROM account;>", subject.toString());
    }

    @Test
    void withOneBinding_toString_shouldFormatInAngleQuotes() {
        Expr<String> subject = new Expr<>("SELECT * FROM account WHERE id >= :min_id");
        subject.bind("min_id", 1000);

        assertEquals("<SQL: SELECT * FROM account WHERE id >= :min_id; BIND: min_id:1000>", subject.toString());
    }

    @Test
    void withMultiplebinds_toString_shouldFormatSortedInAngleQuotes() {
        Expr<String> subject = new Expr<>("SELECT * FROM account WHERE id BETWEEN :min_id AND :max_id LIMIT :limit OFFSET :offset");
        subject.bind("min_id", 1000);
        subject.bind("max_id", 2000);
        subject.bind("limit", 10);
        subject.bind("offset", 5);

        assertEquals("<SQL: SELECT * FROM account WHERE id BETWEEN :min_id AND :max_id LIMIT :limit OFFSET :offset;"+
                " BIND: limit:10, max_id:2000, min_id:1000, offset:5>", subject.toString());
    }
}