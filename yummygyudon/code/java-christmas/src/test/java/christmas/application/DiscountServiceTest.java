package christmas.application;

import christmas.domain.enums.EventDiscountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @DisplayName("날짜에 따른 올바른 크리스마스 디데이 할인 적용 일자 소속 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 확인 일자 = 12월 {0}일, 기댓값 = {1}")
    @MethodSource("argumentsForCheckDateContainsInChristmasEventDays")
    void isInChristmasDiscountEventPeriod(int date, boolean expectedResult) {
        // when
        boolean isInChristmasDiscountEventPeriod = discountService.isInChristmasDiscountEventPeriod(date);

        // then
        assertThat(isInChristmasDiscountEventPeriod).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckDateContainsInChristmasEventDays() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(8, true),
                Arguments.of(15, true),
                Arguments.of(22, true),
                Arguments.of(23, true),
                Arguments.of(24, true),
                Arguments.of(25, true),
                Arguments.of(26, false),
                Arguments.of(27, false),
                Arguments.of(28, false),
                Arguments.of(29, false),
                Arguments.of(30, false),
                Arguments.of(31, false)
        );
    }

    @DisplayName("올바른 크리스마스 디데이 할인 적용 금액을 반환한다.")
    @ParameterizedTest(name = "{index} 번째 적용 할인 타입 = 크리스마스 디데이 할인 | 일자 = 12월 {0}일, 기대 할인가 = {1}원")
    @MethodSource("argumentsForChristmasEventDiscount")
    void getTotalChristmasEventDiscountAmount(int date, int expectedDiscountAmount) {
        // when
        int totalChristmasEventDiscountAmount = discountService.getTotalChristmasEventDiscountAmount(date);

        // then
        assertThat(totalChristmasEventDiscountAmount).isEqualTo(expectedDiscountAmount);
    }
    private static Stream<Arguments> argumentsForChristmasEventDiscount() {
        return Stream.of(
                Arguments.of(1, 1_000),
                Arguments.of(2, 1_100),
                Arguments.of(3, 1_200),
                Arguments.of(4, 1_300),
                Arguments.of(5, 1_400),
                Arguments.of(6, 1_500),
                Arguments.of(7, 1_600),
                Arguments.of(14, 2_300),
                Arguments.of(21, 3_000),
                Arguments.of(22, 3_100),
                Arguments.of(23, 3_200),
                Arguments.of(24, 3_300),
                Arguments.of(25, 3_400)
        );
    }

    @DisplayName("날짜에 따른 올바른 주일 소속 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 확인 일자 = 12월 {0}일, 기댓값 = {1}")
    @MethodSource("argumentsForCheckDateContainsInWeekday")
    void isInWeekdays(int date, boolean expectedResult) {
        // when
        boolean isInWeekdays = discountService.isInWeekdays(date);

        // then
        assertThat(isInWeekdays).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckDateContainsInWeekday() {
        return Stream.of(
                Arguments.of(1, false),
                Arguments.of(2, false),
                Arguments.of(3, true),
                Arguments.of(7, true),
                Arguments.of(8, false),
                Arguments.of(9, false),
                Arguments.of(10, true),
                Arguments.of(14, true),
                Arguments.of(15, false),
                Arguments.of(16, false),
                Arguments.of(17, true),
                Arguments.of(21, true),
                Arguments.of(22, false),
                Arguments.of(23, false),
                Arguments.of(24, true),
                Arguments.of(28, true),
                Arguments.of(29, false),
                Arguments.of(30, false),
                Arguments.of(31, true)
        );
    }

    @DisplayName("날짜에 따른 올바른 주말 소속 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 확인 일자 = 12월 {0}일, 기댓값 = {1}")
    @MethodSource("argumentsForCheckDateContainsInWeekend")
    void isInWeekends(int date, boolean expectedResult) {
        // when
        boolean isInWeekends = discountService.isInWeekends(date);

        // then
        assertThat(isInWeekends).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckDateContainsInWeekend() {
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(2, true),
                Arguments.of(3, false),
                Arguments.of(7, false),
                Arguments.of(8, true),
                Arguments.of(9, true),
                Arguments.of(10, false),
                Arguments.of(14, false),
                Arguments.of(15, true),
                Arguments.of(16, true),
                Arguments.of(17, false),
                Arguments.of(21, false),
                Arguments.of(22, true),
                Arguments.of(23, true),
                Arguments.of(24, false),
                Arguments.of(28, false),
                Arguments.of(29, true),
                Arguments.of(30, true),
                Arguments.of(31, false)
        );
    }

    @DisplayName("올바른 주 할인 적용 금액을 반환한다.")
    @ParameterizedTest(name = "{index} 번째 적용 할인 타입 = {0} | 적용 주문 메뉴 수 = {1}, 기대 할인가 = {2}원")
    @MethodSource("argumentsForWeekDiscount")
    void getTotalWeekDiscountAmount(EventDiscountType discountType, int targetMenuOrdersCount, int expectedDiscountAmount) {
        // when
        int totalWeekDiscountAmount = discountService.getTotalWeekDiscountAmount(discountType, targetMenuOrdersCount);

        // then
        assertThat(totalWeekDiscountAmount).isEqualTo(expectedDiscountAmount);
    }
    private static Stream<Arguments> argumentsForWeekDiscount() {
        return Stream.of(
                Arguments.of(EventDiscountType.WEEKDAY, 1, 2_023),
                Arguments.of(EventDiscountType.WEEKDAY, 2, 4_046),
                Arguments.of(EventDiscountType.WEEKDAY, 3, 6_069),
                Arguments.of(EventDiscountType.WEEKDAY, 4, 8_092),
                Arguments.of(EventDiscountType.WEEKDAY, 5, 10_115),
                Arguments.of(EventDiscountType.WEEKDAY, 10, 20_230),
                Arguments.of(EventDiscountType.WEEKDAY, 15, 30_345),
                Arguments.of(EventDiscountType.WEEKDAY, 16, 32_368),
                Arguments.of(EventDiscountType.WEEKDAY, 17, 34_391),
                Arguments.of(EventDiscountType.WEEKDAY, 18, 36_414),
                Arguments.of(EventDiscountType.WEEKDAY, 19, 38_437),
                Arguments.of(EventDiscountType.WEEKDAY, 20, 40_460),
                Arguments.of(EventDiscountType.WEEKEND, 1, 2_023),
                Arguments.of(EventDiscountType.WEEKEND, 2, 4_046),
                Arguments.of(EventDiscountType.WEEKEND, 3, 6_069),
                Arguments.of(EventDiscountType.WEEKEND, 4, 8_092),
                Arguments.of(EventDiscountType.WEEKEND, 5, 10_115),
                Arguments.of(EventDiscountType.WEEKEND, 10, 20_230),
                Arguments.of(EventDiscountType.WEEKEND, 15, 30_345),
                Arguments.of(EventDiscountType.WEEKEND, 16, 32_368),
                Arguments.of(EventDiscountType.WEEKEND, 17, 34_391),
                Arguments.of(EventDiscountType.WEEKEND, 18, 36_414),
                Arguments.of(EventDiscountType.WEEKEND, 19, 38_437),
                Arguments.of(EventDiscountType.WEEKEND, 20, 40_460)
        );
    }

    @DisplayName("날짜에 따른 올바른 특별 할인 적용 일자 소속 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 확인 일자 = 12월 {0}일, 기댓값 = {1}")
    @MethodSource("argumentsForCheckDateContainsInSpecialDiscountEventDays")
    void isInSpecialDiscountEventDays(int date, boolean expectedResult) {
        // when
        boolean isInSpecialDiscountEventDays = discountService.isInSpecialDiscountEventDays(date);

        // then
        assertThat(isInSpecialDiscountEventDays).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckDateContainsInSpecialDiscountEventDays() {
        return Stream.of(
                Arguments.of(2, false),
                Arguments.of(3, true),
                Arguments.of(4, false),
                Arguments.of(9, false),
                Arguments.of(10, true),
                Arguments.of(11, false),
                Arguments.of(16, false),
                Arguments.of(17, true),
                Arguments.of(18, false),
                Arguments.of(23, false),
                Arguments.of(24, true),
                Arguments.of(25, true),
                Arguments.of(26, false),
                Arguments.of(30, false),
                Arguments.of(31, true)
        );
    }

    @DisplayName("올바른 특별 할인 적용 금액을 반환한다.")
    @Test
    void getTotalSpecialDiscountAmount() {
        // given
        int officialSpecialDiscountAmount = EventDiscountType.SPECIAL.getDiscountAmount();

        // when
        int totalSpecialDiscountAmount = discountService.getTotalSpecialDiscountAmount();

        // then
        assertThat(totalSpecialDiscountAmount).isEqualTo(officialSpecialDiscountAmount);
    }
}