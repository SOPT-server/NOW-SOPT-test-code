package christmas.domain;

import christmas.domain.entity.Menu;
import christmas.domain.enums.MenuType;
import christmas.domain.repository.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Menu 도메인 테스트")
class MenuTest {

    @DisplayName("존재하지 않는 메뉴를 찾으면 예외가 발생한다.")
    @ParameterizedTest(name = "{index} 번째 탐색 메뉴값 = {0}")
    @ValueSource(strings = {
            "우니동", "소갈비살", "탕후루", "사이다"
    })
    void findMenuNotExist(String notExistMenu) {
        assertThatThrownBy(() -> MenuRepository.findMenuByName(notExistMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 메뉴를 찾으면 정확하게 반환한다.")
    @ParameterizedTest(name = "{index} 번째 탐색 메뉴값 = {0}")
    @MethodSource("argumentsForExistMenu")
    void findMenuExist(String searchMenu, Menu expectedMenu) {
        assertThat(MenuRepository.findMenuByName(searchMenu))
                .usingRecursiveComparison().isEqualTo(expectedMenu);
    }
    private static Stream<Arguments> argumentsForExistMenu() {
        return Stream.of(
                Arguments.of("양송이수프", new Menu("양송이수프", 6_000, MenuType.APPETIZER)),
                Arguments.of("타파스", new Menu("타파스", 5_500, MenuType.APPETIZER)),
                Arguments.of("시저샐러드", new Menu("시저샐러드", 8_000, MenuType.APPETIZER)),
                Arguments.of("티본스테이크", new Menu("티본스테이크", 55_000, MenuType.MAIN)),
                Arguments.of("바비큐립", new Menu("바비큐립", 54_000, MenuType.MAIN)),
                Arguments.of("해산물파스타", new Menu("해산물파스타", 35_000, MenuType.MAIN)),
                Arguments.of("크리스마스파스타", new Menu("크리스마스파스타", 25_000, MenuType.MAIN)),
                Arguments.of("초코케이크", new Menu("초코케이크", 15_000, MenuType.DESSERT)),
                Arguments.of("아이스크림", new Menu("아이스크림", 5_000, MenuType.DESSERT)),
                Arguments.of("제로콜라", new Menu("제로콜라", 3_000, MenuType.BEVERAGE)),
                Arguments.of("레드와인", new Menu("레드와인", 60_000, MenuType.BEVERAGE)),
                Arguments.of("샴페인", new Menu("샴페인", 25_000, MenuType.BEVERAGE))
        );
    }


}