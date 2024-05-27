package menu.domain.exception;

import menu.global.exception.base.MenuPlannerError;

public enum MenuError implements MenuPlannerError {
    MENU_NOT_FOUND("존재하지 않는 메뉴입니다."),
    ;

    private final String errorMessage;

    MenuError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
