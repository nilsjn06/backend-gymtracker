package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.dto.ExerciseDto;
import htw_berlin.webtech.rest.dto.ExerciseCreateDto;
import htw_berlin.webtech.rest.dto.WorkoutViewDto;
import htw_berlin.webtech.rest.dto.CreateWorkoutDto;
import htw_berlin.webtech.rest.dto.AddSetDto;
import htw_berlin.webtech.rest.service.WorkoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // ---------- Übungen (für Liste & "Neu") ----------
    @GetMapping("/exercises")
    public List<ExerciseDto> getExercises() {
        return workoutService.getAllExercises();
    }

    @PostMapping("/exercises")
    public ExerciseDto createExercise(@RequestBody ExerciseCreateDto dto) {
        return workoutService.createExercise(dto);
    }

    // ---------- Workouts ----------
    // "Workout beginnen"
    @PostMapping("/workouts")
    public WorkoutViewDto createWorkout(@RequestBody CreateWorkoutDto dto) {
        return workoutService.createWorkout(dto);
    }

    // Verlauf – alle Workouts
    @GetMapping("/workouts")
    public List<WorkoutViewDto> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    // einzelnes Workout anzeigen (für Detailansicht)
    @GetMapping("/workouts/{id}")
    public WorkoutViewDto getWorkout(@PathVariable Long id) {
        return workoutService.getWorkout(id);
    }

    // Satz hinzufügen (immer wieder aufrufen bei + im Frontend)
    @PostMapping("/workouts/{id}/sets")
    public WorkoutViewDto addSet(@PathVariable Long id, @RequestBody AddSetDto dto) {
        return workoutService.addSet(id, dto);
    }
}
