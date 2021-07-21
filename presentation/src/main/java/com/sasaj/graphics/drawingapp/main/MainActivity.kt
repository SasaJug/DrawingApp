package com.sasaj.graphics.drawingapp.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sasaj.graphics.drawingapp.BuildConfig
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.drawing.DrawingActivity
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var mainVMFactory: MainVMFactory

    private lateinit var vm: MainViewModel

    private lateinit var vmNavigation: DrawingListNavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        (application as DrawingApplication).createMainComponent().inject(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        vm = ViewModelProviders.of(this, mainVMFactory).get(MainViewModel::class.java)
        vmNavigation = ViewModelProviders.of(this).get(DrawingListNavigationViewModel::class.java)

        vm.mainLiveData.observe(this, Observer { mainState -> handleResponse(mainState) })
        vm.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                renderErrorState(it)
            }
        })

        vmNavigation.drawingsListLiveData.observe(this, Observer { if (it != null) handleNavigationState(it) })


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DrawingsListFragment(), LIST_FRAGMENT_TAG)
                    .commitNow()
        }

        setFabButton()
        vm.syncData()
        vm.getDrawings()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        vm.signOut()
    }


    private fun handleResponse(mainViewState: MainViewState?) {
        when (mainViewState?.state) {
            MainViewState.LOADING -> renderLoadingState()
            MainViewState.DRAWINGS_LOADED -> renderShowList(mainViewState.drawingsList)
            MainViewState.SIGNOUT_SUCCESSFUL -> renderSuccessSignOutState()
            MainViewState.SYNC_SUCCESSFUL -> renderSuccessSyncDataState()
        }
    }

    private fun handleNavigationState(navigationViewState: DrawingsListNavigationViewState) {
        navigationViewState.imagePath?.let {
            renderItemClicked(navigationViewState.imagePath!!)
        }
    }

    private fun renderShowList(list: List<DrawingUI>?) {
        val navigationViewState = DrawingsListNavigationViewState(list = list)
        vmNavigation.drawingsListLiveData.value = navigationViewState
        hideProgress()
    }

    private fun renderSuccessSyncDataState() {
        hideProgress()
    }

    private fun renderLoadingState() {
        showProgress()
    }

    private fun renderSuccessSignOutState() {
        hideProgress()
        val intent = Intent(this@MainActivity, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error ", throwable)
        showDialogMessage("Error ", throwable.toString())
    }


    private fun renderItemClicked(imagePath: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.parse("file://$imagePath"), "image/*")
            startActivity(intent)
        } else {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                    File(imagePath))
            intent.data = photoUri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }


    private fun setFabButton() {

        speedDial.inflate(R.menu.menu_speed_dial)
        speedDial.setOnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.portrait_option -> {
                    val intent = DrawingActivity.createIntent(this@MainActivity, DrawingActivity.PORTRAIT)
                    startActivity(intent)
                    speedDial.close()
                    true
                }
                R.id.landscape_option -> {
                    val intent = DrawingActivity.createIntent(this@MainActivity, DrawingActivity.LANDSCAPE)
                    startActivity(intent)
                    speedDial.close()
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        vm.onCleared()
        vmNavigation.onCleared()
//        (application as DrawingApplication).releaseMainComponent()
        super.onDestroy()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val LIST_FRAGMENT_TAG = "drawingsListFragment"
    }
}
