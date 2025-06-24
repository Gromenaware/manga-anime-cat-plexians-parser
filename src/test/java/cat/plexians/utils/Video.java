package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Video {
    @JsonProperty("informacio")
    private Informacio informacio;
    @JsonProperty("media")
    private Media media;
    @JsonProperty("subtitols")
    private List<Subtitol> subtitols;

    // Getters and Setters
    public Informacio getInformacio() {
        return informacio;
    }

    public void setInformacio(Informacio informacio) {
        this.informacio = informacio;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<Subtitol> getSubtitols() {
        return subtitols;
    }

    public void setSubtitols(List<Subtitol> subtitols) {
        this.subtitols = subtitols;
    }

    @Override
    public String toString() {
        return "Video{" + "informacio=" + informacio + ", media=" + media + ", subtitols=" + subtitols + '}';
    }
}
