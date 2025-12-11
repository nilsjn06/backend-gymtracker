package htw_berlin.webtech.rest.dto;

import lombok.Data;

@Data
public class CreateWorkoutDto {

    // Datum als String "yyyy-MM-dd"
    private String date;

    // optionaler Titel, z.B. "Push"
    private String title;
}

