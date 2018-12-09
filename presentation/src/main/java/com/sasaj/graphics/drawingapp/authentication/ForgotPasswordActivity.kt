package com.sasaj.graphics.drawingapp.authentication

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sasaj.graphics.drawingapp.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        getCodeButton?.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, NewPasswordActivity::class.java)
            intent.putExtra("username", forgotPasswordUsername.text.toString())
            startActivity(intent)
        }
    }
}
