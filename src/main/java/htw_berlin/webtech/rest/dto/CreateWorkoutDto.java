package htw_berlin.webtech.rest.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateWorkoutDto {

    // Datum als String "yyyy-MM-dd"
    private String date;

    // Titel ist jetzt verpflichtend
    @NotBlank(message = "Titel darf nicht leer sein")
    private String title;
}
