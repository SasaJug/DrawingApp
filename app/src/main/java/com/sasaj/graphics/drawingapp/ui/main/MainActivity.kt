package com.sasaj.graphics.drawingapp.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.ui.drawing.DrawingActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setFabButton()
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
