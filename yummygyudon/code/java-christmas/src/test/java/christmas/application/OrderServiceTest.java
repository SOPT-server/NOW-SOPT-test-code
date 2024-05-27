package christmas.application;

import christmas.application.info.EventResultInfo;
import christmas.domain.enums.MenuType;
import christmas.domain.exception.MenuException;
import christmas.domain.exception.OrderException;
import christmas.domain.exception.error.MenuError;
import christmas.domain.exception.error.OrderError;
import christmas.presentation.view.InputView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    void initRepository() {
        orderService = new OrderService();
    }


    @DisplayName("잘못된 입력 주문들에 대해 저장할 경우, 예외가 발생한다.")
    @ParameterizedTest(name = "{index} 번째 입력 주문 = {0}, 기대 예외 = {1}")
    @MethodSource("argumentsForWrongMenuInputs")
    void registerWrongOrders(List<InputView.MenuInput> wrongMenuInputs, Exception expectedException) {
        assertThatThrownBy(() -> orderService.registerOrders(wrongMenuInputs))
                .usingRecursiveComparison()
                .isEqualTo(expectedException);
    }
    private static Stream<Arguments> argumentsForWrongMenuInputs() {
        return Stream.of(
                // 존재하지 않은 메뉴를 등록하려고 할 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("계란샐러드",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        new MenuException(MenuError.MENU_NOT_FOUND)
                ),
                // 단일 메뉴 중 주문 갯수가 20개를 초과할 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("시저샐러드",21),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        new OrderException(OrderError.TOO_MANY_SINGLE_MENU_QUANTITY)
                ),
                // 단일 메뉴 중 주문 갯수가 1개 미만일 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("크리스마스파스타",0),
                                new InputView.MenuInput("초코케이크",5),
                                new InputView.MenuInput("레드와인",5)
                        ),
                        new OrderException(OrderError.TOO_LITTLE_SINGLE_MENU_QUANTITY)
                ),
                // 총 메뉴 갯수가 20개를 초과할 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",6),
                                new InputView.MenuInput("크리스마스파스타",5),
                                new InputView.MenuInput("초코케이크",5),
                                new InputView.MenuInput("레드와인",5)
                        ),
                        new OrderException(OrderError.TOO_MANY_MENU_QUANTITY)
                ),
                // 주문 메뉴가 모두 음료일 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("제로콜라",7),
                                new InputView.MenuInput("레드와인",7),
                                new InputView.MenuInput("샴페인",6)
                        ),
                        new OrderException(OrderError.ONLY_BEVERAGE)
                ),
                // 중복 메뉴 주문이 있을 경우
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("바비큐립",3),
                                new InputView.MenuInput("바비큐립",7),
                                new InputView.MenuInput("제로콜라",5)
                        ),
                        new OrderException(OrderError.DUPLICATED_ORDER_EXIST)
                )
        );
    }
    @DisplayName("올바른 입력 주문들에 대해 저장할 경우, 성공한다.")
    @ParameterizedTest(name = "{index} 번째 탐색 메뉴값 = {0}")
    @MethodSource("argumentsForAvailableMenuInputs")
    void registerAvailableOrders(List<InputView.MenuInput> availableMenuInputs) {
        assertThatCode(() -> orderService.registerOrders(availableMenuInputs))
                .doesNotThrowAnyException();
    }
    private static Stream<Arguments> argumentsForAvailableMenuInputs() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("시저샐러드",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("제로콜라",5),
                                new InputView.MenuInput("레드와인",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("크리스마스파스타",5),
                                new InputView.MenuInput("초코케이크",5),
                                new InputView.MenuInput("레드와인",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("제로콜라",7),
                                new InputView.MenuInput("레드와인",7),
                                new InputView.MenuInput("아이스크림",6)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("바비큐립",3),
                                new InputView.MenuInput("티본스테이크",7),
                                new InputView.MenuInput("제로콜라",5)
                        )
                )
        );
    }

    /** 연산 기능에 대한 테스트 */
    @DisplayName("지정 타입의 메뉴에 대한 주문 존재 여부를 정확하게 반환한다.")
    @ParameterizedTest(name = "{index} 번째 탐색 메뉴값 = {0}")
    @MethodSource("argumentsForCheckSpecifiedTypeExist")
    void isExistAnyMenuTypeInOrders(List<InputView.MenuInput> menuInputs, MenuType targetMenuType, boolean expectedResult) {
        // given
        orderService.registerOrders(menuInputs);

        // when
        boolean existAnyMenuTypeInOrders = orderService.isExistAnyMenuTypeInOrders(targetMenuType);

        // then
        assertThat(existAnyMenuTypeInOrders).isEqualTo(expectedResult);
    }
    private static Stream<Arguments> argumentsForCheckSpecifiedTypeExist() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        MenuType.BEVERAGE,
                        false
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("시저샐러드",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        MenuType.MAIN,
                        true
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("제로콜라",5),
                                new InputView.MenuInput("레드와인",5)
                        ),
                        MenuType.MAIN,
                        false
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("제로콜라",5),
                                new InputView.MenuInput("레드와인",5)
                        ),
                        MenuType.APPETIZER,
                        true
                )
        );
    }

    @DisplayName("주문들의 총합 결제금액을 정확하게 반환한다.")
    @ParameterizedTest(name = "{index} 번째 탐색 메뉴값 = {0}")
    @MethodSource("argumentsForTotalAmount")
    void getTotalAmount(List<InputView.MenuInput> menuInputs, int expectedTotalAmount) {
        // given
        orderService.registerOrders(menuInputs);

        // when
        int totalAmount = orderService.getTotalAmount();

        // then
        assertThat(totalAmount).isEqualTo(expectedTotalAmount);
    }
    private static Stream<Arguments> argumentsForTotalAmount() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5), // 27_500
                                new InputView.MenuInput("해산물파스타",5), // 175_000
                                new InputView.MenuInput("아이스크림",5) // 25_000
                        ),
                        227_500
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5), // 27_500
                                new InputView.MenuInput("시저샐러드",5), // 40_000
                                new InputView.MenuInput("해산물파스타",5), // 175_000
                                new InputView.MenuInput("아이스크림",5) // 25_000
                        ),
                        267_500
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5), // 30_000
                                new InputView.MenuInput("제로콜라",5), // 15_000
                                new InputView.MenuInput("레드와인",5) // 300_000
                        ),
                        345_000
                )
        );
    }

    @DisplayName("지정 타입의 메뉴에 대한 총 주문 수량을 정확하게 반환한다.")
    @ParameterizedTest(name = "{index} 번째 입력 주문 = {0}, 탐색 대상 타입 : {1} | 기대 주문 총량 = {2}")
    @MethodSource("argumentsForTotalAmountOfSpecifiedType")
    void getOrdersCountByMenuType(List<InputView.MenuInput> menuInputs, MenuType targetType, int expectedOrdersCount) {
        // given
        orderService.registerOrders(menuInputs);

        // when
        int ordersCountByMenuType = orderService.getOrdersCountByMenuType(targetType);

        // then
        assertThat(ordersCountByMenuType).isEqualTo(expectedOrdersCount);
    }
    private static Stream<Arguments> argumentsForTotalAmountOfSpecifiedType() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        MenuType.BEVERAGE,
                        0
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        MenuType.APPETIZER,
                        5
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("시저샐러드",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        MenuType.APPETIZER,
                        10
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("바비큐립",5),
                                new InputView.MenuInput("티본스테이크",5)
                        ),
                        MenuType.MAIN,
                        10
                )
        );
    }

    @DisplayName("주문 내역 정보를 정확하게 반환한다.")
    @ParameterizedTest(name = "{index} 번째 입력 주문 = {0}, 기대 주문 내역 = {1}")
    @MethodSource("argumentsForOrderHistoryInfo")
    void getOrdersHistory(List<InputView.MenuInput> menuInputs, List<EventResultInfo.ReceiveMenu> expectedMenuHistoryInfos) {
        // given
        orderService.registerOrders(menuInputs);

        // when
        List<EventResultInfo.ReceiveMenu> ordersHistory = orderService.getOrdersHistory();

        // then
        assertThat(ordersHistory).usingRecursiveComparison().isEqualTo(expectedMenuHistoryInfos);
    }
    private static Stream<Arguments> argumentsForOrderHistoryInfo() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        List.of(
                                new EventResultInfo.ReceiveMenu("타파스",5),
                                new EventResultInfo.ReceiveMenu("해산물파스타",5),
                                new EventResultInfo.ReceiveMenu("아이스크림",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("타파스",5),
                                new InputView.MenuInput("시저샐러드",5),
                                new InputView.MenuInput("해산물파스타",5),
                                new InputView.MenuInput("아이스크림",5)
                        ),
                        List.of(
                                new EventResultInfo.ReceiveMenu("타파스",5),
                                new EventResultInfo.ReceiveMenu("시저샐러드",5),
                                new EventResultInfo.ReceiveMenu("해산물파스타",5),
                                new EventResultInfo.ReceiveMenu("아이스크림",5)
                        )
                ),
                Arguments.of(
                        List.of(
                                new InputView.MenuInput("양송이수프",5),
                                new InputView.MenuInput("제로콜라",5),
                                new InputView.MenuInput("레드와인",5)
                        ),
                        List.of(
                                new EventResultInfo.ReceiveMenu("양송이수프",5),
                                new EventResultInfo.ReceiveMenu("제로콜라",5),
                                new EventResultInfo.ReceiveMenu("레드와인",5)
                        )
                )
        );
    }

}
