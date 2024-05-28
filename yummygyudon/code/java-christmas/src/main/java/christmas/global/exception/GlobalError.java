package christmas.global.exception;

import christmas.global.exception.base.ChristmasPlannerError;

public enum GlobalError implements ChristmasPlannerError {

    /** 입력 에러 */
    // 잘못된 날짜 형식으로 입력했을 경우
    NOT_AVAILABLE_DATE_INPUT_FORMAT("유효하지 않은 날짜입니다. 다시 입력해 주세요."),

    // 잘못된 패턴으로 메뉴와 주문 갯수를 입력했을 경우
    NOT_AVAILABLE_MENU_INPUT_FORMAT("유효하지 않은 주문입니다. 다시 입력해 주세요."),
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
