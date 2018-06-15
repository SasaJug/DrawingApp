package com.sasaj.graphics.drawingapp.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation
import com.sasaj.graphics.drawingapp.ui.drawing.DrawingActivity
import kotlinx.android.synthetic.main.activity_drawing.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DrawingsListFragment.OnFragmentInteractionListener, DrawingsListContract.View {

    private var actionsListener: DrawingsListPresenter? = null
    private var drawingsListFragment: DrawingsListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        actionsListener = DrawingsListPresenter(this, DrawingsRepositoryImplementation.getInstance())

        drawingsListFragment = supportFragmentManager.findFragmentById(R.id.container) as DrawingsListFragment?
        if (drawingsListFragment == null) {
            drawingsListFragment = DrawingsListFragment.newInstance(null, null)
            initFragment(drawingsListFragment)
        }

        setFabButton()
    }


    private fun initFragment(drawingsListFragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.container, drawingsListFragment)
        transaction.commit()
    }


    override fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress?.visibility = View.GONE
    }

    override fun setDrawingsData(drawings: MutableList<Drawing>?) {
        drawingsListFragment!!.setDrawingsList(drawings)
    }

    override fun getDrawings() {
        actionsListener!!.getDrawings()
    }

    private fun setFabButton() {

        speedDial.inflate(R.menu.menu_speed_dial)
        speedDial.setOnActionSelectedListener({ speedDialActionItem ->
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
        })
    }
}
