package br.com.accenture.qa.testdata;

import java.util.UUID;

public final class AccountCredentialsGenerator {
    private AccountCredentialsGenerator() {
    }

    public static String username() {
        return "qa_" + uniqueId().toLowerCase();
    }

    public static String password() {
        return "QAa1!" + uniqueId();
    }

    private static String uniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
