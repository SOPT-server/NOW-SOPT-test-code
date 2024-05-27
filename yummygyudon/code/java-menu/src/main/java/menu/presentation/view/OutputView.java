package menu.presentation.view;

import menu.application.PlannerInfo;
import menu.global.channel.Printer;
import menu.global.constant.Standard;
import menu.global.constant.Variable;
import menu.global.exception.base.MenuPlannerException;
import menu.global.message.Notice;

import java.util.StringJoiner;

public class OutputView {

    /** 예외 출력 기능 */
    public void printException(MenuPlannerException exception) {
        Printer.print(exception.getMessage());
    }

    /** 결과 출력 기능 */
    public void printResultHead() {
        Printer.print(Notice.RESULT_RECOMMEND);
        Printer.print(Notice.RESULT_RECOMMEND_HEADLINE);
    }


    public void printCategoryOfWeek(PlannerInfo.WeeklyCategory weeklyCategory) {
        StringJoiner joiner = new StringJoiner(
                Standard.RESULT_RECOMMEND_SEPARATOR, Standard.RESULT_RECOMMEND_PREFIX, Standard.RESULT_RECOMMEND_SUFFIX
        );
        joiner.add(Variable.KOR_CATEGORY);
        for (String category : weeklyCategory.getCategories()) {
            joiner.add(category);
        }
        Printer.print(joiner.toString());
    }
    public void printCoachMenuOfWeek(PlannerInfo.CoachMenu coachMenu) {
        StringJoiner joiner = new StringJoiner(
                Standard.RESULT_RECOMMEND_SEPARATOR, Standard.RESULT_RECOMMEND_PREFIX, Standard.RESULT_RECOMMEND_SUFFIX
        );
        joiner.add(coachMenu.getCoachName());
        for (String menuName : coachMenu.getMenuNames()) {
            joiner.add(menuName);
        }
        Printer.print(joiner.toString());
    }
    public void printFinishing() {
        Printer.printBlankLine();
        Printer.print(Notice.FINISH_RECOMMEND);
    }

}
