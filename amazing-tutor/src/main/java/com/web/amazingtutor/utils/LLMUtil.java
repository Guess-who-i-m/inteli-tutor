package com.web.amazingtutor.utils;

public class LLMUtil {

    /**
     * Helper method to clean the JSON string from potential markdown.
     * LLMs sometimes wrap JSON in ```json ... ```.
     */
    public static String cleanJsonString(String rawResponse) {
        if (rawResponse == null) return null;
        String cleaned = rawResponse.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7); // Remove ```json
            if (cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3); // Remove ```
            }
        } else if (cleaned.startsWith("```")) { // Sometimes just ```
            cleaned = cleaned.substring(3);
            if (cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3);
            }
        }
        return cleaned.trim();
    }
}
