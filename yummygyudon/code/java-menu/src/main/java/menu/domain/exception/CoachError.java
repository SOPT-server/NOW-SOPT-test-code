package menu.domain.exception;

import menu.global.exception.base.MenuPlannerError;

public enum CoachError implements MenuPlannerError {
    TOO_LONG_NAME("이름이 너무 깁니다."),
    TOO_SHORT_NAME("이름이 너무 짧습니다."),
    TOO_MANY_COACH("코치의 인원이 너무 많습니다."),
    TOO_FEW_COACH("코치의 인원이 너무 적습니다."),
    TOO_MANY_IMPOSSIBLE_MENUS_OF_COACH("못먹는 메뉴가 너무 많습니다."),
    ;

    private final String errorMessage;

    CoachError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
