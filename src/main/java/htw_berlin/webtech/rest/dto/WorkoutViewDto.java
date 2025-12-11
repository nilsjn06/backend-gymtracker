package htw_berlin.webtech.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutViewDto {
    private Long id;
    private String date;
    private String title;
    private List<WorkoutExerciseViewDto> exercises;
}

