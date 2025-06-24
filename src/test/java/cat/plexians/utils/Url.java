package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Url {
    @JsonProperty("file")
    private String file;
    @JsonProperty("label")
    private String label;

    // Getters and Setters
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Url{" + "file='" + file + '\'' + ", label='" + label + '\'' + '}';
    }
}
