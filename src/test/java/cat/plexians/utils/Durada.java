package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Durada {
    @JsonProperty("milisegons")
    private int milisegons;

    // Getter and Setter
    public int getMilisegons() {
        return milisegons;
    }

    public void setMilisegons(int milisegons) {
        this.milisegons = milisegons;
    }

    @Override
    public String toString() {
        return "Durada{" + "milisegons=" + milisegons + '}';
    }
}
