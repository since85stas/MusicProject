package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager


class MainActivity : AppCompatActivity() {

    private lateinit var navContr : NavController

    val TAG = MainActivity::javaClass.toString()

    lateinit var  mainViewModel : MainAcivityViewModel

    private lateinit var appBarConfiguration: AppBarConfiguration


//    private lateinit var navContr : NavController

//    private lateinit var dataSource : TracksDao

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu_send)

        navContr = findNavController(this,R.id.nav_host_fragment)

        mainViewModel = ViewModelProviders
            .of(this, InjectorUtils.provideMainViewModel(this.application))
            .get(MainAcivityViewModel::class.java)

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
            if (it != null) {
                mainViewModel.playClicked()
            }
        })

        val songsManager = SongsManager();
        val playlist = songsManager.playList
//        createMusicService()

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
            mainViewModel.serviceConnection!!.value!!,
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
                R.id.nav_home, R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        setupActionBarWithNavController(this, navContr)
        navView.setupWithNavController(navContr)
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
    private fun createSectionsInMenu(sections : List<Int>) {
        var menu = nav_view.menu
        var listId: MutableList<Int> = ArrayList()

        var count : Int = 0

//        // если уже присутствуют секции то их удаляем
//        menu.removeGroup(SECT_GROUP_ID)
//
//        for (section in sections) {
//            menu.add(SECT_GROUP_ID,section.sectionId.toInt(),2, section.sectionName + count  )
//            count++
//            listId.add(section.sectionId.toInt())
//        }
//
//        // устанавливаем слушатель на нажатие клавиш
//        nav_view.setNavigationItemSelectedListener( (NavigationView.OnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.nav_home -> {
//                    Log.d("main", "Home")
//                    drawer_layout.closeDrawers()
//                    true
//                }
//                in listId ->  {
//                    Log.d("main", "frag$listId")
//                    val result = mainActivityViewModel.setCurrentSection(it.itemId)
//                    drawer_layout.closeDrawers()
//                    true
//                }
//                else -> false
//            }
//        }) )

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




}
