package htw_berlin.webtech.rest.dto;

import htw_berlin.webtech.rest.model.Muskelgruppe;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ExerciseCreateDto {
    @NotBlank(message = "Name darf nicht leer sein")
    private String name;
    private Muskelgruppe muskelgruppe;
}
