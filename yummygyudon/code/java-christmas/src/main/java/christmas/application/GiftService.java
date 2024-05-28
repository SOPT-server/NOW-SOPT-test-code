package christmas.application;

import christmas.domain.entity.Menu;
import christmas.domain.enums.Badge;
import christmas.domain.repository.MenuRepository;
import christmas.global.constant.Standard;


class GiftService {
    private static final int GIVEAWAY_PROVIDE_QUANTITY = 1;

    GiftService() {
    }

    /** 증정 Menu(Giveaway) 관련 기능 */
    boolean isGiveaway(int totalAmount) {
        return totalAmount >= Standard.MINIMUM_AMOUNT_FOR_GIVEAWAY_EVENT;
    }

    String getGiveawayMenuName() {
        Menu giveawayMenu = MenuRepository.findMenuByName(Standard.GIVEAWAY_MENU_NAME);
        return giveawayMenu.getName();
    }

    int getGiveawayMenuPrice() {
        Menu giveawayMenu = MenuRepository.findMenuByName(Standard.GIVEAWAY_MENU_NAME);
        return giveawayMenu.getPrice();
    }

    int getGiveawayMenuQuantity() {
        return GIVEAWAY_PROVIDE_QUANTITY;
    }


    /** 증정 Badge 관련 기능 */
    boolean isBadge(int discountAmount) {
        return discountAmount >= Standard.MINIMUM_AMOUNT_FOR_BADGE_EVENT;
    }

    String receiveBadgeName(int discountAmount) {
        if (discountAmount >= Badge.SANTA.getPriceCriteria()) {
            return Badge.SANTA.getName();
        }
        if (discountAmount >= Badge.TREE.getPriceCriteria()) {
            return Badge.TREE.getName();
        }
        if (discountAmount >= Badge.STAR.getPriceCriteria()) {
            return Badge.STAR.getName();
        }
        return Badge.NONE.getName();
    }
}
