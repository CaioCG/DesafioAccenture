package br.com.accenture.qa.testdata;

import br.com.accenture.qa.testdata.model.PracticeFormData;
import br.com.accenture.qa.testdata.model.TableRecord;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class TestDataFactory {
    private static final List<String> FIRST_NAMES = List.of("Alex", "Bianca", "Caio", "Diana", "Enzo", "Fernanda", "Gustavo", "Helena");
    private static final List<String> LAST_NAMES = List.of("Almeida", "Barros", "Costa", "Dias", "Esteves", "Freitas", "Gomes", "Lima");
    private static final List<String> SUBJECTS = List.of("Maths", "Physics", "Chemistry", "Biology", "English", "History");
    private static final List<String> HOBBIES = List.of("Sports", "Reading", "Music");
    private static final List<String> DEPARTMENTS = List.of("QA", "Engineering", "Compliance", "Finance", "Operations");
    private static final List<String> CITIES = List.of("Delhi", "Gurgaon", "Noida");

    private TestDataFactory() {
    }

    public static PracticeFormData practiceForm() {
        String id = uniqueId().toLowerCase();
        String city = choose(CITIES);
        List<String> hobbies = HOBBIES.stream().filter(ignored -> randomBoolean()).limit(2).toList();
        if (hobbies.isEmpty()) {
            hobbies = List.of("Reading");
        }
        return new PracticeFormData(
                choose(FIRST_NAMES) + id.substring(0, 2),
                choose(LAST_NAMES) + id.substring(2, 4),
                "student." + id + "@example.com",
                choose(List.of("Male", "Female", "Other")),
                String.format("%010d", ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L)),
                LocalDate.of(
                        ThreadLocalRandom.current().nextInt(1985, 2005),
                        ThreadLocalRandom.current().nextInt(1, 13),
                        1).plusDays(ThreadLocalRandom.current().nextInt(0, 27)),
                choose(SUBJECTS),
                hobbies,
                "upload.txt",
                "QA Street " + id.substring(0, 6) + ", " + city,
                "NCR",
                city);
    }

    public static Path uploadPicturePath() {
        try {
            var resource = TestDataFactory.class.getResource("/fixtures/upload.txt");
            if (resource == null) {
                throw new IllegalStateException("Could not locate fixtures/upload.txt");
            }
            return Path.of(resource.toURI()).toRealPath();
        } catch (URISyntaxException | IOException exception) {
            throw new IllegalStateException("Could not access fixtures/upload.txt", exception);
        }
    }

    public static TableRecord tableRecord() {
        String id = uniqueId().toLowerCase();
        return new TableRecord(
                choose(FIRST_NAMES) + id.substring(0, 2),
                choose(LAST_NAMES) + id.substring(2, 4),
                String.valueOf(ThreadLocalRandom.current().nextInt(21, 66)),
                "qa." + id + "@example.com",
                String.valueOf(ThreadLocalRandom.current().nextInt(2_000, 25_001)),
                choose(DEPARTMENTS));
    }

    public static TableRecord tableRecord(Map<String, String> providedValues) {
        return tableRecord().withChanges(providedValues);
    }

    public static List<TableRecord> tableRecords(int count) {
        return java.util.stream.IntStream.range(0, count).mapToObj(ignored -> tableRecord()).toList();
    }

    private static String uniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    private static <T> T choose(List<T> values) {
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}
