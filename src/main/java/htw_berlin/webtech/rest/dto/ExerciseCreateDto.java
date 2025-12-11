package htw_berlin.webtech.rest.dto;

import htw_berlin.webtech.rest.model.Muskelgruppe;
import lombok.Data;

@Data
public class ExerciseCreateDto {
    private String name;
    private Muskelgruppe muskelgruppe;
}

