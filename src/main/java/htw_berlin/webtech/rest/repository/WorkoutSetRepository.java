package htw_berlin.webtech.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<htw_berlin.webtech.rest.model.WorkoutSet, Long> {

    List<htw_berlin.webtech.rest.model.WorkoutSet> findByWorkout(htw_berlin.webtech.rest.model.Workout workout);

    int countByWorkoutAndExercise(htw_berlin.webtech.rest.model.Workout workout, htw_berlin.webtech.rest.model.Exercise exercise);
}
