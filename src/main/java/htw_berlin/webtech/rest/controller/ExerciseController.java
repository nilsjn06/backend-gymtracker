package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://frontend-gymtracker.onrender.com")
@RestController
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService ExerciseService) {
        this.service = ExerciseService;
    }

    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return service.getAll();
    }

    @PostMapping("/exercises")
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return service.save(exercise);
    }
}
