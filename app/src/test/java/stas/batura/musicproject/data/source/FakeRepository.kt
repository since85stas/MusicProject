package stas.batura.musicproject.data.source

import androidx.lifecycle.LiveData
import stas.batura.musicproject.repository.room.MainData
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao

class FakeRepository(): TracksDao() {

    private var trackList: MutableMap<Int, TrackKot> = HashMap()

    override fun setMainPlaylistId(mainData: MainData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateMainPlayilistId(playlistId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMainPlaylistId(id: Long): LiveData<MainData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertTrack(trackKot: TrackKot) {
        trackList.put(trackKot._ID, trackKot)
    }

    override fun insertTracks(tracks: List<TrackKot>) {
        for (track in tracks) {
            trackList.put(track._ID, track)
        }
    }

    override fun getAllTracksFromPlaylist(playlistId: Int): List<TrackKot>? {
        var resList: MutableList<TrackKot> = ArrayList<TrackKot>();
        for (track in trackList) {
            if (track.value.trackPlaylistId == playlistId) {
                resList.add(track.value)
            }
        }
        return resList
    }

    override fun getAllTracks(): List<TrackKot>? {
        return trackList.values.toList()
    }

    override fun deleteAllTracks() {
        trackList = HashMap()
    }

    override fun getAllTracksFromMainPlaylist(): List<TrackKot>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTrackIsPlaying(trackId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlayingTrack(): LiveData<TrackKot> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAllTrackIsNOTPlaying() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTracksInPlayList(playlistId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteTracksInMainPlayList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPlaylist(playlist: Playlist): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPlaylists(): LiveData<List<Playlist>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePlaylistName(playlistId: Int, name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}