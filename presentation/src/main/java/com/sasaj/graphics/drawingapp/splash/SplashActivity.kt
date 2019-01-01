package com.sasaj.graphics.drawingapp.splash


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
import com.sasaj.graphics.drawingapp.DrawingApplication
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.authentication.AuthenticationActivity
import com.sasaj.graphics.drawingapp.main.MainActivity
import javax.inject.Inject

/**
 * Created by sjugurdzija on 4/22/2017
 */

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var splashVMFactory: SplashVMFactory

    private lateinit var vm: SplashViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as DrawingApplication).createSplashComponent().inject(this)

        vm = ViewModelProviders.of(this, splashVMFactory).get(SplashViewModel::class.java)
        vm.getSplashLiveData().observe(this, Observer {
            if (it != null) handleViewState(it)
        })
        vm.errorState.observe(this, Observer { throwable ->
            throwable?.let {
                renderErrorState(throwable)
            }
        })
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
        vm.checkIfLoggedIn()
    }

    private fun handleViewState(splashViewState: SplashViewState) {
        if (splashViewState.loading)
            renderLoadingState()
        else
            renderAlreadyLoggedInState(splashViewState.username)
    }

    private fun renderLoadingState() {
        showProgress()
    }

    private fun renderAlreadyLoggedInState(username: String?) {
        hideProgress()
        if (username != "") {
            Log.i(TAG, "Already logged in: $username")
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.i(TAG, "No logIn details")
            val intent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error checking logIn status: ", throwable)
        showDialogMessage("Error checking logIn status", throwable.toString())
    }

    override fun onDestroy() {
        (application as DrawingApplication).releaseSplashComponent()
        super.onDestroy()
    }


    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 10
    }

}
