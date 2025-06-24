package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Media {
    @JsonProperty("url")
    private List<Url> url;

    // Getter and Setter
    public List<Url> getUrl() {
        return url;
    }

    public void setUrl(List<Url> url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Media{" + "url=" + url + '}';
    }
}
