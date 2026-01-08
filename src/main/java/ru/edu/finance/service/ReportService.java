package ru.edu.finance.service;

import ru.edu.finance.model.User;
import ru.edu.finance.report.ReportFormat;
import ru.edu.finance.report.StatisticsReport;
import ru.edu.finance.storage.FileStorage;

public class ReportService {

    private final StatisticsService statisticsService;
    private final FileStorage storage;

    public ReportService(StatisticsService statisticsService, FileStorage storage) {
        this.statisticsService = statisticsService;
        this.storage = storage;
    }

    public void exportStatistics(User user, ReportFormat format) {

        StatisticsReport report = new StatisticsReport(
                user.getLogin(),
                statisticsService.getTotalIncome(user),
                statisticsService.getTotalExpense(user),
                statisticsService.getIncomeByCategory(user),
                statisticsService.getExpensesByCategory(user)
        );

        switch (format) {
            case CSV -> storage.saveStatisticsCsv(report);
            case JSON -> storage.saveStatisticsJson(report);
        }
    }
}
