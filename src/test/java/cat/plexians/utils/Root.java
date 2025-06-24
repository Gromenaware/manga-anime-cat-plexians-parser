package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Root {
    @JsonProperty("data_emissio")
    private String dataEmissio;
    @JsonProperty("data_modificacio")
    private String dataModificacio;
    @JsonProperty("data_publicacio")
    private String dataPublicacio;
    @JsonProperty("entradeta")
    private String entradeta;
    @JsonProperty("nombonic")
    private String nombonic;
    @JsonProperty("programa_id")
    private int programaId;
    @JsonProperty("titol")
    private String titol;
    @JsonProperty("videos")
    private List<Video> videos;

    // Getters and Setters
    public String getDataEmissio() {
        return dataEmissio;
    }

    public void setDataEmissio(String dataEmissio) {
        this.dataEmissio = dataEmissio;
    }

    public String getDataModificacio() {
        return dataModificacio;
    }

    public void setDataModificacio(String dataModificacio) {
        this.dataModificacio = dataModificacio;
    }

    public String getDataPublicacio() {
        return dataPublicacio;
    }

    public void setDataPublicacio(String dataPublicacio) {
        this.dataPublicacio = dataPublicacio;
    }

    public String getEntradeta() {
        return entradeta;
    }

    public void setEntradeta(String entradeta) {
        this.entradeta = entradeta;
    }

    public String getNombonic() {
        return nombonic;
    }

    public void setNombonic(String nombonic) {
        this.nombonic = nombonic;
    }

    public int getProgramaId() {
        return programaId;
    }

    public void setProgramaId(int programaId) {
        this.programaId = programaId;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Root{" + "dataEmissio='" + dataEmissio + '\'' + ", dataModificacio='" + dataModificacio + '\'' + ", dataPublicacio='" + dataPublicacio + '\'' + ", entradeta='" + entradeta + '\'' + ", nombonic='" + nombonic + '\'' + ", programaId=" + programaId + ", titol='" + titol + '\'' + ", videos=" + videos + '}';
    }
}

