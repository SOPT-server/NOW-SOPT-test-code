package christmas.global.exception;

import christmas.global.exception.base.ChristmasPlannerException;

/**
 * 전역적 혹은 시스템 전체 범위에서 발생하는 예외
 */
public class GlobalException extends ChristmasPlannerException {

    public GlobalException(GlobalError error) {
        super(error);
    }
}
