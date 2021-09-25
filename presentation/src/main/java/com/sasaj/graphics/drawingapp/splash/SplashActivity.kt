package com.sasaj.graphics.drawingapp.splash


import android.Manifest
import androidx.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.sasaj.domain.usecases.CheckIfLoggedIn
import com.sasaj.graphics.drawingapp.common.BaseActivity
import com.sasaj.graphics.drawingapp.authentication.AuthenticationActivity
import com.sasaj.graphics.drawingapp.common.BaseViewModel
import com.sasaj.graphics.drawingapp.common.SingleLiveEvent
import com.sasaj.graphics.drawingapp.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by sjugurdzija on 4/22/2017
 */

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val vm by viewModels<SplashViewModel>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                checkIfLoggedIn()
            }
        } else {
            checkIfLoggedIn()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkIfLoggedIn()
                } else {
                    finish()
                }
                return
            }
        }
    }

    private fun checkIfLoggedIn() {
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
        } else {
            Log.i(TAG, "No logIn details")
            val intent = Intent(this@SplashActivity, AuthenticationActivity::class.java)
            startActivity(intent)
        }
        finish()
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error checking logIn status: ", throwable)
        showDialogMessage("Error checking logIn status", throwable.toString())
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        private const val MY_PERMISSIONS_REQUEST_STORAGE = 10
    }
}


@HiltViewModel
class SplashViewModel @Inject constructor(private val checkIfLoggedIn: CheckIfLoggedIn) : BaseViewModel() {

    private val splashLiveData: MutableLiveData<SplashViewState> = MutableLiveData()
    var errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    init {
        val splashViewState = SplashViewState()
        splashLiveData.value = splashViewState
    }

    fun getSplashLiveData(): MutableLiveData<SplashViewState> {
        return splashLiveData
    }

    fun checkIfLoggedIn() {
        addDisposable(checkIfLoggedIn.checkIfLoggedIn()
            .subscribe(
                { s: String ->
                    val newSplashViewState = splashLiveData.value?.copy(loading = false, username = s)
                    splashLiveData.value = newSplashViewState
                    errorState.value = null
                },
                { e ->
                    splashLiveData.value = splashLiveData.value?.copy(loading = false, username = "")
                    errorState.value = e
                },
                { Log.i(TAG, "Check user logged in completed") }
            )
        )
    }


    companion object {
        private val TAG = SplashViewModel::class.java.simpleName
    }
}
