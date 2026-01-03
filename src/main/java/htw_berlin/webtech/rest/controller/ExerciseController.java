package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.dto.ExerciseCreateDto;
import htw_berlin.webtech.rest.dto.ExerciseDto;
import htw_berlin.webtech.rest.service.ExerciseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<ExerciseDto> getAll() {
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{id}")
    public ExerciseDto getById(@PathVariable Long id) {
        return exerciseService.getExercise(id);
    }

    @PostMapping
    public ExerciseDto create(@RequestBody ExerciseCreateDto dto) {
        return exerciseService.createExercise(dto);
    }

    @PutMapping("/{id}")
    public ExerciseDto update(@PathVariable Long id, @RequestBody ExerciseCreateDto dto) {
        return exerciseService.updateExercise(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
    }
}

