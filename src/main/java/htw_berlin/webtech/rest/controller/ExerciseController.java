package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.model.Exercise;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


    @Controller
    public class ExerciseController {

        @GetMapping(path = "/exercise")
        public ResponseEntity<Exercise> getExercise() {
            final Exercise exercise = new Exercise("Bankdrücken", "Brust", 2,10, 80.0);
            return ResponseEntity.ok(exercise);
        }
    }


