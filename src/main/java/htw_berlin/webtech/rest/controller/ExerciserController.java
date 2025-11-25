package htw_berlin.webtech.rest.controller;

import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.model.Muskelgruppe;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@CrossOrigin(origins = "*") // fürs erste: alle Origins erlauben
@RequestMapping("/api")     // gemeinsame Basis-URL für alle Exercise-Endpunkte
public class ExerciserController {

    // einzelnes Beispiel – z.B. für einen Detail-View
    @GetMapping(path = "/exercise")
    public Exercise getExercise() {
        return new Exercise("Bankdrücken", Muskelgruppe.BRUST, 4, 10, 80.0);
    }

    // Liste von Übungen – ideal für das Frontend (Tabelle/List)
    @GetMapping(path = "/exercises")
    public List<Exercise> getExercises() {
        return List.of(
                new Exercise("Bankdrücken", Muskelgruppe.BRUST, 4, 10, 80.0),
                new Exercise("Kreuzheben", Muskelgruppe.RUECKEN, 3, 5, 100.0),
                new Exercise("Kniebeugen", Muskelgruppe.BEINE, 5, 5, 90.0),
                new Exercise("Schulterdrücken", Muskelgruppe.SCHULTERN, 4, 8, 40.0),
                new Exercise("Bizepscurls", Muskelgruppe.BIZEPS, 3, 12, 15.0)
        );
    }
}
