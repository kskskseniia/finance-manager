package ru.edu.finance.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

public final class ConfigLoader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream is = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }

            PROPERTIES.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigLoader() {
    }

    public static BigDecimal getBudgetWarningThreshold() {
        return new BigDecimal(
                PROPERTIES.getProperty(
                        "budget.warning.threshold",
                        "0.80"
                )
        );
    }
}
