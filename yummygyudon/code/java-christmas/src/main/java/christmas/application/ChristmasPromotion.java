package christmas.application;

import christmas.application.info.EventResultInfo;
import christmas.domain.enums.EventDiscountType;
import christmas.domain.enums.MenuType;
import christmas.global.constant.Standard;
import christmas.presentation.view.InputView;

import java.util.List;

public class ChristmasPromotion {

    private static final String GIVEAWAY_EVENT_NAME = "증정 이벤트";

    private final int planningDate;
    private final GiftService giftService;
    private final DiscountService discountService;
    private final OrderService orderService;

    public ChristmasPromotion(int date) {
        this.planningDate = date;
        this.giftService = new GiftService();
        this.discountService = new DiscountService();
        this.orderService = new OrderService();
    }

    /** 주문합니다.*/
    public void order(List<InputView.MenuInput> menuInputs) {
        orderService.registerOrders(menuInputs);
    }

    /** 플래너의 정보를 반환합니다.*/
    public EventResultInfo.DateInfo viewPlannerInfo() {
        return new EventResultInfo.DateInfo(Standard.MONTH_OF_DECEMBER, planningDate);
    }

    /** 주문 메뉴를 반환합니다. */
    public List<EventResultInfo.ReceiveMenu> viewOrderedMenusHistory() {
        return orderService.getOrdersHistory();
    }


    /** 할인 전 총주문 금액을 반환합니다. */
    public EventResultInfo.Amount viewTotalAmountPaidBeforeDiscount() {
        return new EventResultInfo.Amount(orderService.getTotalAmount());
    }

    /** 증정 메뉴를 반환합니다. */
    public EventResultInfo.ReceiveMenu viewGiveawayMenu() {
        if (!giftService.isGiveaway(orderService.getTotalAmount())) {
            return null;
        }
        String giveawayMenuName = giftService.getGiveawayMenuName();
        int providedQuantity = giftService.getGiveawayMenuQuantity();
        return new EventResultInfo.ReceiveMenu(giveawayMenuName, providedQuantity);
    }

    /** 크리스마스 디데이 혜택 내역을 반환합니다. */
    public EventResultInfo.Benefit viewChristmasPeriodEventBenefit() {
        if (orderService.getTotalAmount() < Standard.MINIMUM_AMOUNT_FOR_ALL_EVENT) {
            return null;
        }
        if (!discountService.isInChristmasDiscountEventPeriod(planningDate)) {
            return null;
        }
        int totalChristmasEventDiscountAmount = discountService.getTotalChristmasEventDiscountAmount(planningDate);
        return makeBenefitInfo(EventDiscountType.CHRISTMAS.getName(), totalChristmasEventDiscountAmount);
    }

    /** 주일/주말 혜택 내역을 반환합니다. */
    public EventResultInfo.Benefit viewWeekEventBenefit() {
        if (orderService.getTotalAmount() < Standard.MINIMUM_AMOUNT_FOR_ALL_EVENT) {
            return null;
        }
        return getWeekEventBenefit();
    }

    private EventResultInfo.Benefit getWeekEventBenefit() {
        EventResultInfo.Benefit result = null;
        if (discountService.isInWeekdays(planningDate) && orderService.isExistAnyMenuTypeInOrders(MenuType.DESSERT)) {
            int dessertMenuCount = orderService.getOrdersCountByMenuType(MenuType.DESSERT);
            int totalWeekdayDiscountAmount = discountService.getTotalWeekDiscountAmount(EventDiscountType.WEEKDAY, dessertMenuCount);
            result = makeBenefitInfo(EventDiscountType.WEEKDAY.getName(), totalWeekdayDiscountAmount);
        }
        if (discountService.isInWeekends(planningDate) && orderService.isExistAnyMenuTypeInOrders(MenuType.MAIN)) {
            int mainMenuCount = orderService.getOrdersCountByMenuType(MenuType.MAIN);
            int totalWeekendDiscountAmount = discountService.getTotalWeekDiscountAmount(EventDiscountType.WEEKEND, mainMenuCount);
            result = makeBenefitInfo(EventDiscountType.WEEKEND.getName(), totalWeekendDiscountAmount);
        }
        return result;
    }

