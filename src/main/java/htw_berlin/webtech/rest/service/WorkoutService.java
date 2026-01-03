package htw_berlin.webtech.rest.service;

import htw_berlin.webtech.rest.dto.*;
import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.model.Workout;
import htw_berlin.webtech.rest.model.WorkoutSet;
import htw_berlin.webtech.rest.repository.ExerciseRepository;
import htw_berlin.webtech.rest.repository.WorkoutRepository;
import htw_berlin.webtech.rest.repository.WorkoutSetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutSetRepository workoutSetRepository;

    public WorkoutService(ExerciseRepository exerciseRepository,
                          WorkoutRepository workoutRepository,
                          WorkoutSetRepository workoutSetRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutSetRepository = workoutSetRepository;
    }

    // ---------- Workouts ----------
    public WorkoutViewDto createWorkout(CreateWorkoutDto dto) {
        Workout workout = Workout.builder()
                .date(LocalDate.parse(dto.getDate()))
                .title(dto.getTitle())
                .build();
        workout = workoutRepository.save(workout);
        return toWorkoutViewDto(workout, Collections.emptyList());
    }

    public WorkoutViewDto getWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        List<WorkoutSet> sets = workoutSetRepository.findByWorkout(workout);
        return toWorkoutViewDto(workout, sets);
    }

    public List<WorkoutViewDto> getAllWorkouts() {
        List<Workout> workouts = workoutRepository.findAllByOrderByDateDescIdDesc();
        return workouts.stream()
                .map(w -> {
                    List<WorkoutSet> sets = workoutSetRepository.findByWorkout(w);
                    return toWorkoutViewDto(w, sets);
                })
                .collect(Collectors.toList());
    }

    // ---------- Sätze hinzufügen ----------
    public WorkoutViewDto addSet(Long workoutId, AddSetDto dto) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        int nextSetNumber = workoutSetRepository.countByWorkoutAndExercise(workout, exercise) + 1;

        WorkoutSet set = WorkoutSet.builder()
                .workout(workout)
                .exercise(exercise)
                .setNumber(nextSetNumber)
                .weight(dto.getWeight())
                .reps(dto.getReps())
                .build();

        workoutSetRepository.save(set);

        List<WorkoutSet> sets = workoutSetRepository.findByWorkout(workout);
        return toWorkoutViewDto(workout, sets);
    }

    // ---------- Mapping in deine gewünschte Struktur ----------
    private WorkoutViewDto toWorkoutViewDto(Workout workout, List<WorkoutSet> sets) {

        // nach Übung gruppieren
        Map<Exercise, List<WorkoutSet>> byExercise = sets.stream()
                .collect(Collectors.groupingBy(WorkoutSet::getExercise));

        // für stabile Darstellung sortieren wir erst nach Übungsname, dann nach Satznummer
        List<WorkoutExerciseViewDto> exerciseDtos = byExercise.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getName()))
                .map(entry -> {
                    Exercise exercise = entry.getKey();

                    List<WorkoutSetViewDto> setDtos = entry.getValue().stream()
                            .sorted(Comparator.comparingInt(WorkoutSet::getSetNumber))
                            .map(ws -> WorkoutSetViewDto.builder()
                                    .satz(ws.getSetNumber())
                                    .kg(ws.getWeight())
                                    .reps(ws.getReps())
                                    .build())
                            .collect(Collectors.toList());

                    return WorkoutExerciseViewDto.builder()
                            .exerciseId(exercise.getId())
                            .exerciseName(exercise.getName())
                            .sets(setDtos)
                            .build();
                })
                .collect(Collectors.toList());

        return WorkoutViewDto.builder()
                .id(workout.getId())
                .date(workout.getDate().toString())
                .title(workout.getTitle())
                .exercises(exerciseDtos)
                .build();
    }
}
