package org.keithkim.safeql.predicate;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Not extends Predicate {
    private final Predicate predicate;

    public Not(Predicate predicate) {
        super("NOT");
        this.predicate = predicate;
    }

    public String sql() {
        if (predicate.isKnownFalse()) {
            return Predicates.TRUE.sql();
        }
        if (predicate.isKnownTrue()) {
            return Predicates.FALSE.sql();
        }
        return "NOT " + group(predicate.sql());
    }
}
