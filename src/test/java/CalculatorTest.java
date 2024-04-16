import org.example.Calculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void plus() {
        Calculator calculator = new Calculator();
        int result = calculator.plus(1, 2);
        assertEquals(3, result);
    }
}
