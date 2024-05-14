package chap2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sopt.chap2.PasswordStrength;
import org.sopt.chap2.PasswordStrengthMeter;

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
    private final PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr) {
        PasswordStrength result = meter.meter(password);
        Assertions.assertEquals(expStr, result);
    }
    @Test
    void meetsAllCriteria_Then_Strong() {
        assertStrength("ab12!@AB", PasswordStrength.STRONG);
        assertStrength("ab12!@Add", PasswordStrength.STRONG);
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal() {
        assertStrength("ab12!@A", PasswordStrength.NORMAL);
        assertStrength("Ab12!c", PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_Number_Then_Normal() {
        assertStrength("ab!!@ABqwer", PasswordStrength.NORMAL);
    }
}
