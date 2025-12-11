package htw_berlin.webtech.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutSetViewDto {
    private int satz;
    private double kg;
    private int reps;
}

