package stas.batura.musicproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.developer.filepicker.controller.DialogSelectionListener
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_fragment_new.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.pager_activity.*
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.repository.room.Playlist
import stas.batura.musicproject.ui.dialogs.DeleteAlertDialog
import stas.batura.musicproject.ui.dialogs.PlaylistListDialog
import stas.batura.musicproject.ui.dialogs.PlaylistNameDialog
import stas.batura.musicproject.ui.dialogs.TextDialog
import stas.batura.musicproject.ui.playlist.PlaylistFragment
import stas.batura.musicproject.ui.song_decor.SongDecorFragment
import stas.batura.musicproject.utils.CircleTransform
import stas.batura.musicproject.utils.ContexUtils
import stas.batura.musicproject.utils.InjectorUtils
import java.io.File


class MainActivity : AppCompatActivity(), DialogSelectionListener {

    lateinit var  mFirebaseAnalytics: FirebaseAnalytics

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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        toolbar.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
////        toolbar.navi
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp)
        toolbar.setBackgroundColor(resources.getColor(R.color.contrBackColor))
        toolbar.title = ""
        setSupportActionBar(toolbar)



//        navContr = findNavController(this,R.id.nav_host_fragment)

//        mainViewModel = ViewModelProviders
//            .of(this, InjectorUtils.provideMainViewModel(application))
//            .get(MainAcivityViewModel::class.java)

        mainViewModel = ViewModelProvider((application as MusicApplication).modelStore,
            InjectorUtils.provideMainViewModel(application))
            .get(MainAcivityViewModel::class.java)

        mainViewModel.onActivityCreated()

        createBasicNavView()

        if (!ContexUtils.checkStorageAccessPermissions(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT
                )
            }
        }

//        if (mainViewModel.serviseIsCreated) {
//            bindCurrentService()
//        }
        Log.d(TAG, "onCreate")
    }

    /**
     * добавляем слушатели для модели
     */
    private fun addObservers() {
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
            }
        })

        mainViewModel.openTextListner.observe(this, Observer {
            if (it) {
                openTrackTextDialog()
            }
        })

        mainViewModel.currentPlaylistName.observe(this, Observer {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            if (it != null) {
                toolbar.title = it
            } else {
                toolbar.title = "No playlist"
            }
        })

        mainViewModel.currentTrackPlaying.observe(this, Observer {
            if (it != null) {
                val param: Bundle = Bundle()
                param.putString("SONG",it.title)
                param.putString("ARTIST", it.artist)

                mFirebaseAnalytics.logEvent("Play", param)
                print("play")
            }
        })
    }

    /**
     * убераем слушание если активити не видно
     */
    private fun removeObservers() {
        mainViewModel.createServiceListner.removeObservers(this)
        mainViewModel.serviceConnection.removeObservers(this)
        mainViewModel.mediaController.removeObservers(this)
        mainViewModel.playlistListLive.removeObservers(this)
        mainViewModel.mainDataLive.removeObservers(this)
        mainViewModel.exoPlayer.removeObservers(this)
        mainViewModel.openTextListner.removeObservers(this)
        mainViewModel.currentPlaylistName.removeObservers(this)
        mainViewModel.currentTrackPlaying.removeObservers(this)
    }

    /**
     * создаем и привязываем сервис
     */
    private fun createMusicService() {
        // инициализируем муз сервис
        mainViewModel.initMusicService(false)

        // привязываем сервис к активити
        bindCurrentService()

    }

    private fun bindCurrentService() {
        // привязываем сервис к активити
        bindService(Intent(applicationContext!!, MusicService::class.java),
            mainViewModel.serviceConnection.value!!,
            Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        addObservers()
//        if (pagerAdapter!=null) {
//            pagerAdapter!!.createFragment(0)
//            pagerAdapter!!.createFragment(1)
//        }
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragment(SongDecorFragment())
        pagerAdapter!!.addFragment(PlaylistFragment())

        viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager!!.setAdapter(pagerAdapter)

        super.onStart()
    }

    override fun onPause() {
        println("main activity pause")
//        removeObservers()
        super.onPause()
    }

    override fun onStop() {
        removeObservers()
        pagerAdapter!!.removeFragments()
        println("main activity stop")
        super.onStop()
    }

    /**
     * после закрытия активити выключаем сервис, и убираем связывание
     */
    override fun onDestroy() {
        if (mainViewModel.mediaController.value != null) {
            mainViewModel.mediaController.value!!.transportControls.stop()
        }
        if ((mainViewModel.serviceConnection.value != null)) {
            unbindService(mainViewModel.serviceConnection.value!!)
        }
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
                R.id.nav_home, R.id.nav_add
            ), drawerLayout
        )
