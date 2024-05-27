package christmas.domain.exception;

import christmas.domain.exception.error.MenuError;
import christmas.global.exception.base.ChristmasPlannerException;

/**
 * Menu 도메인 관련 에러만 발생시키는 예외
 */
public class MenuException extends ChristmasPlannerException {
    public MenuException(MenuError error) {
        super(error);
    }
}
