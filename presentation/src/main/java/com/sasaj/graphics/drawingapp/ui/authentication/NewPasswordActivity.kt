package com.sasaj.graphics.drawingapp.ui.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.ChangePasswordViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.common.Status
import kotlinx.android.synthetic.main.activity_new_password.*

class NewPasswordActivity : BaseActivity() {

    private lateinit var vm: ChangePasswordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)


        vm = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        vm.getChangePasswordLiveData().observe(this, Observer { response -> processResponse(response) })

        vm.changePassword(intent.getStringExtra("username"))

        changePasswordButton.setOnClickListener {
            if(newPassword.text.toString().equals(confirmPassword.text.toString())){
                vm.newPassword(newPassword.text.toString(), newPasswordCode.text.toString())
            } else {
                showDialogMessage("Error", "Passwords do not match!")
            }

        }
    }

    override fun resetViewModel() {
        vm.resetLiveData()
    }

    private fun processResponse(response: Response?) {
        when (response?.status) {
            Status.LOADING -> renderLoadingState()
            Status.SUCCESS -> renderSucessLoggingInState(response.data)
            Status.ERROR -> renderErrorState(response.error)
        }
    }


    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderSucessLoggingInState(username: String?) {
        hideProgress()
        if (username == "success") {
            Log.i(TAG, "Succesfully logged in")
            val intent = Intent(this@NewPasswordActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            showDialogMessage("Error logging in", username)
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error logging in ", throwable)
        showDialogMessage("Error logging in", throwable.toString())
    }

    companion object {
        private val TAG = NewPasswordActivity::class.java.simpleName
    }
}
