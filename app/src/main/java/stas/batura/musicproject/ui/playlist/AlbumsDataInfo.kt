package stas.batura.musicproject.ui.playlist

import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.room.Track
import stas.batura.musicproject.repository.room.TrackKot

class AlbumsDataInfo {

    private lateinit var tracks: MutableList<MusicRepository.Track>

    constructor(tracks: List<MusicRepository.Track>) {
        this.tracks = tracks.toMutableList();
    }

    fun getAlbumsData(): List<AlbumsViewHolder> {
        var list = ArrayList<AlbumsViewHolder>()
        var albumNames = HashMap<String, AlbumsViewHolder>()

        for (track in tracks) {
            albumNames.put(track.album, AlbumsViewHolder(track.album))
        }

        for (track in tracks) {
            albumNames.get(track.album)!!.addTrack(track)
        }
        return albumNames.values.toList()
    }

    class AlbumsViewHolder {
        var albumName: String? = null
        var albumTracks: MutableList<MusicRepository.Track>? = null

        constructor(name: String) {
            albumName = name
            albumTracks = ArrayList<MusicRepository.Track>()
        }

        fun addTrack(track: MusicRepository.Track) {
            albumTracks!!.add(track)
        }
    }

}