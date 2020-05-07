package stas.batura.musicproject.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import stas.batura.musicproject.repository.room.MainData
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.repository.room.TrackKot
import stas.batura.musicproject.repository.room.TracksDao

class FakeRepositoryAndr(): TracksDao() {

    private var trackMap: MutableMap<String, TrackKot> = HashMap()

    private var tracksList: MutableList<TrackKot> = ArrayList<TrackKot>()

//    private

    private var mainDataId: Int = 0

    public fun setMainIdName (id: Int) {
        mainDataId = id
    }

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
        trackMap.put(trackKot.title, trackKot)
        tracksList.add(trackKot)
    }

    override fun insertTracks(tracks: List<TrackKot>) {
        for (track in tracks) {
            trackMap.put(track.title, track)
            tracksList.add(track)
        }
    }

    override fun getAllTracksFromPlaylist(playlistId: Int): List<TrackKot>? {
        var resList: MutableList<TrackKot> = ArrayList<TrackKot>();
        for (track in tracksList) {
            if (track.trackPlaylistId == playlistId) {
                resList.add(track)
            }
        }
        return resList
    }

    override fun getAllTracks(): List<TrackKot>? {
        return tracksList.toList()
    }

    override fun deleteAllTracks() {
        tracksList = ArrayList()
    }

    override fun getAllTracksFromMainPlaylist(): List<TrackKot>? {
        var resList: MutableList<TrackKot> = ArrayList<TrackKot>();
        for (track in tracksList) {
            if (track.trackPlaylistId == mainDataId) {
                resList.add(track)
            }
        }
        return resList
    }

    override fun setTrackIsPlaying(trackId: Int) {
        tracksList.get(trackId).isPlaying = true
    }

    override fun getPlayingTrack(): LiveData<TrackKot> {
        val live: MutableLiveData<TrackKot> = MutableLiveData()
        for (track in tracksList) {
            if (track.isPlaying) {
                 live.postValue(track)
                return live
            }
        }
        return live
    }

    override fun setAllTrackIsNOTPlaying() {
        for (track in tracksList) {
            track.isPlaying = false
        }
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
}