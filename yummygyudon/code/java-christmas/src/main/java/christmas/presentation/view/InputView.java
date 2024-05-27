package christmas.presentation.view;

import christmas.global.channel.Printer;
import christmas.global.channel.Reader;
import christmas.global.constant.Regex;
import christmas.global.constant.Standard;
import christmas.global.exception.GlobalError;
import christmas.global.exception.GlobalException;
import christmas.global.message.Ask;

import java.util.ArrayList;
import java.util.List;

public class InputView {

    /** 방문 날짜를 입력받습니다. */
    public int readDate() {
        Printer.print(Ask.VISIT_DATE);

        String input = Reader.read();
        validateDateInput(input);

        int date = Integer.parseInt(input);
        validateDate(date);
        return date;
    }

    private void validateDateInput(String dateInput) {
        if (dateInput.isBlank() || dateInput.isEmpty()) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_DATE_INPUT_FORMAT);
        }
        if (!dateInput.matches(Regex.REGEX_PATTERN_FOR_DATE)) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_DATE_INPUT_FORMAT);
        }

    }

    private void validateDate(int date) {
        if (date < Standard.FIRST_DATE_OF_DECEMBER || date > Standard.LAST_DATE_OF_DECEMBER) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_DATE_INPUT_FORMAT);
        }
    }

    /** 주문 메뉴와 수량을 입력받습니다. */
    public List<MenuInput> readMenuAndQuantity() {
        Printer.print(Ask.MENU);

        String input = Reader.read();
        validateMenuInput(input);

        return splitMenuInput(input);
    }

    private void validateMenuInput(String menuInput) {
        if (menuInput.isBlank() || menuInput.isEmpty()) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_MENU_INPUT_FORMAT);
        }
        if (!menuInput.matches(Regex.REGEX_PATTERN_FOR_MENU_INPUT)) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_MENU_INPUT_FORMAT);
        }
    }

    private List<MenuInput> splitMenuInput(String menuInput) {
        String[] inputMenus = menuInput.split(Standard.MENU_INPUT_SEPARATOR_FOR_ORDER);
        List<MenuInput> result = new ArrayList<>();

        for (String eachMenu : inputMenus) {
            eachMenu = eachMenu.trim();
            String[] nameAndQuantity = eachMenu.split(Standard.MENU_INPUT_SEPARATOR_FOR_NAME_AND_QUANTITY);

            String name = nameAndQuantity[0].trim();
            int quantity = Integer.parseInt(nameAndQuantity[1].trim());
            result.add(new MenuInput(name, quantity));
        }
        return result;
    }

    public record MenuInput(String name, int quantity) { }

}
