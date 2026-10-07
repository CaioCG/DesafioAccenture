package br.com.accenture.qa.testdata.model;

import java.time.LocalDate;
import java.util.List;

public record PracticeFormData(
        String firstName,
        String lastName,
        String email,
        String gender,
        String mobile,
        LocalDate dateOfBirth,
        String subject,
        List<String> hobbies,
        String pictureName,
        String address,
        String state,
        String city) {

    public PracticeFormData {
        hobbies = List.copyOf(hobbies);
    }
}
