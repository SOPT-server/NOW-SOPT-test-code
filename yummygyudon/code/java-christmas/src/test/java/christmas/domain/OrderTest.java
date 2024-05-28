package christmas.domain;

import christmas.domain.entity.Menu;
import christmas.domain.entity.Order;
import christmas.domain.enums.MenuType;
import christmas.domain.exception.OrderException;
import christmas.domain.exception.error.OrderError;
import christmas.domain.repository.MenuRepository;
import christmas.domain.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Order 도메인 테스트")
class OrderTest {


    /** Order 테스트 */
    @Nested
    @DisplayName("Order Object Test")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class OrderObjectTest {
        private static final Menu availableMenu = MenuRepository.findMenuByName("초코케이크");
        @DisplayName("1보다 작은 수량으로 Order 객체를 생성할 경우, 예외가 발생한다.")
        @ParameterizedTest(name = "{index} 번째 유효하지 않은 수량값 = {0}")
        @ValueSource(ints = {
                -1, 0, -9999
        })
        void createOrderWithTooSmallQuantity(int invalidQuantity) {
            assertThatThrownBy(() -> new Order(invalidQuantity, availableMenu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(OrderException.class)
                    .hasMessageContaining(OrderError.TOO_LITTLE_SINGLE_MENU_QUANTITY.getErrorMessage());
        }

        @DisplayName("20보다 큰 수량으로 Order 객체를 생성할 경우, 예외가 발생한다.")
        @ParameterizedTest(name = "{index} 번째 유효하지 않은 수량값 = {0}")
        @ValueSource(ints = {
                21, 100, 1000, 9999
        })
        void createOrderWithTooBigQuantity(int invalidQuantity) {
            assertThatThrownBy(() -> new Order(invalidQuantity, availableMenu))
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(OrderException.class)
                    .hasMessageContaining(OrderError.TOO_MANY_SINGLE_MENU_QUANTITY.getErrorMessage());
        }

        @DisplayName("해당 주문에 대한 결제금액을 반환한다.")
        @ParameterizedTest(name = "{index} 번째 주문 = {1} : {0}개 / 예상 총 금액 : {2}")
        @MethodSource("argumentsForAvailableOrder")
        void calculateTotalPrice(int quantity, Menu menu, int expectedTotalPrice) {
            // when
            Order availableOrder = new Order(quantity, menu);

            // given
            int totalPrice = availableOrder.calculateTotalPrice();

            // then
            assertThat(totalPrice).isEqualTo(expectedTotalPrice);
        }
        private static Stream<Arguments> argumentsForAvailableOrder() {
            return Stream.of(
                    Arguments.of(3, new Menu("양송이수프", 6_000, MenuType.APPETIZER), 18_000),
                    Arguments.of(2, new Menu("타파스", 5_500, MenuType.APPETIZER), 11_000),
                    Arguments.of(5, new Menu("시저샐러드", 8_000, MenuType.APPETIZER), 40_000),
                    Arguments.of(10, new Menu("티본스테이크", 55_000, MenuType.MAIN), 550_000),
                    Arguments.of(13, new Menu("바비큐립", 54_000, MenuType.MAIN), 702_000),
                    Arguments.of(7, new Menu("해산물파스타", 35_000, MenuType.MAIN), 245_000),
                    Arguments.of(4, new Menu("크리스마스파스타", 25_000, MenuType.MAIN), 100_000),
                    Arguments.of(9, new Menu("초코케이크", 15_000, MenuType.DESSERT), 135_000),
                    Arguments.of(20, new Menu("아이스크림", 5_000, MenuType.DESSERT), 100_000),
                    Arguments.of(1, new Menu("제로콜라", 3_000, MenuType.BEVERAGE), 3_000),
                    Arguments.of(11, new Menu("레드와인", 60_000, MenuType.BEVERAGE), 660_000),
                    Arguments.of(19, new Menu("샴페인", 25_000, MenuType.BEVERAGE), 475_000)
            );
        }

    }

    /** Order Repository 테스트 */
    @Nested
    @DisplayName("Order Repository Test")
    @TestInstance(TestInstance.Lifecycle.PER_METHOD)
    class OrderRepoTest {
        private OrderRepository orderRepository;

        @BeforeEach
        void setOrderRepository() {
            orderRepository = new OrderRepository();
        }

        @DisplayName("음료 메뉴에 대한 Order 만 존재할 경우, 예외가 발생한다.")
        @ParameterizedTest(name = "{index} 번째 음료 메뉴만 주문 = {0}")
        @MethodSource("argumentsForOnlyBeverage")
        void checkOnlyBeverage(List<Order> orders) {
            assertThatThrownBy(() -> orderRepository.insert(orders))
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(OrderException.class)
                    .hasMessageContaining(OrderError.ONLY_BEVERAGE.getErrorMessage());
        }
        private static Stream<Arguments> argumentsForOnlyBeverage() {
            return Stream.of(
                    Arguments.of(
                            Named.of(
                                    "[제로콜라:1]",
                                    List.of(
                                            new Order(1, new Menu("제로콜라", 3_000, MenuType.BEVERAGE))
                                    )
                            )
                    ),
                    Arguments.of(
                            Named.of(
                                    "[레드와인:5, 제로콜라:10, 샴페인:5]",
                                    List.of(
                                            new Order(5, new Menu("레드와인", 60_000, MenuType.BEVERAGE)),
                                            new Order(10, new Menu("제로콜라", 3_000, MenuType.BEVERAGE)),
                                            new Order(5, new Menu("샴페인", 25_000, MenuType.BEVERAGE))
                                    )
                            )
                    ),
                    Arguments.of(
                            Named.of(
                                    "[레드와인:5, 샴페인:5]",
                                    List.of(
                                            new Order(5, new Menu("레드와인", 60_000, MenuType.BEVERAGE)),
                                            new Order(5, new Menu("샴페인", 25_000, MenuType.BEVERAGE))
                                    )
                            )
                    )
            );
        }

        @DisplayName("동일 메뉴에 대한 중복 Order 가 존재할 경우, 예외가 발생한다.")
        @ParameterizedTest(name = "{index} 번째 중복 존재 주문 = {0}")
        @MethodSource("argumentsForDuplicatedOrders")
        void checkDuplicatedMenuOrder(List<Order> orders) {
            assertThatThrownBy(() -> orderRepository.insert(orders))
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(OrderException.class)
                    .hasMessageContaining(OrderError.DUPLICATED_ORDER_EXIST.getErrorMessage());
        }
        private static Stream<Arguments> argumentsForDuplicatedOrders() {
            return Stream.of(
                    Arguments.of(
                            Named.of(
                                    "[양송이수프:1, 양송이수프:2, 타파스:1]",
                                    List.of(
                                            new Order(1, new Menu("양송이수프", 6_000, MenuType.APPETIZER)),
                                            new Order(2, new Menu("양송이수프", 6_000, MenuType.APPETIZER)),
                                            new Order(1, new Menu("타파스", 5_500, MenuType.APPETIZER))
                                    )
                            )

                    ),
                    Arguments.of(
                            Named.of(
                                    "[티본스테이크:1, 크리스마스파스타:2, 아이스크림:2, 티본스테이크:1]",
                                    List.of(
                                            new Order(1, new Menu("티본스테이크", 55_000, MenuType.MAIN)),
                                            new Order(2, new Menu("크리스마스파스타", 25_000, MenuType.MAIN)),
                                            new Order(2, new Menu("아이스크림", 5_000, MenuType.DESSERT)),
                                            new Order(1, new Menu("티본스테이크", 55_000, MenuType.MAIN))
                                    )
                            )
                    ),
                    Arguments.of(
                            Named.of(
                                    "[양송이수프:3, 시저샐러드:6, 티본스테이크:2, 티본스테이크:4, 레드와인:5]",
                                    List.of(
                                            new Order(3, new Menu("양송이수프", 6_000, MenuType.APPETIZER)),
                                            new Order(6, new Menu("시저샐러드", 8_000, MenuType.APPETIZER)),
                                            new Order(2, new Menu("티본스테이크", 55_000, MenuType.MAIN)),
                                            new Order(4, new Menu("티본스테이크", 55_000, MenuType.MAIN)),
                                            new Order(5, new Menu("레드와인", 60_000, MenuType.BEVERAGE))
                                    )
                            )
                    )
            );
        }

        @DisplayName("전체 Order 된 Menu 들의 주문 수량이 20개를 넘어갈 경우, 예외가 발생한다.")
        @ParameterizedTest(name = "{index} 번째 총 주문 갯수 한계치 초과 주문 = {0}")
        @MethodSource("argumentsForOverTotalQuantity")
        void checkTotalOrderMenuQuantity(List<Order> orders) {
            assertThatThrownBy(() -> orderRepository.insert(orders))
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(OrderException.class)
                    .hasMessageContaining(OrderError.TOO_MANY_MENU_QUANTITY.getErrorMessage());
        }
        private static Stream<Arguments> argumentsForOverTotalQuantity() {
            return Stream.of(
                    Arguments.of(
                            Named.of(
                                    "[해산물파스타:19, 제로콜라:2]",
                                    List.of(
                                            new Order(19, new Menu("해산물파스타", 35_000, MenuType.MAIN)),
                                            new Order(2, new Menu("제로콜라", 3_000, MenuType.BEVERAGE))
                                    )
                            )
                    ),
                    Arguments.of(
                            Named.of(
                                    "[티본스테이크:5, 제로콜라:5, 레드와인:5, 샴페인:6]",
                                    List.of(
                                            new Order(5, new Menu("티본스테이크", 55_000, MenuType.MAIN)),
                                            new Order(5, new Menu("제로콜라", 3_000, MenuType.BEVERAGE)),
                                            new Order(5, new Menu("레드와인", 60_000, MenuType.BEVERAGE)),
                                            new Order(6, new Menu("샴페인", 25_000, MenuType.BEVERAGE))
                                    )
                            )
                    ),
                    Arguments.of(
                            Named.of(
                                    "[시저샐러드:8, 바비큐립:5, 크리스마스파스타:4, 제로콜라:10]",
                                    List.of(
                                            new Order(8, new Menu("시저샐러드", 8_000, MenuType.APPETIZER)),
                                            new Order(5, new Menu("바비큐립", 54_000, MenuType.MAIN)),
                                            new Order(4, new Menu("크리스마스파스타", 25_000, MenuType.MAIN)),
                                            new Order(10, new Menu("제로콜라", 3_000, MenuType.BEVERAGE)))
                            )
                    )
            );
        }
    }

}