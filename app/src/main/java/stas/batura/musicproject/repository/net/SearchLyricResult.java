package stas.batura.musicproject.repository.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "SearchLyricResult", strict = false)
public class SearchLyricResult {

    @Element int TrackId ;

    @Element String LyricChecksum;

    @Element int LyricId;

    @Element String SongUrl;

    @Element String ArtistUrl;

    @Element String Artist;

    @Element String Song;

    @Element int SongRank;
}
