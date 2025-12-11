package htw_berlin.webtech.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutExerciseViewDto {
    private Long exerciseId;
    private String exerciseName;
    private List<WorkoutSetViewDto> sets;
}

