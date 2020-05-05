package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.Repository
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.repository.room.TracksDatabase
import stas.batura.musicproject.ui.playlist.PlaylistViewModel

object InjectorUtils {


    var tracksDao : TracksDao? = null

    @Volatile
    var repository: Repository? = null

    var musicRepository: MusicRepository? = null

    private fun provideDao (application: Application) : TracksDao {
        if (tracksDao == null) {
            tracksDao = TracksDatabase.getInstance(application).tracksDatabaseDao
        }
        return tracksDao!!
    }

    fun provideRep (application: Application): Repository {
        if (repository == null) {
            repository = Repository(provideDao(application))
        }
        return repository!!
    }

    fun provideMusicRep(application: Application): MusicRepository {
        if (musicRepository == null) {
            musicRepository = MusicRepository.getInstance(application)
        }
        return musicRepository!!
    }

    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {
        val dataSource = TracksDatabase.getInstance(application).tracksDatabaseDao
        return MainAcivityViewModel.Factory(application, provideRep(application), provideMusicRep(application))
    }

    fun providePlaylistViewModel (application: Application) : PlaylistViewModel.Factory {
        return PlaylistViewModel.Factory(application, provideRep(application), provideMusicRep(application))
    }

}