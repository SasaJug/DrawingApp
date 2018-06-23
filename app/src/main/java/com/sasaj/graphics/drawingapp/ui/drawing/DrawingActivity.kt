package com.sasaj.graphics.drawingapp.ui.drawing

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation
import kotlinx.android.synthetic.main.activity_drawing.*

class DrawingActivity : AppCompatActivity(){

    companion object {

        val ORIENTATION = "ORIENTATION"
        val LANDSCAPE = 0
        val PORTRAIT = 1

        fun createIntent(context: Context, orientation: Int): Intent {
            val intent = Intent(context, DrawingActivity::class.java)
            intent.putExtra(ORIENTATION, orientation)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setScreenOrientation(intent.getIntExtra(ORIENTATION, PORTRAIT))
        setContentView(R.layout.activity_drawing)
        setSupportActionBar(toolbar_drawing)

    }

    private fun setScreenOrientation(orientation: Int) {
        requestedOrientation = if (orientation == PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
}

