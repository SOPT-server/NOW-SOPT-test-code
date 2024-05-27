package christmas.application;

import christmas.domain.enums.EventDiscountType;
import christmas.global.constant.Standard;


class DiscountService {

    /** 크리스마스 D-Day 할인 관련 기능 */
    boolean isInChristmasDiscountEventPeriod(int date) {
        return date <= Standard.DATE_OF_CHRISTMAS;
    }

    int getTotalChristmasEventDiscountAmount(int date) {
        int dateDifference = date - Standard.FIRST_DATE_OF_DECEMBER;
        int increaseDiscount = Standard.D_DAY_DISCOUNT_INCREASE_AMOUNT * dateDifference;
        return EventDiscountType.CHRISTMAS.getDiscountAmount() + increaseDiscount;
    }

    /** 주중-주말 할인 관련 기능 */
    boolean isInWeekdays(int date) {
        return Standard.WEEKDAY_OF_DECEMBER.contains(date);
    }

    boolean isInWeekends(int date) {
        return Standard.WEEKEND_OF_DECEMBER.contains(date);
    }

    int getTotalWeekDiscountAmount(EventDiscountType discountType, int targetMenuCount) {
        return discountType.getDiscountAmount() * targetMenuCount;
    }


    /** 특별 할인 관련 기능 */
    boolean isInSpecialDiscountEventDays(int date) {
        return Standard.SPECIAL_DATE_OF_DECEMBER.contains(date);
    }

    int getTotalSpecialDiscountAmount() {
        return EventDiscountType.SPECIAL.getDiscountAmount();
    }

}
