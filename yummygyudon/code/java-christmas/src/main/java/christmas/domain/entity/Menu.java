package christmas.domain.entity;

import christmas.domain.enums.MenuType;

public class Menu {

    private final String name;
    private final int price;
    private final MenuType type;

    public Menu(String name, int price, MenuType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public MenuType getType() {
        return type;
    }
}
