package menu.global.exception.base;

public class MenuPlannerException extends IllegalArgumentException{
    private static final String ERROR_MESSAGE_HEADER = "[ERROR] ";

    public MenuPlannerException(MenuPlannerError error) {
        super(ERROR_MESSAGE_HEADER + error.getErrorMessage());
    }

}
