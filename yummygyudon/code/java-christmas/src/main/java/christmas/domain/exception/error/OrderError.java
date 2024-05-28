package christmas.domain.exception.error;

import christmas.global.exception.base.ChristmasPlannerError;

public enum OrderError implements ChristmasPlannerError {

    // 단일 메뉴 주문 갯수가 20개 초과일 경우
    TOO_MANY_SINGLE_MENU_QUANTITY("유효하지 않은 주문입니다. 다시 입력해 주세요."),

    // 단일 메뉴 주문 갯수가 1개 미만일 경우
    TOO_LITTLE_SINGLE_MENU_QUANTITY("유효하지 않은 주문입니다. 다시 입력해 주세요."),

    // 전체 메뉴 주문 갯수가 20개 초과할 경우
    TOO_MANY_MENU_QUANTITY("유효하지 않은 주문입니다. 다시 입력해 주세요."),

    // 음료 메뉴만 주문한 경우
    ONLY_BEVERAGE("유효하지 않은 주문입니다. 다시 입력해 주세요."),

    // 중복된 메뉴에 대한 주문이 존재할 경우
    DUPLICATED_ORDER_EXIST("유효하지 않은 주문입니다. 다시 입력해 주세요."),
    ;

    private final String errorMessage;

    OrderError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override

    public String getErrorMessage() {
        return errorMessage;
    }
}
