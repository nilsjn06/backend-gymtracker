package htw_berlin.webtech;

public class Exercise {

    String name;
    String Muskelgruppe;
    int Wiederholungen;
    double Gewicht;

    Exercise(){
    }

    public Exercise(String name, String muskelgruppe, int Wiederholungen, double Gewicht) {
        this.name = name;
        this.Muskelgruppe = muskelgruppe;
        this.Wiederholungen = Wiederholungen;
        this.Gewicht = Gewicht;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuskelgruppe() {
        return Muskelgruppe;
    }

    public void setMuskelgruppe(String muskelgruppe) {
        Muskelgruppe = muskelgruppe;
    }

    public int getWiederholungen() {
        return Wiederholungen;
    }

    public void setWiederholungen(int wiederholungen) {
        Wiederholungen = wiederholungen;
    }

    public double getGewicht() {
        return Gewicht;
    }

    public void setGewicht(double gewicht) {
        Gewicht = gewicht;
    }
}
