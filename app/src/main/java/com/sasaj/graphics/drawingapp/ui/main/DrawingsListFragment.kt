package com.sasaj.graphics.drawingapp.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sasaj.graphics.drawingapp.BuildConfig
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.domain.Drawing
import com.sasaj.graphics.drawingapp.ui.main.adapter.DrawingsListAdapter
import java.io.File
import java.util.*

class DrawingsListFragment : Fragment() {

    companion object {
        private const val NUMBER_OF_COLUMNS_PORTRAIT = 3
        private const val NUMBER_OF_COLUMNS_LANDSCAPE = 5
    }

    private lateinit var vm: DrawingsListViewModel
    private lateinit var drawingsList: RecyclerView
    private lateinit var adapter: DrawingsListAdapter

    private var drawingItemListener: DrawingItemListener = object : DrawingItemListener {
        override fun onItemClicked(clickedItem: Drawing) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(Uri.parse("file://" + clickedItem.imagePath), "image/*")
                startActivity(intent)
            } else {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val photoUri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID + ".provider",
                        File(clickedItem.imagePath))
                intent.data = photoUri
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_drawings_list, container, false)
        drawingsList = root.findViewById(R.id.drawings_grid)

        val orientation = activity!!.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            drawingsList.layoutManager = GridLayoutManager(activity, NUMBER_OF_COLUMNS_PORTRAIT)
        } else {
            drawingsList.layoutManager = GridLayoutManager(activity, NUMBER_OF_COLUMNS_LANDSCAPE)
        }

        adapter = DrawingsListAdapter(activity!!, ArrayList(0), drawingItemListener)
        drawingsList.adapter = adapter

        vm = ViewModelProviders.of(this)[DrawingsListViewModel::class.java]

        val observer = Observer<List<Drawing>> { value ->
            if (value != null)
                adapter?.setDrawings(value)
        }
        vm.drawings.observe(activity!!, observer)

        return root
    }

    override fun onResume() {
        super.onResume()
        vm.getDrawings()
    }

    interface DrawingItemListener {
        fun onItemClicked(clickedItem: Drawing)
    }
}