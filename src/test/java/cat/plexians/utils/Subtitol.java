package cat.plexians.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subtitol {
    @JsonProperty("format")
    private String format;
    @JsonProperty("iso")
    private String iso;
    @JsonProperty("url")
    private String url;

    // Getters and Setters
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Subtitol{" + "format='" + format + '\'' + ", iso='" + iso + '\'' + ", url='" + url + '\'' + '}';
    }
}
