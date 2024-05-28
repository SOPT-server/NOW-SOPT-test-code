package christmas.domain.repository;

import christmas.domain.entity.Order;
import christmas.domain.enums.MenuType;
import christmas.domain.exception.OrderException;
import christmas.domain.exception.error.OrderError;
import christmas.global.constant.Standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderRepository {
    private final List<Order> orders ;

    public OrderRepository() {
        orders = new ArrayList<>();
    }


    // insert
    public void insert(List<Order> orders) {
        validate(orders);
        this.orders.addAll(orders);
    }

    private void validate(List<Order> orders) {
        checkOnlyBeverage(orders);
        checkDuplicatedMenuOrder(orders);
        checkTotalOrderMenuQuantity(orders);
    }
    public void checkOnlyBeverage(List<Order> orders) {
        long beverageCount = orders.stream()
                .filter(order -> order.getMenu().getType().equals(MenuType.BEVERAGE))
                .count();
        if (orders.size() == beverageCount) {
            throw new OrderException(OrderError.ONLY_BEVERAGE);
        }
    }

    public void checkDuplicatedMenuOrder(List<Order> orders) {
        long uniqueOrderCount = orders.stream()
                .map(order -> order.getMenu().getName())
                .distinct()
                .count();
        if (orders.size() != uniqueOrderCount) {
            throw new OrderException(OrderError.DUPLICATED_ORDER_EXIST);
        }
    }

    public void checkTotalOrderMenuQuantity(List<Order> orders) {
        int totalOrderQuantity = orders.stream()
                .mapToInt(Order::getQuantity)
                .sum();
        if (totalOrderQuantity > Standard.MAXIMUM_QUANTITY_FOR_ORDER) {
            throw new OrderException(OrderError.TOO_MANY_SINGLE_MENU_QUANTITY);
        }
    }


    // select
    public List<Order> findAll() {
        return Collections.unmodifiableList(orders);
    }

}
