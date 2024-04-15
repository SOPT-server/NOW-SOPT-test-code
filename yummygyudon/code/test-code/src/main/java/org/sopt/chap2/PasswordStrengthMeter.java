package org.sopt.chap2;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s.length() < 8) {
            return PasswordStrength.NORMAL;
        }

        boolean containsNum = meetsContainingNumberCriteria(s);
        if (!containsNum) {
            return PasswordStrength.NORMAL;
        }
        return PasswordStrength.STRONG;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }
}
