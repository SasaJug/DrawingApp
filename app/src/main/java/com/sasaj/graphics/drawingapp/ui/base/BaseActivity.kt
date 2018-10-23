package com.sasaj.graphics.drawingapp.ui.base

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog

open class BaseActivity : AppCompatActivity() {

    private var userDialog: AlertDialog? = null
    private var waitDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showProgress(message: String) {
        hideProgress()
        waitDialog = ProgressDialog(this)
        waitDialog?.setTitle(message)
        waitDialog?.show()
    }

    fun showDialogMessage(title: String, body: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(body).setNeutralButton("OK") { dialog, which ->
            try {
                userDialog?.dismiss()
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
}
