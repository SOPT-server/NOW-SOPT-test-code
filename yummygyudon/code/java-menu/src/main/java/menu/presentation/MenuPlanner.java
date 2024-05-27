package menu.presentation;


import menu.application.MenuPlanService;
import menu.application.PlannerInfo;
import menu.global.exception.base.MenuPlannerException;
import menu.presentation.view.InputView;
import menu.presentation.view.OutputView;

import java.util.List;

public class MenuPlanner {

    private final InputView inputView;
    private final OutputView outputView;
    private final MenuPlanService menuPlanService;

    public MenuPlanner() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        List<String> coachNames = inputView.askCoachNames();
        this.menuPlanService = new MenuPlanService(coachNames);
    }

    public void startPlan() {
        List<String> coachNames = menuPlanService.getCoachNames();
        for (String coachName : coachNames) {
            boolean isSuccessToRegister;
            do {
                isSuccessToRegister = registerImpossibleMenus(coachName);
            } while (!isSuccessToRegister);
        }
        viewResultOfPlan();
    }

    private boolean registerImpossibleMenus(String coachName) {
        try {
            List<String> impossibleMenus = inputView.askImpossibleMenusOf(coachName);
            menuPlanService.registerImpossibleMenusOf(coachName, impossibleMenus);
            return true;
        } catch (MenuPlannerException plannerException) {
            outputView.printException(plannerException);
            return false;
        }
    }
    private void viewResultOfPlan() {
        outputView.printResultHead();
        PlannerInfo.WeeklyCategory categories = menuPlanService.planCategoryOfWeek();
        outputView.printCategoryOfWeek(categories);

        menuPlanService.planMenuForAllCoachOfWeek(categories);
        List<PlannerInfo.CoachMenu> coachMenus = menuPlanService.makeWeeklyMenuBoard();
        for (PlannerInfo.CoachMenu coachMenu : coachMenus) {
            outputView.printCoachMenuOfWeek(coachMenu);
        }

        outputView.printFinishing();
    }

}
