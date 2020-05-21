package stas.batura.musicproject.repository.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SearchLyricResult", strict = false)
    public class SearchLyricResult {

        @Element (name = "TrackId")
        int TrackId ;

        @Element  (required = false)
        String LyricChecksum;

        @Element  (required = false)
        int LyricId;

        @Element  (required = false)
        String SongUrl;

        @Element  (required = false)
        String ArtistUrl;

        @Element  (required = false)
        String Artist;

        @Element  (required = false)
        String Song;

        @Element  (required = false)
        int SongRank;
}
