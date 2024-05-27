package menu.application;

import menu.domain.Coach;
import menu.domain.repository.CoachRepository;
import menu.global.constant.Standard;

import java.util.ArrayList;
import java.util.List;

public class MenuPlanService {
    private final MenuService menuService;
    private final CategoryService categoryService;

    public MenuPlanService(List<String> coachNames) {
        CoachRepository.init(coachNames);
        this.menuService = new MenuService();
        this.categoryService = new CategoryService();
    }

    public List<String> getCoachNames() {
        return CoachRepository.coaches().stream()
                .map(Coach::getName)
                .toList();
    }

    public void registerImpossibleMenusOf(String coachName, List<String> menus) {
        menuService.checkIsMenuExist(menus);
        for (Coach coach : CoachRepository.coaches()) {
            if (coach.getName().equals(coachName)) {
                coach.changeImpossibleMenus(menus);
                break;
            }
        }
    }

    public PlannerInfo.WeeklyCategory planCategoryOfWeek() {
        List<String> randomCategories = new ArrayList<>();
        while (randomCategories.size() < Standard.DATE_LIMIT_FOR_PLAN) {
            String randomCategory = categoryService.getRandomCategory();
            if (isTooMuchCategoryDuplicate(randomCategory, randomCategories)) {
                randomCategories.add(randomCategory);
            }
        }
        return PlannerInfo.WeeklyCategory.of(randomCategories);
    }
    private boolean isTooMuchCategoryDuplicate(String targetCategory, List<String> existCategories) {
        long numberOfSameCategory = existCategories.stream()
                .filter(category -> category.equals(targetCategory))
                .count();
        return numberOfSameCategory < 2;
    }

    public void planMenuForAllCoachOfWeek(PlannerInfo.WeeklyCategory weeklyCategory) {
        for (String category : weeklyCategory.getCategories()) {
            for (Coach coach : CoachRepository.coaches()) {
                planCoachMenuOfDay(coach, category);
            }
        }
    }




    private void planCoachMenuOfDay(Coach coach, String categoryOfDay) {
        String menu;
        do {
            menu = planCoachMenu(coach, categoryOfDay);
        } while (isMenuDuplicate(menu, coach.getMenus()));
        coach.addMenu(menu);
    }
    private String planCoachMenu(Coach coach, String category) {
        String randomMenu;
        do {
            randomMenu = menuService.pickRandomMenuOf(category);
        } while (!coach.isPossibleToEat(randomMenu));
        return randomMenu;
    }
    private boolean isMenuDuplicate(String targetMenu, List<String> existMenus) {
        return existMenus.contains(targetMenu);
    }

    public List<PlannerInfo.CoachMenu> makeWeeklyMenuBoard() {
        List<PlannerInfo.CoachMenu> planResult = new ArrayList<>();
        for (Coach coach : CoachRepository.coaches()) {
            PlannerInfo.CoachMenu coachMenu = PlannerInfo.CoachMenu.of(coach.getName(), coach.getMenus());
            planResult.add(coachMenu);
        }
        return planResult;
    }
}
