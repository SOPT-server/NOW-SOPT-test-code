package christmas.domain.entity;

import christmas.domain.exception.OrderException;
import christmas.domain.exception.error.OrderError;
import christmas.global.constant.Standard;

public class Order {

    private final int quantity;
    private final Menu menu;

    public Order(int quantity, Menu menu) {
        validateQuantity(quantity);
        this.quantity = quantity;
        this.menu = menu;
    }

    private void validateQuantity(int quantity) {
        // 최대 갯수 검사
        if (quantity > Standard.MAXIMUM_QUANTITY_FOR_ORDER) {
            throw new OrderException(OrderError.TOO_MANY_SINGLE_MENU_QUANTITY);
        }
        // 0개 검사
        if (quantity < Standard.MINIMUM_QUANTITY_FOR_ORDER) {
            throw new OrderException(OrderError.TOO_LITTLE_SINGLE_MENU_QUANTITY);
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public int calculateTotalPrice() {
        return quantity * menu.getPrice();
    }

}
