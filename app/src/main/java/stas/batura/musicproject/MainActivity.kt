package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.developer.filepicker.controller.DialogSelectionListener
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_fragment_new.*
import kotlinx.android.synthetic.main.pager_activity.*
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.ui.control.ControlFragment
import stas.batura.musicproject.ui.dialogs.PlaylistNameDialog
import stas.batura.musicproject.ui.dialogs.TextDialog
import stas.batura.musicproject.ui.playlist.PlaylistFragment
import stas.batura.musicproject.ui.song_decor.SongDecorFragment
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager
import java.io.File


class MainActivity : AppCompatActivity(), DialogSelectionListener {

    val SECT_GROUP_ID : Int = 4476

    private lateinit var navContr : NavController

    val TAG = MainActivity::javaClass.toString()

    lateinit var  mainViewModel : MainAcivityViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration


//    private lateinit var navContr : NavController

//    private lateinit var dataSource : TracksDao
private val NUM_PAGES = 2

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private var viewPager: ViewPager2? = null

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private var pagerAdapter: ScreenSlidePagerAdapter? = null

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
////        toolbar.navi
        toolbar.setNavigationIcon(R.drawable.ic_menu_send)
        setSupportActionBar(toolbar)

//
//        setSupportActionBar(toolbar)
//        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);


        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager);
        pagerAdapter!!.addFragment(SongDecorFragment())
        pagerAdapter!!.addFragment(PlaylistFragment())

        viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager!!.setAdapter(pagerAdapter);

//        navContr = findNavController(this,R.id.nav_host_fragment)

        mainViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMainViewModel(application))
            .get(MainAcivityViewModel::class.java)

        mainViewModel.onActivityCreated()

        mainViewModel.createServiceListner.observe(this, Observer {
            if (it) {
                createMusicService()
            }
        })

        mainViewModel.serviceConnection.observe(this, Observer {
            if (it != null) {
                print("service created")
//                mainViewModel.playClicked()
            }
        })

        mainViewModel.mediaController.observe(this, Observer {
            if (it != null && mainViewModel.playIsClicked) {
                mainViewModel.playClicked()
                mainViewModel.playIsClicked = false
            }
        })

        mainViewModel.playlistListLive.observe(this, Observer {
            if (it != null) {
                Log.d("MainAct","Play")
                createSectionsInMenu(it)
            }
        })

        mainViewModel.mainDataLive.observe(this, Observer {
            if (it != null) {
                Log.d("MainAct", "main")
            }
        })

        mainViewModel.exoPlayer.observe(this, Observer {
            if (it != null) {
                exoplayer_control.player = it
            } else {

            }
        })

        mainViewModel.openTextListner.observe(this, Observer {
            if (it) {
                openTrackTextDialog()
            }
        })
        createBasicNavView()
    }

    /**
     * создаем и привязываем сервис
     */
    private fun createMusicService() {
        // инициализируем муз сервис
        mainViewModel.initMusicService(false)

        // привязываем сервис к активити
        bindService(Intent(applicationContext!!, MusicService::class.java),
            mainViewModel.serviceConnection.value!!,
            Context.BIND_AUTO_CREATE)

    }

    override fun onPause() {
        println("main activity pause")
        super.onPause()
    }

    override fun onStop() {
        println("main activity stop")
        super.onStop()
    }

    override fun onDestroy() {
        mainViewModel.onActivityDestroyed()
        super.onDestroy()
    }

    /**
     * создаем навигейтион лейаут
     */
    private fun createBasicNavView() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navContr = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_tools, R.id.nav_share, R.id.nav_add
            ), drawerLayout
        )
//        setupActionBarWithNavController(navContr, appBarConfiguration)
        navView.setupWithNavController(navContr)

//        createSectionsInMenu(ma)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navContr = findNavController(R.id.nav_host_fragment)
        return navContr.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * создает отображение списка секций в меню
     */
    private fun createSectionsInMenu(playlists : List<Playlist>) {
        var menu = nav_view.menu
        var count : Int = 0
        var listId: MutableList<Int> = ArrayList()

        val view = LayoutInflater.from(this).inflate(R.layout.nav_view_play_item, null)

//        view.findViewById<TextView>()
//        MenuItemCompat.setActionView(menu.getItem(0),1)


        // если уже присутствуют секции то их удаляем
        menu.removeGroup(SECT_GROUP_ID)

        for (playlist in playlists) {
            menu.add(SECT_GROUP_ID, playlist.playlistId, 2, playlist.name )

            count++
            listId.add(playlist.playlistId)
        }

//        // устанавливаем слушатель на нажатие клавиш
        nav_view.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_add -> {
                    Log.d("main", "Home")
                    drawer_layout.closeDrawers()
                    createNewPlaylistDialog()
                    true
                }
                in listId ->  {
                    Log.d("main", "frag$listId")
                    drawer_layout.closeDrawers()

                    pager.currentItem = 1

                    mainViewModel.onNavPlaylistItemClicked(it.itemId)
                    true
                }
                else -> false
            }
        }) )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "press")
                drawer_layout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return false
    }

    /**
     * навигация в фрагмент с плейлистом
     */
    private fun navigateToLeft () {
        navContr.navigate(R.id.playlistFragment)
    }

    /**
     * навигация с фрагментом с управлением
     */
    private fun navigateToRight () {
        navContr.navigate(R.id.controlFragment)
    }

    /**
     * создает новый диалог для создания плейлиста
     */
    private fun createNewPlaylistDialog() {
       val dialog: PlaylistNameDialog = PlaylistNameDialog()
        val fragmentManage = supportFragmentManager
        dialog.show(fragmentManage, "playlist")
        pager.currentItem = 1
    }

    /**
     * после выборв директории
     */
    override fun onSelectedFilePaths(files: Array<out String>?) {
        print("test select")
        mainViewModel.addTracksToPlaylist(files!![0])
    }

    fun openTrackTextDialog() {
        val dialog: TextDialog = TextDialog(mainViewModel)
        val fragmentManager = supportFragmentManager
        dialog.show(fragmentManager, "text")
        mainViewModel.openTextDialogFinish()
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    inner class ScreenSlidePagerAdapter(val fragmentManager: FragmentManager) :
        FragmentStateAdapter(fragmentManager, this@MainActivity.lifecycle) {

        private val arrayList: ArrayList<Fragment> = ArrayList()

        override fun createFragment(position: Int): Fragment {
            return arrayList.get(position)
        }



        public fun addFragment(fragment: Fragment?) {
            arrayList.add(fragment!!)
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }


    }


}
