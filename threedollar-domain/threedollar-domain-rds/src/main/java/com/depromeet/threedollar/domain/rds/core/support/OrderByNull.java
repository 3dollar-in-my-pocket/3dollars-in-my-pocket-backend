package com.depromeet.threedollar.domain.rds.core.support;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

/**
 * MySQL 8.0 이전 버전 or MariaDB에서는 GROUP BY시 묵시적으로 해당 필드로 ORDER BY가 수행
 * MySQL 8.0부터 묵시적으로 ORDER BY 수행하지 않도록 개선됨)
 * -
 * In MariaDB Docs
 * By default, if a GROUP BY clause is present, the rows in the output will be sorted by the expressions used in the GROUP BY.
 * If you want the rows to be sorted by another field, you can add an explicit ORDER BY. If you don't want the result to be ordered, you can add ORDER BY NULL.
 */
@SuppressWarnings("unchecked")
public class OrderByNull extends OrderSpecifier {

    public static final OrderByNull DEFAULT = new OrderByNull();

    private OrderByNull() {
        super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
    }

}
