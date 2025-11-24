package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.service.ExerciseService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "https://frontend-gymtrakcer.onrender.com")
@RestController
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping("/exercises")
    public List<Exercise> getAllExercises() {
        return service.getAll();
    }
}
