package menu.domain.exception;

import menu.global.exception.base.MenuPlannerException;

public class MenuException extends MenuPlannerException {
    public MenuException(MenuError error) {
        super(error);
    }
}
