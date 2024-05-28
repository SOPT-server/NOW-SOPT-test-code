package menu.global.exception;

import menu.global.exception.base.MenuPlannerError;

public enum GlobalError implements MenuPlannerError {

    /** 입력 에러 */
    // 잘못된 패턴으로 메뉴와 주문 갯수를 입력했을 경우
    NOT_AVAILABLE_INPUT_PATTERN("잘못된 형식의 입력값입니다. 다시 입력해 주세요."),

    ;

    private final String errorMessage;

    GlobalError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
