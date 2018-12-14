package com.sasaj.graphics.drawingapp.main

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
import com.sasaj.domain.entities.Drawing
import com.sasaj.graphics.drawingapp.BuildConfig
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.main.adapter.DrawingsListAdapter
import io.reactivex.disposables.Disposable
import java.io.File


class DrawingsListFragment : Fragment() {

    companion object {
        private const val NUMBER_OF_COLUMNS_PORTRAIT = 3
        private const val NUMBER_OF_COLUMNS_LANDSCAPE = 5
    }

    private lateinit var vm: DrawingListViewModel
    private lateinit var drawingsList: RecyclerView
    private var adapter: DrawingsListAdapter? = null

    private var drawingItemListener: DrawingItemListener = object : DrawingItemListener {
        override fun onItemClicked(clickedItem: DrawingUI) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this)[DrawingListViewModel::class.java]
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

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.drawingsListLiveData.observe(this, Observer { drawingListViewState -> handleViewState(drawingListViewState!!) })

        vm.getDrawings()
    }

    private fun handleViewState(drawingListViewState : DrawingsListViewState) {
        if (drawingListViewState.loading)
//            vmNavigation.loadingData()
        else if (drawingListViewState.isLoaded) {
            adapter?.setDrawings(drawingListViewState.list!!)
        }
    }

    interface DrawingItemListener {
        fun onItemClicked(clickedItem: DrawingUI)
    }
}
