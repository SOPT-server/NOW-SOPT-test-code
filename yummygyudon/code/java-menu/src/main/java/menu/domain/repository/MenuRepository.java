package menu.domain.repository;

import menu.domain.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    private static final List<Menu> menus = new ArrayList<>();

    static {
        addMenusOf("한식","김밥", "김치찌개", "쌈밥", "된장찌개", "비빔밥", "칼국수", "불고기", "떡볶이", "제육볶음");
        addMenusOf("일식", "규동", "우동", "미소시루", "스시", "가츠동", "오니기리", "하이라이스", "라멘", "오코노미야끼");
        addMenusOf("중식", "깐풍기", "볶음면", "동파육", "짜장면", "짬뽕", "마파두부", "탕수육", "토마토 달걀볶음", "고추잡채");
        addMenusOf("아시안", "팟타이", "카오 팟", "나시고렝", "파인애플 볶음밥", "쌀국수", "똠얌꿍", "반미", "월남쌈", "분짜");
        addMenusOf("양식", "라자냐", "그라탱", "뇨끼", "끼슈", "프렌치 토스트", "바게트", "스파게티", "피자", "파니니");
    }

    private static void addMenusOf(String category, String... menuNames) {
        for (String menuName : menuNames) {
            menus.add(new Menu(menuName, category));
        }
    }

    public static boolean isExist(String menuName) {
        return menus.stream()
                .anyMatch(menu -> menu.getName().equals(menuName));
    }

    public static List<String> getMenusOf(String category) {
        return menus.stream()
                .filter(menu -> menu.getCategory().equals(category))
                .map(Menu::getName)
                .toList();
    }

}
