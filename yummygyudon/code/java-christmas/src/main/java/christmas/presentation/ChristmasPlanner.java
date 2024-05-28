package christmas.presentation;

import christmas.application.ChristmasPromotion;
import christmas.application.info.EventResultInfo;
import christmas.global.exception.base.ChristmasPlannerException;
import christmas.presentation.view.InputView;
import christmas.presentation.view.OutputView;

import java.util.ArrayList;
import java.util.List;

public class ChristmasPlanner {

    private final ChristmasPromotion eventService;
    private final InputView inputView;
    private final OutputView outputView;


    public ChristmasPlanner() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.eventService = new ChristmasPromotion(registerDate());
    }


    /**
     * 플래너 객체 자체가 등록일자 기반으로 행동해야 하기 때문에<br/>
     * 생성 시점에 입력을 받아 플래너를 생성합니다.
     *
     * @return 예약일
     * @see ChristmasPlanner#ChristmasPlanner()
     */
    private int registerDate() {
        boolean isRegistered = false;
        int date = 0;
        outputView.printGreeting();
        while (!isRegistered) {
            try {
                date = inputView.readDate();
                isRegistered = true;
            } catch (ChristmasPlannerException exception) {
                outputView.printException(exception);
            }
        }
        return date;
    }

    /**
     * 해당 구현체의 유일한 public 메서드로서 <br/>
     * 해당 메서드 호출에 따라 전체 기능을 수행합니다.
     */
    public void run() {
        order();

        showPlannerHeader();

        showPlanSummary();
    }

    /** 주문 처리를 수행합니다. */
    private void order() {
        boolean isOrdered = false;
        while (!isOrdered) {
            try {
                List<InputView.MenuInput> menuInputs = inputView.readMenuAndQuantity();
                eventService.order(menuInputs);
                isOrdered = true;
            } catch (ChristmasPlannerException exception) {
                outputView.printException(exception);
            }
        }
    }

    /** 플래너 상단 헤더를 출력합니다. */
    private void showPlannerHeader() {
        EventResultInfo.DateInfo dateInfo = eventService.viewPlannerInfo();

        outputView.printSummaryHeader(dateInfo);
    }
    
    /** 플래너 결과를 정리하여 출력합니다. */
    private void showPlanSummary() {
        showOrderDetails();
        showBenefits();
        showAppliedResult();
    }

    // 사용자의 주문 내역 및 결제 금액을 출력합니다.
    private void showOrderDetails() {
        List<EventResultInfo.ReceiveMenu> orderedMenus = eventService.viewOrderedMenusHistory();
        outputView.printOrderedMenus(orderedMenus);

        EventResultInfo.Amount totalAmountBeforeDiscount = eventService.viewTotalAmountPaidBeforeDiscount();
        outputView.printPaidBeforeDiscount(totalAmountBeforeDiscount);
    }

    // 각종 혜택 정보를 출력합니다.
    private void showBenefits() {
        EventResultInfo.ReceiveMenu giveawayMenu = eventService.viewGiveawayMenu();
        outputView.printGiveawayMenu(giveawayMenu);

        List<EventResultInfo.Benefit> benefits = new ArrayList<>();
        benefits.add(eventService.viewChristmasPeriodEventBenefit());
        benefits.add(eventService.viewWeekEventBenefit());
        benefits.add(eventService.viewSpecialDiscountEventBenefit());
        benefits.add(eventService.viewGiveawayBenefit());

        outputView.printBenefits(benefits);
    }

    // 총 혜택 금액과 최종 결제 금액 및 배지 정보를 출력합니다.
    private void showAppliedResult() {
        EventResultInfo.Amount totalDiscountAmount = eventService.viewTotalBenefitAmount();
        outputView.printTotalDiscountAmount(totalDiscountAmount);

        EventResultInfo.Amount totalAmountPaid = eventService.viewTotalAmountPaidAfterDiscount();
        outputView.printPaidAfterDiscount(totalAmountPaid);

        EventResultInfo.Badge badge = eventService.viewBadge();
        outputView.printBadge(badge);
    }
}
