package stas.batura.musicproject.utils

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import kotlinx.coroutines.runBlocking
import stas.batura.musicproject.MainAcivityViewModel
import stas.batura.musicproject.musicservice.MusicRepository
import stas.batura.musicproject.repository.Repository
import stas.batura.musicproject.repository.net.RetrofitClient
import stas.batura.musicproject.repository.room.TracksDao
import stas.batura.musicproject.repository.room.TracksDatabase
import stas.batura.musicproject.ui.playlist.PlaylistViewModel

object InjectorUtils {

    private var lock = Any()

    var tracksDao : TracksDao? = null

    @Volatile
    var repository: TracksDao? = null
        @VisibleForTesting set

    @Volatile
    var musicRepository: MusicRepository? = null

    var retrofit: RetrofitClient.API_COUR? = null

    private fun provideDao (application: Context): TracksDao {
        if (tracksDao == null) {
            tracksDao = TracksDatabase.getInstance(application).tracksDatabaseDao
        }
        return tracksDao!!
    }

    fun provideRep (application: Context): TracksDao {
        if (repository == null) {
            repository = Repository(provideDao(application))
        }
        return repository!!
    }

    fun provideMusicRep(application: Context): MusicRepository {
        if (musicRepository == null) {
            musicRepository = MusicRepository(provideRep(application))
        }
        return musicRepository!!
    }

    fun provideMainViewModel ( application: Application ) : MainAcivityViewModel.Factory {
        return MainAcivityViewModel.Factory(application, provideRep(application), provideMusicRep(application))
    }

    fun providePlaylistViewModel (application: Application) : PlaylistViewModel.Factory {
        return PlaylistViewModel.Factory(provideRep(application), provideMusicRep(application))
    }

    fun provideRetrofit(): RetrofitClient.API_COUR {
        if (retrofit == null) {
            retrofit = RetrofitClient.netApi.retrofitServise
        }
        return retrofit!!
    }


    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                if (tracksDao!=null) {
                    tracksDao!!.deleteAllTracks()
                }
            }
            // Clear all data to avoid test pollution.
//            database?.apply {
//                clearAllTables()
//                close()
//            }
            repository = null
            musicRepository = null
            tracksDao = null
        }
    }

}