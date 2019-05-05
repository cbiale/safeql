package org.keithkim.safeql.sql.expression;

import com.google.common.base.Joiner;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.keithkim.safeql.sql.expression.Helpers.group;

public class NAryExpr<T> extends Expr<T> {
    private final Expr<T> identity;
    private final Expr<T>[] exprs;

    public NAryExpr(String operator, Expr<T> identity, Expr<T>... exprs) {
        super(operator);
        this.identity = identity;
        this.exprs = exprs;
    }

    public String sql() {
        if (exprs.length == 0) {
            return identity.sql();
        } else if (exprs.length == 1) {
            return exprs[0].sql();
        }
        String operator = super.sql();

        List<String> terms = asList(exprs).stream().map(expr -> group(expr.sql())).collect(toList());

        if (operator.endsWith("()")) {
            String arguments = Joiner.on(", ").join(terms);
            return operator.substring(0, operator.length() - 1) + arguments +")";
        } else {
            return Joiner.on(" "+operator+" ").join(terms);
        }
    }
}