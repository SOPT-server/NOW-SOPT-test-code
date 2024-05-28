package christmas.domain.enums;

public enum EventDiscountType {
    WEEKDAY("평일 할인", 2_023),
    WEEKEND("주말 할인", 2_023),
    SPECIAL("특별 할인", 1_000),
    CHRISTMAS("크리스마스 디데이 할인", 1_000),
    ;

    private final String name;
    private final int discountAmount;


    EventDiscountType(String name, int discountAmount) {
        this.name = name;
        this.discountAmount = discountAmount;
    }

    public String getName() {
        return name;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
