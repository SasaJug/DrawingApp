package com.sasaj.graphics.drawingapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.aws.CognitoHelper
import com.sasaj.graphics.drawingapp.ui.drawing.DrawingActivity
import com.sasaj.graphics.drawingapp.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val cognitoHelper: CognitoHelper = CognitoHelper(this)
    private var username: String? = null
    private var user: CognitoUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        username = cognitoHelper.userPool.currentUser.userId
        user = cognitoHelper.userPool.getUser(username)

        setFabButton()
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
        user?.signOut()
        val intent = Intent(this, SplashActivity ::class.java)
        startActivity(intent)
        finish()
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

}
