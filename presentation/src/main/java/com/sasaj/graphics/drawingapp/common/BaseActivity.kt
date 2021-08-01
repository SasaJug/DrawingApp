package com.sasaj.graphics.drawingapp.common

import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sasaj.graphics.drawingapp.R

open class BaseActivity : AppCompatActivity() {

    private var userDialog: AlertDialog? = null
    private var waitDialog: ProgressDialog? = null

    fun showProgress(message: String = getString(R.string.progress_dialog_message)) {
        hideProgress()
        waitDialog = ProgressDialog(this)
        waitDialog?.setTitle(message)
        waitDialog?.show()
    }

    fun showDialogMessage(title: String?, body: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(body).setNeutralButton("OK") { dialog, which ->
            try {
                userDialog?.dismiss()
                resetViewModel()
            } catch (e: Exception) {
                //
            }
        }
        userDialog = builder.create()
        userDialog?.show()
    }

    fun hideProgress() {
        try {
            waitDialog?.dismiss()
        } catch (e: Exception) {
            //
        }

    }

    open fun resetViewModel(){}
}
