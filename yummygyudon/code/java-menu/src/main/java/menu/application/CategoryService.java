package menu.application;

import camp.nextstep.edu.missionutils.Randoms;
import menu.domain.repository.CategoryRepository;

import java.util.Map;

class CategoryService {

    public String getRandomCategory() {
        Map<Integer, String> categories = CategoryRepository.categories();
        return categories.get(Randoms.pickNumberInRange(1, 5));
    }
}
