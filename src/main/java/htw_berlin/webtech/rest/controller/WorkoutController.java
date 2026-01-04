package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.dto.WorkoutViewDto;
import htw_berlin.webtech.rest.dto.CreateWorkoutDto;
import htw_berlin.webtech.rest.dto.AddSetDto;
import htw_berlin.webtech.rest.service.WorkoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    // "Workout beginnen"
    @PostMapping
    public WorkoutViewDto createWorkout(@RequestBody CreateWorkoutDto dto) {
        return workoutService.createWorkout(dto);
    }

    // Verlauf – alle Workouts
    @GetMapping
    public List<WorkoutViewDto> getAllWorkouts() {
        return workoutService.getAllWorkouts();
    }

    // einzelnes Workout anzeigen (für Detailansicht)
    @GetMapping("/{id}")
    public WorkoutViewDto getWorkout(@PathVariable Long id) {
        return workoutService.getWorkout(id);
    }

    // Satz hinzufügen
    @PostMapping("/{id}/sets")
    public WorkoutViewDto addSet(@PathVariable Long id, @RequestBody AddSetDto dto) {
        return workoutService.addSet(id, dto);
    }

    // DELETE
    @DeleteMapping
    public void deleteAllWorkouts() {
        workoutService.deleteAllWorkouts();
    }

    @DeleteMapping("/{id}")
    public void deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
    }
}
