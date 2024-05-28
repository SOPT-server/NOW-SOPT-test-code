package menu.global.exception;

import menu.global.exception.base.MenuPlannerException;

public class GlobalException extends MenuPlannerException {
    public GlobalException(GlobalError error) {
        super(error);
    }
}
