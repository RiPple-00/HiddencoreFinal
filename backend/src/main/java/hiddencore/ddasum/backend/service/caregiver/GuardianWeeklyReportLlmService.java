package hiddencore.ddasum.backend.service.caregiver;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GuardianWeeklyReportLlmService {

    private static final Logger log = LoggerFactory.getLogger(GuardianWeeklyReportLlmService.class);

    private static final String SYSTEM_PROMPT = """
            너는 보호자에게 보여줄 주간 돌봄 요약을 작성하는 도우미다.
            출력은 반드시 JSON 하나만 반환한다.
            JSON 외 설명, 코드블록, 마크다운은 금지한다.
            진단/처방을 단정하지 않고 관찰 중심으로 작성한다.
            """;

    private final ObjectMapper objectMapper;

    @Value("${app.llm.weekly-report.enabled:false}")
    private boolean enabled;

    @Value("${app.llm.weekly-report.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    @Value("${app.llm.weekly-report.api-key:}")
    private String apiKey;

    @Value("${app.llm.weekly-report.model:gpt-4o-mini}")
    private String model;

    @Value("${app.llm.weekly-report.temperature:0.2}")
    private double temperature;

    @Value("${app.llm.weekly-report.max-tokens:500}")
    private int maxTokens;

    public GuardianWeeklyReportLlmService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GeneratedNarrative generateWeeklyNarrative(WeeklyNarrativeInput input) {
        if (!enabled) {
            return null;
        }
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("LLM weekly report is enabled but API key is empty. Using fallback narrative.");
            return null;
        }

        try {
            RestClient restClient = RestClient.builder().build();
            String endpoint = baseUrl.endsWith("/") ? baseUrl + "chat/completions" : baseUrl + "/chat/completions";

            JsonNode payload = objectMapper.createObjectNode()
                    .put("model", model)
                    .put("temperature", temperature)
                    .put("max_tokens", maxTokens);

            JsonNode messages = ((com.fasterxml.jackson.databind.node.ObjectNode) payload).putArray("messages");
            ((com.fasterxml.jackson.databind.node.ArrayNode) messages).addObject()
                    .put("role", "system")
                    .put("content", SYSTEM_PROMPT);
            ((com.fasterxml.jackson.databind.node.ArrayNode) messages).addObject()
                    .put("role", "user")
                    .put("content", buildUserPrompt(input));

            JsonNode response = restClient.post()
                    .uri(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(payload)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null) {
                return null;
            }

            String raw = response.path("choices").path(0).path("message").path("content").asText("").trim();
            if (raw.isBlank()) {
                return null;
            }

            String jsonOnly = stripCodeFence(raw);
            JsonNode root = objectMapper.readTree(jsonOnly);

            String summaryText = normalize(root.path("summaryText").asText(""), 220);
            String checklistInsight = normalize(root.path("checklistInsight").asText(""), 180);
            List<String> aiComments = parseStringArray(root.path("aiComments"), 4, 140);
            List<String> nextWeekTips = parseStringArray(root.path("nextWeekTips"), 3, 160);
            ProgramSectionData programSection = parseProgramSection(root.path("programSection"));

            if (summaryText.isBlank() && checklistInsight.isBlank() && aiComments.isEmpty() && nextWeekTips.isEmpty() && programSection == null) {
                return null;
            }

            return new GeneratedNarrative(summaryText, checklistInsight, aiComments, nextWeekTips, programSection);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse LLM weekly report response. Using fallback. reason={}", e.getMessage());
            return null;
        } catch (RestClientException e) {
            log.warn("Failed to call LLM weekly report endpoint. Using fallback. reason={}", e.getMessage());
            return null;
        }
    }

    private String buildUserPrompt(WeeklyNarrativeInput input) {
        return """
                아래 주간 정보를 바탕으로 보호자용 요약을 생성해라.
                반드시 다음 JSON 스키마만 반환:
                {
                  "summaryText": "문자열",
                                    "checklistInsight": "문자열",
                  "aiComments": ["문자열"],
                                    "nextWeekTips": ["문자열"],
                                    "programSection": {
                                        "activityTitle": "문자열",
                                        "activityDescription": "문자열",
                                        "effects": ["문자열"],
                                        "recommendations": ["문자열"]
                                    }
                }

                제약:
                - summaryText: 1~2문장
                                - checklistInsight: 1문장
                - aiComments: 1~4개
                - nextWeekTips: 1~3개
                                - programSection.effects: 1~2개
                                - programSection.recommendations: 2~3개
                - 공포/단정 표현 금지

                입력:
                period: %s ~ %s
                overallRate: %d
                riskLevel: %s
                riskFlags: %s
                mealMissingCount: 아침 %d, 점심 %d, 저녁 %d
                checklistRates: 식사 %d, 위생 %d, 상태 %d, 배변 %d
                환자 상태에 맞는 프로그램 활동과 증진 효과를 programSection에 작성해라.
                """.formatted(
                input.periodStart,
                input.periodEnd,
                input.overallRate,
                input.riskLevel,
                String.join(",", input.riskFlags),
                input.mealMorningMissingCount,
                input.mealLunchMissingCount,
                input.mealDinnerMissingCount,
                input.mealPercent,
                input.hygienePercent,
                input.conditionPercent,
                input.eliminationPercent
        );
    }

    private List<String> parseStringArray(JsonNode arrayNode, int maxItems, int maxLen) {
        List<String> values = new ArrayList<>();
        if (!arrayNode.isArray()) {
            return values;
        }
        for (JsonNode n : arrayNode) {
            String v = normalize(n.asText(""), maxLen);
            if (!v.isBlank()) {
                values.add(v);
            }
            if (values.size() >= maxItems) {
                break;
            }
        }
        return values;
    }

    private ProgramSectionData parseProgramSection(JsonNode node) {
        if (node == null || !node.isObject()) {
            return null;
        }
        String activityTitle = normalize(node.path("activityTitle").asText(""), 60);
        String activityDescription = normalize(node.path("activityDescription").asText(""), 220);
        List<String> effects = parseStringArray(node.path("effects"), 2, 120);
        List<String> recommendations = parseStringArray(node.path("recommendations"), 3, 160);

        if (activityTitle.isBlank() && activityDescription.isBlank() && effects.isEmpty() && recommendations.isEmpty()) {
            return null;
        }
        return new ProgramSectionData(activityTitle, activityDescription, effects, recommendations);
    }

    private String normalize(String text, int maxLen) {
        if (text == null) return "";
        String normalized = text.replace("\n", " ").replace("\r", " ").trim();
        while (normalized.contains("  ")) {
            normalized = normalized.replace("  ", " ");
        }
        if (normalized.length() > maxLen) {
            return normalized.substring(0, maxLen).trim();
        }
        return normalized;
    }

    private String stripCodeFence(String raw) {
        String s = raw.trim();
        if (!s.startsWith("```")) {
            return s;
        }
        s = s.replaceFirst("^```[a-zA-Z]*\\n", "");
        s = s.replaceFirst("\\n```$", "");
        return s.trim();
    }

    public static class WeeklyNarrativeInput {
        public final String periodStart;
        public final String periodEnd;
        public final int overallRate;
        public final String riskLevel;
        public final List<String> riskFlags;
        public final int mealMorningMissingCount;
        public final int mealLunchMissingCount;
        public final int mealDinnerMissingCount;
        public final int mealPercent;
        public final int hygienePercent;
        public final int conditionPercent;
        public final int eliminationPercent;

        public WeeklyNarrativeInput(
                String periodStart,
                String periodEnd,
                int overallRate,
                String riskLevel,
                List<String> riskFlags,
                int mealMorningMissingCount,
                int mealLunchMissingCount,
                int mealDinnerMissingCount,
                int mealPercent,
                int hygienePercent,
                int conditionPercent,
                int eliminationPercent
        ) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.overallRate = overallRate;
            this.riskLevel = riskLevel;
            this.riskFlags = riskFlags;
            this.mealMorningMissingCount = mealMorningMissingCount;
            this.mealLunchMissingCount = mealLunchMissingCount;
            this.mealDinnerMissingCount = mealDinnerMissingCount;
            this.mealPercent = mealPercent;
            this.hygienePercent = hygienePercent;
            this.conditionPercent = conditionPercent;
            this.eliminationPercent = eliminationPercent;
        }
    }

    public record ProgramSectionData(String activityTitle, String activityDescription, List<String> effects, List<String> recommendations) {
    }

    public record GeneratedNarrative(String summaryText, String checklistInsight, List<String> aiComments, List<String> nextWeekTips, ProgramSectionData programSection) {
    }
}
