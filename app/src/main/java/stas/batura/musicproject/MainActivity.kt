package stas.batura.musicproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import stas.batura.musicproject.musicservice.MusicService
import stas.batura.musicproject.ui.control.ControlFragment
import stas.batura.musicproject.ui.playlist.PlaylistFragment
import stas.batura.musicproject.utils.InjectorUtils
import stas.batura.musicproject.utils.SongsManager


class MainActivity : FragmentActivity() {

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

    private var x1   = 0f
    private  var x2  = 0f
    val MIN_DISTANCE = 150

    val TAG = MainActivity::javaClass.toString()

    lateinit var  mainViewModel : MainAcivityViewModel

//    private lateinit var navContr : NavController

//    private lateinit var dataSource : TracksDao

    /**
     * создаем активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

//        dataSource = TracksDatabase.getInstance(this).tracksDatabaseDao

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager);
        pagerAdapter!!.addFragment(ControlFragment())
        pagerAdapter!!.addFragment(PlaylistFragment())

        viewPager!!.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager!!.setAdapter(pagerAdapter);
//        navContr = Navigation.findNavController(this, R.id.nav_host_fragment)

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
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    inner final class ScreenSlidePagerAdapter(val fragmentManager: FragmentManager) :
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

    /**
     * навигация в фрагмент с плейлистом
     */
//    private fun navigateToLeft () {
//        navContr.navigate(R.id.playlistFragment)
//    }

    /**
     * навигация с фрагментом с управлением
     */
//    private fun navigateToRight () {
//        navContr.navigate(R.id.controlFragment)
//    }

    /**
     * проверяем слайд вправо и влево
     */
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        val myAction: Int = MotionEventCompat.getActionMasked(event)
//
//        return when (myAction) {
//            MotionEvent.ACTION_UP -> {
//                x2 = event!!.x
//                val deltaX = x2 - x1
//                if (Math.abs(deltaX) > MIN_DISTANCE) {
//                    if (deltaX < 0) {
//                        navigateToRight()
//                    } else {
//                        navigateToLeft()
//                    }
//                }
//                true
//            }
//
//            MotionEvent.ACTION_DOWN -> {
//                x1 = event!!.getX()
//                true
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                true
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                true
//            }
//            else -> super.onTouchEvent(event)
//        }
//    }


}
