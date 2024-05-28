package menu.domain.repository;


import java.util.Map;

public class CategoryRepository {

    private static final Map<Integer, String> categories = Map.of(
            1, "일식",
            2, "한식",
            3, "중식",
            4, "아시안",
            5, "양식"
    );

    // read-only
    public static Map<Integer, String> categories() {
        return categories;
    }

}
