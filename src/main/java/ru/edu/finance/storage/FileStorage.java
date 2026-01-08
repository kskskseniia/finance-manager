package ru.edu.finance.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ru.edu.finance.config.ConfigLoader;
import ru.edu.finance.model.User;
import ru.edu.finance.report.StatisticsReport;

import java.io.File;
import java.io.IOException;

public class FileStorage {

    private final ObjectMapper mapper;
    private final File dataDir;

    public FileStorage() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);

        this.dataDir = new File(ConfigLoader.storageDir());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public void saveUser(User user) {
        File file = getUserFile(user.getLogin());
        try {
            mapper.writeValue(file, user);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Ошибка сохранения данных пользователя", e);
        }
    }

    public User loadUser(String login) {
        File file = getUserFile(login);
        if (!file.exists()) {
            return null;
        }

        try {
            return mapper.readValue(file, User.class);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Ошибка загрузки данных пользователя", e);
        }
    }

    public boolean userExists(String login) {
        return getUserFile(login).exists();
    }

    private File getUserFile(String login) {
        return new File(dataDir, "user_" + login + ".json");
    }

    public void saveStatisticsReport(StatisticsReport report) {
        try {
            File dir = new File(dataDir, "reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filename = "stats_" + report.getLogin() + ".json";
            File file = new File(dir, filename);

            mapper.writeValue(file, report);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения отчёта", e);
        }
    }
}
