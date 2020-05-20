package stas.batura.musicproject.repository.net;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "response", strict = false)
public class SearchResponse {

    @ElementList(name = "list",inline = true, required = false)
    List<SearchLyricResult> results;

}
