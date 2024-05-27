package christmas.global.exception.base;

public class ChristmasPlannerException extends IllegalArgumentException{
    private static final String ERROR_MESSAGE_HEADER = "[ERROR] ";

    public ChristmasPlannerException(ErrorBase error) {
        super(ERROR_MESSAGE_HEADER + error.getErrorMessage());
    }

}
