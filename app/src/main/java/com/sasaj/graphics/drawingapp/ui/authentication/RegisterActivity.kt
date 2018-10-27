package com.sasaj.graphics.drawingapp.ui.authentication

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.base.BaseActivity
import com.sasaj.graphics.drawingapp.ui.main.MainActivity
import com.sasaj.graphics.drawingapp.viewmodel.RegisterViewModel
import com.sasaj.graphics.drawingapp.viewmodel.common.Response
import com.sasaj.graphics.drawingapp.viewmodel.common.Status
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    private lateinit var vm: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        vm = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        vm.getRegisterLiveData().observe(this, Observer { response -> processResponse(response) })

        signUpButton!!.setOnClickListener {
            val attributes: HashMap<String, String> = HashMap()
            attributes.put("email", email!!.text.toString())
            vm.register(username!!.text.toString(), password!!.text.toString(), attributes)

        }
    }

    private fun processResponse(response: Response?) {
        when (response?.status) {
            Status.LOADING -> renderLoadingState()
            Status.SUCCESS -> renderSucessRegisteringState(response.data)
            Status.ERROR -> renderErrorState(response.error)
        }
    }

    override fun resetViewModel() {
        vm.resetLiveData()
    }

    private fun renderLoadingState() {
        showProgress("wait...")
    }

    private fun renderSucessRegisteringState(status: String?) {
        hideProgress()
        if (status == "confirmed") {
            Log.i(TAG, "Succesfully registered")
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.i(TAG, "Go to verify")
            val intent = Intent(this@RegisterActivity, VerifyActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderErrorState(throwable: Throwable?) {
        hideProgress()
        Log.e(TAG, "Error logging in ", throwable)
        showDialogMessage("Error logging in", throwable.toString())
    }


    companion object {

        private val TAG = RegisterActivity::class.java.simpleName
    }
}
