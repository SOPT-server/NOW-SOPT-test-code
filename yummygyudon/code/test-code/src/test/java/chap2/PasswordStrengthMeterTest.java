package chap2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <strong><검사할 규칙></strong>
 *
 * <li>길이 8글자 이상</li>
 * <li>0 ~ 9 사이의 숫자 포함</li>
 * <li>대문자 포함</li>
 *
 * <br/>
 *
 * <strong><판정></strong>
 *
 * <li>모두 충족 : "강함"</li>
 * <li>2개 충족 : "보통"</li>
 * <li>1개 이하 충족 : "약함"</li>
 */
public class PasswordStrengthMeterTest {

    @Test
    void meetsAllCriteria_Then_Strong() {
        PasswordStrengthMeter meter = new PasswordStrengthMeter();
        PasswordStrength result = meter.meter("ab12!@AB");
        Assertions.assertEquals(PasswordStrength.STRON, result);
    }
}
