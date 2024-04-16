package org.example;

public class PasswordStrengthMeter {
    public PasswordStrength meter(String s) {
        if (s == null || s.isEmpty()) return  PasswordStrength.INVALID;

        int meterCounts = getMeterCriteriaCounts(s);

        if (meterCounts <= 1) {
            return PasswordStrength.WEAK;
        }

        if (meterCounts == 2) {
            return PasswordStrength.NORMAL;
        }

        return PasswordStrength.STRONG;
    }

    private int getMeterCriteriaCounts(String s) {
        int meterCounts = 0;
        if (s.length() >= 8) {
            meterCounts++;
        }

        if (meetsContainingNumberCriteria(s)) {
            meterCounts++;
        }

        if (meetsContainingUppercaseCriteria(s)) {
            meterCounts++;
        }
        return meterCounts;
    }

    private boolean meetsContainingNumberCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                return true;
            }
        }
        return false;
    }

    private boolean meetsContainingUppercaseCriteria(String s) {
        for (char ch : s.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }
}
