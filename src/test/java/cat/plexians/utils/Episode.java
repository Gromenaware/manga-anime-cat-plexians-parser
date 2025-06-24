package cat.plexians.utils;

public class Episode {
    private String title;
    private String releaseDate;
    private String infoUrl;
    private String episodeTitle;
    private String mediumQualityUrl;
    private String highQualityUrl;
    private String subtitlesUrl1;
    private String subtitlesUrl2;

    public Episode(String title, String releaseDate, String infoUrl, String episodeTitle, String mediumQualityUrl, String highQualityUrl, String subtitlesUrl1, String subtitlesUrl2) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.infoUrl = infoUrl;
        this.episodeTitle = episodeTitle;
        this.mediumQualityUrl = mediumQualityUrl;
        this.highQualityUrl = highQualityUrl;
        this.subtitlesUrl1 = subtitlesUrl1;
        this.subtitlesUrl2 = subtitlesUrl2;
    }


    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public String getMediumQualityUrl() {
        return mediumQualityUrl;
    }

    public String getHighQualityUrl() {
        return highQualityUrl;
    }

    public String getSubtitlesUrl1() {
        return subtitlesUrl1;
    }

    public String getSubtitlesUrl2() {
        return subtitlesUrl2;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" + "Release Date: " + releaseDate + "\n" + "Info URL: " + infoUrl + "\n" + "Episode Title: " + episodeTitle + "\n" + "Medium Quality URL: " + mediumQualityUrl + "\n" + "High Quality URL: " + highQualityUrl + "\n" + "Subtitles URL 1: " + subtitlesUrl1 + "\n" + "Subtitles URL 2: " + subtitlesUrl2 + "\n";
    }
}
