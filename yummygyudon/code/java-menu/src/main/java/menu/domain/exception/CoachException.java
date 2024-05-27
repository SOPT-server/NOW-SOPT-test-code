package menu.domain.exception;

import menu.global.exception.base.MenuPlannerException;

public class CoachException extends MenuPlannerException {
    public CoachException(CoachError error) {
        super(error);
    }
}
