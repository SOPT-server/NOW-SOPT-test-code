package menu.application;

import camp.nextstep.edu.missionutils.Randoms;
import menu.domain.exception.MenuError;
import menu.domain.exception.MenuException;
import menu.domain.repository.MenuRepository;

import java.util.List;

class MenuService {

    public void checkIsMenuExist(List<String> menus) {
        for (String menuName : menus) {
            boolean exist = MenuRepository.isExist(menuName);
            if (!exist) {
                throw new MenuException(MenuError.MENU_NOT_FOUND);
            }
        }
    }
    public String pickRandomMenuOf(String category) {
        List<String> menusOfCategory = MenuRepository.getMenusOf(category);
        return Randoms.shuffle(menusOfCategory).get(0);
    }
}