    /** 특별 혜택 내역을 반환합니다. */
    public EventResultInfo.Benefit viewSpecialDiscountEventBenefit() {
        if (orderService.getTotalAmount() < Standard.MINIMUM_AMOUNT_FOR_ALL_EVENT) {
            return null;
        }
        if (!discountService.isInSpecialDiscountEventDays(planningDate)) {
            return null;
        }
        int totalSpecialDiscountAmount = discountService.getTotalSpecialDiscountAmount();
        return makeBenefitInfo(EventDiscountType.SPECIAL.getName(), totalSpecialDiscountAmount);
    }

    /** 증정 혜택 내역을 반환합니다. */
    public EventResultInfo.Benefit viewGiveawayBenefit() {
        if (!giftService.isGiveaway(orderService.getTotalAmount())) {
            return null;
        }
        int giveawayMenuPrice = giftService.getGiveawayMenuPrice();
        return makeBenefitInfo(GIVEAWAY_EVENT_NAME, giveawayMenuPrice);
    }

    // Benefit DTO로 만들어주는 생성 메서드
    private EventResultInfo.Benefit makeBenefitInfo(String benefitName, int benefitAmount) {
        return new EventResultInfo.Benefit(benefitName, benefitAmount);
    }


    /** 총 혜택 금액을 반환합니다. */
    public EventResultInfo.Amount viewTotalBenefitAmount() {
        if (orderService.getTotalAmount() < Standard.MINIMUM_AMOUNT_FOR_ALL_EVENT) {
            return null;
        }
        int totalBenefitAmount = combineAllDiscountAmount();
        if (giftService.isGiveaway(orderService.getTotalAmount())) {
            totalBenefitAmount += giftService.getGiveawayMenuPrice();
        }
        return new EventResultInfo.Amount(totalBenefitAmount);
    }


    /** 할인 후 예상 결제 금액을 반환합니다. */
    public EventResultInfo.Amount viewTotalAmountPaidAfterDiscount() {
        if (orderService.getTotalAmount() < Standard.MINIMUM_AMOUNT_FOR_ALL_EVENT) {
            return new EventResultInfo.Amount(orderService.getTotalAmount());
        }
        return new EventResultInfo.Amount(orderService.getTotalAmount() - combineAllDiscountAmount());
    }

    // 전체 혜택 금액을 연산합니다.
    private int combineAllDiscountAmount() {
        int totalDiscountAmount = 0;
        if (discountService.isInChristmasDiscountEventPeriod(planningDate)) {
            totalDiscountAmount += discountService.getTotalChristmasEventDiscountAmount(planningDate);
        }
        if (discountService.isInWeekdays(planningDate)) {
            int dessertMenuCount = orderService.getOrdersCountByMenuType(MenuType.DESSERT);
            totalDiscountAmount += discountService.getTotalWeekDiscountAmount(EventDiscountType.WEEKDAY, dessertMenuCount);
        }
        if (discountService.isInWeekends(planningDate)) {
            int mainMenuCount = orderService.getOrdersCountByMenuType(MenuType.MAIN);
            totalDiscountAmount += discountService.getTotalWeekDiscountAmount(EventDiscountType.WEEKEND, mainMenuCount);
        }
        if (discountService.isInSpecialDiscountEventDays(planningDate)) {
            totalDiscountAmount +=  discountService.getTotalSpecialDiscountAmount();
        }
        return totalDiscountAmount;
    }

    /** 이벤트 뱃지를 반환합니다. */
    public EventResultInfo.Badge viewBadge() {
        int totalDiscountAmount = combineAllDiscountAmount();
        if (!giftService.isBadge(totalDiscountAmount)) {
            return null;
        }
        String badgeName = giftService.receiveBadgeName(totalDiscountAmount);
        return new EventResultInfo.Badge(badgeName);
    }
}
