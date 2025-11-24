package htw_berlin.webtech.rest.service;

import htw_berlin.webtech.rest.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private final List<Exercise> exercises = new ArrayList<>();

    public List<Exercise> getAll() {
        return exercises;
    }

    public Exercise save(Exercise ex) {
        exercises.add(ex);
        return ex;
    }
}
