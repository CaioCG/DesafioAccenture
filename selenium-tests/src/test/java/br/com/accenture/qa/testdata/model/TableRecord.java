package br.com.accenture.qa.testdata.model;

import java.util.Map;
import java.util.Set;

public record TableRecord(
        String firstName,
        String lastName,
        String age,
        String email,
        String salary,
        String department) {

    private static final Set<String> FIELDS = Set.of(
            "firstName", "lastName", "age", "email", "salary", "department");

    public TableRecord withChanges(Map<String, String> changes) {
        Set<String> unsupportedFields = changes.keySet().stream()
                .filter(field -> !FIELDS.contains(field))
                .collect(java.util.stream.Collectors.toSet());
        if (!unsupportedFields.isEmpty()) {
            throw new IllegalArgumentException("Unsupported table fields: " + unsupportedFields);
        }

        return new TableRecord(
                changes.getOrDefault("firstName", firstName),
                changes.getOrDefault("lastName", lastName),
                changes.getOrDefault("age", age),
                changes.getOrDefault("email", email),
                changes.getOrDefault("salary", salary),
                changes.getOrDefault("department", department));
    }
}
