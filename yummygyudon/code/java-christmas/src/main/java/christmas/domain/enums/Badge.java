package christmas.domain.enums;

public enum Badge {
    NONE(null, 0),
    STAR("별", 5_000),
    TREE("트리", 10_000),
    SANTA("산타", 20_000);

    private final String name;
    private final int priceCriteria;

    Badge(String name, int priceCriteria) {
        this.name = name;
        this.priceCriteria = priceCriteria;
    }

    public String getName() {
        return name;
    }

    public int getPriceCriteria() {
        return priceCriteria;
    }
}
