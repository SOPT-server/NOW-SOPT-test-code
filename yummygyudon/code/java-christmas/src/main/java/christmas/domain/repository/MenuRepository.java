package christmas.domain.repository;

import christmas.domain.entity.Menu;
import christmas.domain.enums.MenuType;
import christmas.domain.exception.MenuException;
import christmas.domain.exception.error.MenuError;

import java.util.HashMap;
import java.util.Map;

/**
 * 절댓값인 메뉴 데이터만 들어있는 클래스이기에 생성자를 막아놓습니다.
 */
public abstract class MenuRepository {
    private static final Map<String, Menu> menus;

    static {
        menus = new HashMap<>();
        initAppetizer();
        initMain();
        initDessert();
        initBeverage();
    }

    // init
    private static void initAppetizer() {
        menus.put("양송이수프", new Menu("양송이수프", 6_000, MenuType.APPETIZER));
        menus.put("타파스", new Menu("타파스", 5_500, MenuType.APPETIZER));
        menus.put("시저샐러드", new Menu("시저샐러드", 8_000, MenuType.APPETIZER));
    }
    private static void initMain() {
        menus.put("티본스테이크", new Menu("티본스테이크", 55_000, MenuType.MAIN));
        menus.put("바비큐립", new Menu("바비큐립", 54_000, MenuType.MAIN));
        menus.put("해산물파스타", new Menu("해산물파스타", 35_000, MenuType.MAIN));
        menus.put("크리스마스파스타", new Menu("크리스마스파스타", 25_000, MenuType.MAIN));
    }
    private static void initDessert() {
        menus.put("초코케이크", new Menu("초코케이크", 15_000, MenuType.DESSERT));
        menus.put("아이스크림", new Menu("아이스크림", 5_000, MenuType.DESSERT));
    }
    private static void initBeverage() {
        menus.put("제로콜라", new Menu("제로콜라", 3_000, MenuType.BEVERAGE));
        menus.put("레드와인", new Menu("레드와인", 60_000, MenuType.BEVERAGE));
        menus.put("샴페인", new Menu("샴페인", 25_000, MenuType.BEVERAGE));
    }

    public static  Menu findMenuByName(String name) {
        if (!menus.containsKey(name)) {
            throw new MenuException(MenuError.MENU_NOT_FOUND);
        }
        return menus.get(name);
    }

}
