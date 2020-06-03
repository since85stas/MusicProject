package stas.batura.musicproject.ui.playlist

import stas.batura.musicproject.musicservice.MusicRepository

class AlbumsDataInfo {

    private lateinit var tracks: MutableList<MusicRepository.Track>

    constructor(tracks: List<MusicRepository.Track>) {
        this.tracks = tracks.toMutableList();
    }

    fun getAlbumsData(): List<AlbumsViewHolder> {
        var list = ArrayList<AlbumsViewHolder>()
        var albumNames = LinkedHashMap<String, AlbumsViewHolder>()

        for (track in tracks) {
            albumNames.put(track.album, AlbumsViewHolder(track.album))
        }

        for (track in tracks) {
            albumNames.get(track.album)!!.addTrack(track)
        }
        return albumNames.values.toList()
    }

    fun getTracksInAlbumsOrder(): List<MusicRepository.Track> {
//        tracks = list.toMutableList()
        val albums = getAlbumsData()

        val newList: MutableList<MusicRepository.Track> = ArrayList<MusicRepository.Track>()
        for (alb in albums) {
            for (tr in alb.albumTracks!!) {
                newList.add(tr)
            }
        }
        return newList
    }

    fun getSongsNum(): Int {
        return tracks.size
    }

    fun getFullDuration(): Long {
        var dur = 0L
        for (tr in tracks) {
            dur += tr.duration
        }
        return dur
    }

    class AlbumsViewHolder {

        var albumYear: Int = 0
        var albumName: String? = null
        var albumTracks: MutableList<MusicRepository.Track>? = null

        constructor(name: String) {
            albumName = name
            albumTracks = ArrayList<MusicRepository.Track>()
        }

        fun addTrack(track: MusicRepository.Track) {
            albumTracks!!.add(track)
            albumYear = track.year
        }
    }

}