package christmas.application;

import christmas.domain.entity.Menu;
import christmas.domain.enums.Badge;
import christmas.domain.repository.MenuRepository;
import christmas.global.constant.Standard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class GiftServiceTest {
    private final GiftService giftService = new GiftService();
    private final Menu officialGiveawayMenu = MenuRepository.findMenuByName(Standard.GIVEAWAY_MENU_NAME);

    @DisplayName("결제 금액에 따른 올바른 증정 메뉴 증정 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 확인 대상 총 금액 = {0}원, 기댓값 = {1}")
    @MethodSource("argumentsForCheckGiveaway")
    void isGiveaway(int totalAmount, boolean expectedResult) {
        // when
        boolean isGiveaway = giftService.isGiveaway(totalAmount);

        // then
        assertThat(isGiveaway).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckGiveaway() {
        return Stream.of(
                Arguments.of(119_000, false),
                Arguments.of(120_001, true),
                Arguments.of(120_000, true),
                Arguments.of(121_000, true),
                Arguments.of(119_999, false)
        );
    }

    @DisplayName("올바른 증정 메뉴 이름을 반환한다.")
    @Test
    void getGiveawayMenuName() {
        // when
        String giveawayMenuName = giftService.getGiveawayMenuName();

        // then
        assertThat(giveawayMenuName).isEqualTo(officialGiveawayMenu.getName());
    }

    @DisplayName("올바른 증정 메뉴 증정 수량을 반환한다.")
    @Test
    void getGiveawayMenuPrice() {
        // when
        int giveawayMenuQuantity = giftService.getGiveawayMenuQuantity();

        // then
        assertThat(giveawayMenuQuantity).isEqualTo(1);
    }

    @DisplayName("올바른 증정 메뉴 가격을 반환한다.")
    @Test
    void getGiveawayMenuQuantity() {
        // when
        int giveawayMenuPrice = giftService.getGiveawayMenuPrice();

        // then
        assertThat(giveawayMenuPrice).isEqualTo(officialGiveawayMenu.getPrice());
    }

    @DisplayName("혜택 금액에 따른 올바른 뱃지 증정 여부를 반환한다.")
    @ParameterizedTest(name = "{index} 번째 할인 금액 = {0}원, 기댓값 = {1}")
    @MethodSource("argumentsForCheckProvidingBadge")
    void isBadge(int discountAmount, boolean expectedResult) {
        // when
        boolean isBadgeProvided = giftService.isBadge(discountAmount);

        // then
        assertThat(isBadgeProvided).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckProvidingBadge() {
        return Stream.of(
                Arguments.of(1_000, false),
                Arguments.of(4_900, false),
                Arguments.of(4_999, false),
                Arguments.of(5_000, true),
                Arguments.of(5_001, true)
        );
    }

    @DisplayName("올바른 증정 뱃지의 이름을 반환한다.")
    @ParameterizedTest(name = "{index} 번째 할인 금액 = {0}원, 기대 수령 뱃지 이름 = {1}")
    @MethodSource("argumentsForReceiveBadge")
    void receiveBadgeName(int discountAmount, String expectedBadgeName) {
        // when
        String badgeName = giftService.receiveBadgeName(discountAmount);

        // then
        assertThat(badgeName).isEqualTo(expectedBadgeName);
    }
    private static Stream<Arguments> argumentsForReceiveBadge() {
        return Stream.of(
                Arguments.of(4_999, Badge.NONE.getName()),
                Arguments.of(5_001, Badge.STAR.getName()),
                Arguments.of(9_999, Badge.STAR.getName()),
                Arguments.of(10_001, Badge.TREE.getName()),
                Arguments.of(19_999, Badge.TREE.getName()),
                Arguments.of(20_001, Badge.SANTA.getName()),
                Arguments.of(100_000, Badge.SANTA.getName()),
                Arguments.of(1_000_000, Badge.SANTA.getName())
        );
    }
}