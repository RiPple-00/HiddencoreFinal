package hiddencore.ddasum.backend.web.dto.care;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianWeeklyCareReportResponse {

    private Long patientId;
    private String patientName;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    /** 전체 수행률(%) */
    private Integer overallRate;

    /** 안정 | 주의 | 위험 */
    private String riskLevel;

    private String summaryText;
    private List<String> riskFlags;
    private List<String> aiComments;
    private MealMissingCount mealMissingCount;
    private ProgramSection programSection;

    private List<DailyRate> dailyRates;
    private List<ChecklistRow> checklistRows;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRate {
        private LocalDate date;
        private String day;
        private Integer rate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChecklistRow {
        private String label;
        private Integer percent;
        /** success | warn | danger */
        private String percentStyle;
        private List<String> dailyComments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealMissingCount {
        private Integer morning;
        private Integer lunch;
        private Integer dinner;
        private Integer total;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgramSection {
        private String activityTitle;
        private String activityDescription;
        private List<String> effects;
        private List<String> recommendations;
    }
}
