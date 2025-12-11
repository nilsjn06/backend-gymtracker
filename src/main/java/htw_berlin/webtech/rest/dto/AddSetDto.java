package htw_berlin.webtech.rest.dto;

import lombok.Data;

@Data
public class AddSetDto {

    private Long exerciseId;  // welche Übung
    private double weight;    // kg
    private int reps;         // Wiederholungen
}

