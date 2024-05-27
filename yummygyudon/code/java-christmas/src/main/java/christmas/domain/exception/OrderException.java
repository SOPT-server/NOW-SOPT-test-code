package christmas.domain.exception;

import christmas.domain.exception.error.OrderError;
import christmas.global.exception.base.ChristmasPlannerException;

/**
 * Order 도메인 관련 에러만 발생시키는 예외
 */
public class OrderException extends ChristmasPlannerException {
    public OrderException(OrderError error) {
        super(error);
    }
}
