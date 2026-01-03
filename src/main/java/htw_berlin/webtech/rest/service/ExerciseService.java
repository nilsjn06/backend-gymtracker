package htw_berlin.webtech.rest.service;

import htw_berlin.webtech.rest.dto.ExerciseCreateDto;
import htw_berlin.webtech.rest.dto.ExerciseDto;
import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.repository.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<ExerciseDto> getAllExercises() {
        return exerciseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ExerciseDto getExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
        return toDto(exercise);
    }

    public ExerciseDto createExercise(ExerciseCreateDto dto) {
        Exercise exercise = Exercise.builder()
                .name(dto.getName())
                .muskelgruppe(dto.getMuskelgruppe())
                .build();
        exercise = exerciseRepository.save(exercise);
        return toDto(exercise);
    }

    public ExerciseDto updateExercise(Long id, ExerciseCreateDto dto) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
        exercise.setName(dto.getName());
        exercise.setMuskelgruppe(dto.getMuskelgruppe());
        exercise = exerciseRepository.save(exercise);
        return toDto(exercise);
    }

    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));
        exerciseRepository.delete(exercise);
    }

    private ExerciseDto toDto(Exercise exercise) {
        return ExerciseDto.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .muskelgruppe(exercise.getMuskelgruppe())
                .build();
    }
}

