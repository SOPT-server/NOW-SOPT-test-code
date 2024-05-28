package menu.application;

import java.util.List;

/**
 * 시험 땐 Record로 써야할 듯...
 * 오해할 수도..
 */
public abstract class PlannerInfo {

    public static class WeeklyCategory {
        private final List<String> categories;

        public List<String> getCategories() {
            return categories;
        }

        private WeeklyCategory(List<String> categories) {
            this.categories = categories;
        }

        public static WeeklyCategory of(List<String> categories) {
            return new WeeklyCategory(categories);
        }
    }

    public static class CoachMenu {
        private final String coachName;
        private final List<String> menuNames;

        private CoachMenu(String coachName, List<String> menuNames) {
            this.coachName = coachName;
            this.menuNames = menuNames;
        }

        public String getCoachName() {
            return coachName;
        }

        public List<String> getMenuNames() {
            return menuNames;
        }

        public static CoachMenu of(
                String coachName, List<String> menuNames
        ) {
            return new CoachMenu(coachName, menuNames);
        }
    }
}
