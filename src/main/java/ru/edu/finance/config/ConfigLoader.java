package ru.edu.finance.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

public final class ConfigLoader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found");
            }
            PROPS.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки config.properties", e);
        }
    }

    private ConfigLoader() {}

    public static BigDecimal budgetWarningThreshold() {
        return new BigDecimal(
                PROPS.getProperty("budget.warning.threshold", "0.80")
        );
    }

    public static String storageDir() {
        return PROPS.getProperty("storage.data.dir", "data");
    }
}
