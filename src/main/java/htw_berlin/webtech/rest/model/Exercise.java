package htw_berlin.webtech.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    private String id;              // ← hinzugefügt
    private String name;
    private String muskelgruppe;
    private int satz;
    private int wiederholungen;
    private double gewicht;
}
