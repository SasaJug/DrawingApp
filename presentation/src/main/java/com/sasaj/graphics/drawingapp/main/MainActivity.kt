package com.sasaj.graphics.drawingapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.splash.SplashActivity
import com.sasaj.graphics.drawingapp.drawing.DrawingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        vm = ViewModelProviders.of(this).get(MainViewModel::class.java)
        vm.mainLiveData.observe(this, Observer { mainState -> handleResponse(mainState) })
        vm.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                renderErrorState(it)
            }
        })

        setFabButton()
        vm.syncData()
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
            MainViewState.SIGNOUT_SUCCESSFUL -> renderSuccessSignOutState()
            MainViewState.SYNC_SUCCESSFUL -> renderSuccessSyncDataState()
        }
    }

    private fun renderSuccessSyncDataState() {
        hideProgress()
    }

    private fun renderLoadingState() {
        showProgress("wait...")
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

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
