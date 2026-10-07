package br.com.accenture.qa.api;

public final class DemoQaConfig {
    private static final String DEFAULT_BASE_URL = "https://demoqa.com";

    private DemoQaConfig() {
    }

    public static String apiBaseUrl() {
        String property = System.getProperty("demoqa.api.base-url");
        if (property != null && !property.isBlank()) {
            return trimTrailingSlash(property);
        }
        String environment = System.getenv("DEMOQA_API_BASE_URL");
        if (environment != null && !environment.isBlank()) {
            return trimTrailingSlash(environment);
        }
        return DEFAULT_BASE_URL;
    }

    private static String trimTrailingSlash(String value) {
        return value.replaceAll("/+$", "");
    }
}
