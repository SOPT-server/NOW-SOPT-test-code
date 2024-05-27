package christmas.global.constant;

public abstract class Regex {

    /** 입력 형식*/
    // 날짜 입력
    public static String REGEX_PATTERN_FOR_DATE = "\\d{0,2}";

    // 메뉴 및 수량 입력
    public static String REGEX_PATTERN_FOR_MENU_INPUT = "^(([\\s]*[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+[\\s]*)\\-([\\s]*[\\d]{0,2}[\\s]*))(,(([\\s]*[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+[\\s]*)\\-([\\s]*[\\d]{0,2}[\\s]*)))*(,([\\s]*[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+[\\s]*)\\-([\\s]*[\\d]{0,2}[\\s]*))$";

}
