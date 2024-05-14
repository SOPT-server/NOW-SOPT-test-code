package chap2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sopt.chap2.Calculator;

public class CalculatorTest {

    @Test
    void plus() {
        // `Calculator`라는 클래스 구현 없이 Test 코드 선 작성
        int result = Calculator.plus(1, 2);
        Assertions.assertEquals(3, result);
    }
}
