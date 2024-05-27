package christmas.presentation.view;

import christmas.application.info.EventResultInfo;
import christmas.global.channel.Printer;
import christmas.global.exception.base.ChristmasPlannerException;
import christmas.global.message.Notice;
import christmas.global.message.Unit;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class OutputView {

    private static final String SUMMARY_HEADER_FORMAT = "%d월 %d일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!";
    private static final String BENEFIT_FORMAT = "%s: %s";
    private static final String AMOUNT_FORMAT = "%s" + Unit.CURRENCY;
    private static final String DISCOUNT_AMOUNT_FORMAT = "-%s" + Unit.CURRENCY;
    private static final String MENU_FORMAT = "%s %d" + Unit.MENU_QUANTITY;

    private static final String NO_BENEFIT = "없음";

    /** 예외 출력 기능 */
    public void printException(ChristmasPlannerException exception) {
        Printer.print(exception.getMessage());
    }

    /** 결과 출력 기능 */
    // 플래너 애플리케이션 시작말 출력
    public void printGreeting() {
        Printer.print(Notice.INFO);
    }

    // 혜택 미리보기 시작말 출력
    public void printSummaryHeader(EventResultInfo.DateInfo dateInfo) {
        Printer.print(String.format(SUMMARY_HEADER_FORMAT, dateInfo.month(), dateInfo.date()));

        Printer.printBlankLine();
    }

    // 주문 메뉴 출력
    public void printOrderedMenus(List<EventResultInfo.ReceiveMenu> menus) {
        Printer.print(Notice.ORDERED_MENU_LIST);

        for (EventResultInfo.ReceiveMenu menu : menus) {
            Printer.print(String.format(MENU_FORMAT, menu.name(), menu.quantity()));
        }

        Printer.printBlankLine();
    }

    // 할인 전 총주문 금액 출력
    public void printPaidBeforeDiscount(EventResultInfo.Amount amountBeforeDiscount) {
        Printer.print(Notice.TOTAL_AMOUNT_BEFORE_DISCOUNT);

        Printer.print(String.format(AMOUNT_FORMAT, convertToAmountFormat(amountBeforeDiscount.amount())));

        Printer.printBlankLine();
    }

    // 증정 메뉴 출력
    public void printGiveawayMenu(EventResultInfo.ReceiveMenu menu) {
        Printer.print(Notice.GIVEAWAY_MENU);

        if (Objects.isNull(menu)) {
            Printer.print(NO_BENEFIT);
            Printer.printBlankLine();
            return;
        }
        Printer.print(String.format(MENU_FORMAT, menu.name(), menu.quantity()));

        Printer.printBlankLine();
    }

    // 혜택 내역 출력
    public void printBenefits(List<EventResultInfo.Benefit> benefits) {
        Printer.print(Notice.BENEFIT_LIST);

        if (benefits.stream().allMatch(Objects::isNull)) {
            // 모든 데이터가 Null 일 경우
            Printer.print(NO_BENEFIT);
            Printer.printBlankLine();
            return;
        }

        for (EventResultInfo.Benefit benefit : benefits) {
            if (!Objects.isNull(benefit)){
                String discountResult = String.format(DISCOUNT_AMOUNT_FORMAT, convertToAmountFormat(benefit.discountAmount()));
                Printer.print(String.format(BENEFIT_FORMAT, benefit.name(), discountResult));
            }
        }
        Printer.printBlankLine();
    }

    // 총혜택 금액 출력
    public void printTotalDiscountAmount(EventResultInfo.Amount totalDiscountAmount) {
        Printer.print(Notice.TOTAL_BENEFIT_AMOUNT);

        if (Objects.isNull(totalDiscountAmount)) {
            Printer.print(String.format(AMOUNT_FORMAT, 0));
            Printer.printBlankLine();
            return;
        }
        Printer.print(String.format(DISCOUNT_AMOUNT_FORMAT, convertToAmountFormat(totalDiscountAmount.amount())));

        Printer.printBlankLine();
    }

    // 할인 후 예상 결제 금액 출력
    public void printPaidAfterDiscount(EventResultInfo.Amount amountAfterDiscount) {
        Printer.print(Notice.TOTAL_AMOUNT_AFTER_DISCOUNT);

        Printer.print(String.format(AMOUNT_FORMAT, convertToAmountFormat(amountAfterDiscount.amount())));

        Printer.printBlankLine();
    }

    // 12월 이벤트 배지 출력
    public void printBadge(EventResultInfo.Badge badge) {
        Printer.print(Notice.EVENT_BADGE);

        if (Objects.isNull(badge)) {
            Printer.print(NO_BENEFIT);
            return;
        }

        Printer.print(badge.badgeName());
    }

    /** 천 원 단위 쉼표(,) 처리 기능 */
    private String convertToAmountFormat(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(amount);
    }

}
