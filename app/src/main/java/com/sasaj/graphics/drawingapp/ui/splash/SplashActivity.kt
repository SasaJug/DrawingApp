package com.sasaj.graphics.drawingapp.ui.splash


import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.sasaj.graphics.drawingapp.ui.authentication.LoginActivity
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.SplashViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.common.Status.*

/**
 * Created by sjugurdzija on 4/22/2017
 */

class SplashActivity : BaseActivity() {


    private lateinit var vm: SplashViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_STORAGE)

            } else {
                findCurrent()
            }
        } else {
            findCurrent()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    findCurrent()
                } else {
                    finish()
                }
                return
            }
        }
    }

    private fun findCurrent() {
        vm = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        vm.response().observe(this, Observer { response -> processResponse(response) })
        vm.checkIfLoggedIn()
    }

    private fun processResponse(response: Response?) {
        when (response?.status) {
            LOADING -> renderLoadingState()
            SUCCESS -> renderAlreadyLoggedInState(response.data)
            ERROR -> renderErrorState(response.error)
        }
    }

    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderAlreadyLoggedInState(username: String?) {
        hideProgress()
        if (username != "") {
            Log.i(SplashActivity.TAG, "Already logged in: $username")
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.i(SplashActivity.TAG, "No login details")
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(SplashActivity.TAG, "Error checking login status: ", throwable)
        showDialogMessage("Error checking login status", throwable.toString())
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 10
    }

}
