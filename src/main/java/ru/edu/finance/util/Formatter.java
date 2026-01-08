package ru.edu.finance.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Formatter {

    private Formatter() {}

    public static String money(BigDecimal value) {
        return value
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}
