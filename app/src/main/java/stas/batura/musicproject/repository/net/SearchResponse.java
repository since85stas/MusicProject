package stas.batura.musicproject.repository.net;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "GetLyricResult", strict = false)
public class SearchResponse {

//    @ElementList(inline = true, required = false)
//    public List<SearchLyricResult> results;
    @Element(name = "Lyric")
    public String lyrics;

}
