package menu.presentation.view;

import menu.global.channel.Printer;
import menu.global.channel.Reader;
import menu.global.constant.Regex;
import menu.global.constant.Standard;
import menu.global.exception.GlobalError;
import menu.global.exception.GlobalException;
import menu.global.message.Ask;
import menu.global.message.Notice;

import java.util.ArrayList;
import java.util.List;

public class InputView {

    public List<String> askCoachNames() {
        Printer.print(Notice.START_RECOMMEND);
        Printer.printBlankLine();
        Printer.print(Ask.COACH_NAMES);

        String inputValue = Reader.read().trim();
        validateInputPattern(inputValue);
        Printer.printBlankLine();
        return splitInputValue(inputValue);
    }

    public List<String> askImpossibleMenusOf(String coachName) {
        Printer.print(coachName + Ask.IMPOSSIBLE_MENUS);

        String inputValue = Reader.read().trim();
        validateInputPattern(inputValue);
        Printer.printBlankLine();
        return splitInputValue(inputValue);
    }

    private void validateInputPattern(String input) {
        if (!input.matches(Regex.REGEX_PATTERN_FOR_COACH_NAME_INPUT)) {
            throw new GlobalException(GlobalError.NOT_AVAILABLE_INPUT_PATTERN);
        }
    }

    private List<String> splitInputValue(String inputValue) {
        String[] values = inputValue.split(Standard.NAME_INPUT_SEPARATOR);
        List<String> result = new ArrayList<>();
        for (String value : values) {
            result.add(value.trim());
        }
        return result;
    }

}
