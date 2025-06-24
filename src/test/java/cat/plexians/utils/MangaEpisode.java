package cat.plexians.utils;

import java.util.List;

public class MangaEpisode {

    String fansub;
    String name;
    int volume;
    int episodeNumber;
    String volumeStr;
    String episodeNumberStr;
    String fileId;

    public MangaEpisode(int volume, int episodeNumber, String fileId) {
        this.volume = volume;
        this.episodeNumber = episodeNumber;
        this.fileId = fileId;
    }

    public MangaEpisode(String name, String fansub, int volume, int episodeNumber, String fileId) {
        this.name = name;
        this.fansub = fansub;
        this.volume = volume;
        this.episodeNumber = episodeNumber;
        this.fileId = fileId;
    }

    public MangaEpisode(String name, String fansub, String volumeStr, String episodeNumberStr, String fileId) {
        this.name = name;
        this.fansub = fansub;
        this.volumeStr = volumeStr;
        this.episodeNumberStr = episodeNumberStr;
        this.fileId = fileId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getFansub() {
        return fansub;
    }

    public int getVolumeNumber() {
        return volume;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getVolumeNumberStr() {
        return volumeStr;
    }

    public String getEpisodeNumberStr() {
        return episodeNumberStr;
    }

    public String getFileId() {
        return fileId;
    }

    // Override toString() for easy printing
    @Override
    public String toString() {
        return "Volume " + volume + " Episode " + episodeNumber + ": fileId=" + fileId;
    }

    public static void addEpisode(List<MangaEpisode> episodes, int volume, int episodeNumber, String fileId) {
        if (fileId != null) {
            episodes.add(new MangaEpisode(volume, episodeNumber, fileId));
        }
    }
}
