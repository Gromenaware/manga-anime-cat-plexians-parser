package cat.plexians.utils;

public class VideoInfo {
    private String videoTitle;
    private String idVideo;
    private String urlDinamics;
    private String urlDescarrega;

    public VideoInfo(String videoTitle, String idVideo, String urlDinamics, String urlDescarrega) {
        this.videoTitle = videoTitle;
        this.idVideo = idVideo;
        this.urlDinamics = urlDinamics;
        this.urlDescarrega = urlDescarrega;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public String getUrlDinamics() {
        return urlDinamics;
    }

    public String getUrlDescarrega() {
        return urlDescarrega;
    }

    @Override
    public String toString() {
        return "Títol: " + videoTitle + "\nId del video: " + idVideo + "\nDinamics url: " + urlDinamics + "\nURL descarrega High: " + urlDescarrega;
    }
}