//        setupActionBarWithNavController(navContr, appBarConfiguration)
        navView.setupWithNavController(navContr)

//        createSectionsInMenu(ma)
        loadNavHeader()
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private fun loadNavHeader() { // name, website
        val navView = nav_view.getHeaderView(0)
        navView.name.text = ("Stanislav Batura")
        navView.website.text = ("stanislav.batura85@gmail.com")
        navView.img_header_bg.setImageResource(R.drawable.drawer_back)
        Glide.with(this).load(R.drawable.cat_my).transform(CircleTransform(this))
            .into(navView.img_profile)
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
                R.id.nav_home -> {
                    drawer_layout.closeDrawers()
                    true
                }

                R.id.find_text -> {
                    mainViewModel.getTrackText()
                    true
                }

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

            R.id.action_add -> {
                openFileSelectDialog()
                return true
            }

            R.id.action_delete -> {
                openConfirmDeleteDialog()
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
     * после выборв директории
     */
//    override fun onSelectedFilePaths(files: Array<out String>?) {
//        print("test select")
//        playlistViewModel.addTracksToPlaylist(files!![0])
//    }

    /**
     * создаем диалог для выбора директории
     */
    private fun openFileSelectDialog() {
        pager.currentItem = 1

        // test
        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.MULTI_MODE
        properties.selection_type = DialogConfigs.FILE_AND_DIR_SELECT
        properties.root = File(DialogConfigs.DEFAULT_DIR)
        properties.error_dir = File(DialogConfigs.DEFAULT_DIR)
        properties.offset = File(DialogConfigs.DEFAULT_DIR)
        properties.extensions = null
        properties.show_hidden_files = false
        val dialog = FilePickerDialog(this, properties)
        dialog.setTitle("Select a File")
        dialog.setDialogSelectionListener (this)
        dialog.show()
    }

    /**
     * открываем диалог с выбором плейлистов
     */
    fun openPlaylistSelectDialog() {
        val dialog = PlaylistListDialog(mainViewModel.playlistListLive.value!!, mainViewModel)
        val fragmentManage = supportFragmentManager
        dialog.show(fragmentManage, "playlist")
    }

    /**
     * создает диалог с подтверждением действия
     */
    fun openConfirmDeleteDialog() {
        val delDialog = DeleteAlertDialog()
        val fragmentManager = supportFragmentManager
//        delDialog.setTargetFragment(this, 1111)
        delDialog.show(fragmentManager, "del playlist")
    }

    /**
     * подтверждаем удаление
     */
    fun deletePlaylistOk() {
        mainViewModel.deletePlaylist()
    }

    /**
     * A simple pager adapter that represents 2 ScreenSlidePageFragment objects, in
     * sequence.
     */
    inner class ScreenSlidePagerAdapter(val fragmentManager: FragmentManager) :
        FragmentStateAdapter(fragmentManager, this@MainActivity.lifecycle) {

        private var arrayList: ArrayList<Fragment> = ArrayList()

        override fun createFragment(position: Int): Fragment {
            Log.d(TAG, "fragment pos=${position} created")
            return arrayList.get(position)
        }

        fun addFragment(fragment: Fragment?) {
            arrayList.add(fragment!!)
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        fun removeFragments() {
            arrayList = ArrayList()
        }

    }


}

