package htw_berlin.webtech.rest.service;

import htw_berlin.webtech.rest.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExerciseService {
    private final List<Exercise> exercises = new ArrayList<>();

    public ExerciseService() {
        exercises.add(new Exercise(
                UUID.randomUUID().toString(),
                "Bankdrücken",
                "Brust",
                3,
                10,
                60.0
        ));
        exercises.add(new Exercise(
                UUID.randomUUID().toString(),
                "Kniebeuge",
                "Beine",
                4,
                8,
                80.0
        ));
        exercises.add(new Exercise(
                UUID.randomUUID().toString(),
                "Klimmzüge",
                "Rücken",
                3,
                12,
                0.0
        ));
    }

    public List<Exercise> getAll() {
        return exercises;
    }

    public Exercise save(Exercise ex) {
        if (ex.getId() == null || ex.getId().isBlank()) {
            ex.setId(UUID.randomUUID().toString());
        }
        exercises.add(ex);
        return ex;
    }
}
