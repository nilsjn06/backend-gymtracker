package htw_berlin.webtech.rest.repository;

import htw_berlin.webtech.rest.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}

