package htw_berlin.webtech.rest.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Zu welchem Workout gehört der Satz?
    @ManyToOne(optional = false)
    private Workout workout;

    // Welche Übung ist das (Bankdrücken, Hammercurls, ...)?
    @ManyToOne(optional = false)
    private Exercise exercise;

    private int setNumber;   // Satznummer: 1, 2, 3 ...

    private double weight;   // kg

    private int reps;        // Wiederholungen
}
