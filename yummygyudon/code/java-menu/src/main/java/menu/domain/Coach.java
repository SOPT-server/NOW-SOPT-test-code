package menu.domain;

import menu.domain.exception.CoachError;
import menu.domain.exception.CoachException;
import menu.global.constant.Standard;

import java.util.ArrayList;
import java.util.List;

public class Coach {

    private final String name;
    private List<String> impossibleMenus;
    private final List<String> menus;

    public Coach(String name) {
        if (name.length() < Standard.MINIMUM_LENGTH_OF_COACH_NAME) {
            throw new CoachException(CoachError.TOO_SHORT_NAME);
        }
        if (name.length() > Standard.MAXIMUM_LENGTH_OF_COACH_NAME) {
            throw new CoachException(CoachError.TOO_LONG_NAME);
        }
        this.name = name;
        this.menus = new ArrayList<>();
        this.impossibleMenus = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getMenus() {
        return menus;
    }

    public void changeImpossibleMenus(List<String> impossibleMenus) {
        if (impossibleMenus.size() > Standard.MAXIMUM_NUMBER_OF_IMPOSSIBLE_MENU) {
            throw new CoachException(CoachError.TOO_MANY_IMPOSSIBLE_MENUS_OF_COACH);
        }
        this.impossibleMenus = impossibleMenus;
    }

    public boolean isPossibleToEat(String menuName) {
        return !this.impossibleMenus.contains(menuName);
    }

    public void addMenu(String menu) {
        menus.add(menu);
    }
}
