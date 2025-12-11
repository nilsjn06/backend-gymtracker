package htw_berlin.webtech.rest.repository;

import htw_berlin.webtech.rest.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    // für Verlauf nach Datum
    List<Workout> findAllByOrderByDateDescIdDesc();

    List<Workout> findByDateOrderByIdAsc(LocalDate date);
}

