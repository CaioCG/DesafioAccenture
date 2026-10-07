package br.com.accenture.qa.config;

public final class DemoQaConfig {
    private static final String DEFAULT_BASE_URL = "https://demoqa.com";

    private DemoQaConfig() {
    }

    public static String uiBaseUrl() {
        String property = System.getProperty("demoqa.ui.base-url");
        if (property != null && !property.isBlank()) {
            return trimTrailingSlash(property);
        }
        String environment = System.getenv("DEMOQA_UI_BASE_URL");
        if (environment != null && !environment.isBlank()) {
            return trimTrailingSlash(environment);
        }
        return DEFAULT_BASE_URL;
    }

    private static String trimTrailingSlash(String value) {
        return value.replaceAll("/+$", "");
    }
}
