package menu.domain;

public class Menu {
    private final String name;
    private final String category;

    public Menu(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
