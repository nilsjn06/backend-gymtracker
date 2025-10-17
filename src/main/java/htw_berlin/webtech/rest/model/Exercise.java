package htw_berlin.webtech.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Exercise {

    private String name;
    private String muskelgruppe;
    private int wiederholungen;
    private double gewicht;


    public Exercise() {}
}
