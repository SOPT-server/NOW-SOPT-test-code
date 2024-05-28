package christmas.global.constant;

import java.util.List;

public abstract class Standard {

    // 입력 관련 기준
    public static final String MENU_INPUT_SEPARATOR_FOR_ORDER = ",";
    public static final String MENU_INPUT_SEPARATOR_FOR_NAME_AND_QUANTITY = "-";

    // 날짜 관련 기준
    public static final int MONTH_OF_DECEMBER = 12;
    public static final int FIRST_DATE_OF_DECEMBER = 1;
    public static final int LAST_DATE_OF_DECEMBER = 31;
    public static final int DATE_OF_CHRISTMAS = 25;

    // 메뉴 관련 기준
    public static final int MINIMUM_QUANTITY_FOR_ORDER = 1;
    public static final int MAXIMUM_QUANTITY_FOR_ORDER = 20;
    public static final String GIVEAWAY_MENU_NAME = "샴페인";

    // 금액 관련 기준
    public static final int D_DAY_DISCOUNT_INCREASE_AMOUNT = 100;
    public static final int MINIMUM_AMOUNT_FOR_ALL_EVENT = 10_000;
    public static final int MINIMUM_AMOUNT_FOR_GIVEAWAY_EVENT = 120_000;
    public static final int MINIMUM_AMOUNT_FOR_BADGE_EVENT = 5_000;

    // 기간 관련 기준
    public static final List<Integer> WEEKDAY_OF_DECEMBER = List.of(
            3, 4, 5, 6, 7,
            10, 11, 12, 13, 14,
            17, 18, 19, 20, 21,
            24, 25, 26, 27, 28,
            31
    );

    public static final List<Integer> WEEKEND_OF_DECEMBER = List.of(
            1, 2,
            8, 9,
            15, 16,
            22, 23,
            29, 30
    );

    public static final List<Integer> SPECIAL_DATE_OF_DECEMBER = List.of(
            3, 10, 17, 24, 25, 31
    );

}
