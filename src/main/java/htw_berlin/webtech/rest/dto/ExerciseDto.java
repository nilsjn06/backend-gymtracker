package htw_berlin.webtech.rest.dto;

import htw_berlin.webtech.rest.model.Muskelgruppe;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDto {
    private Long id;
    private String name;
    private Muskelgruppe muskelgruppe;
}

