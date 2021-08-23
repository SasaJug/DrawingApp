package com.sasaj.graphics.drawingapp.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.sasaj.graphics.drawingapp.BuildConfig
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.main.adapter.DrawingsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DrawingsListFragment : androidx.fragment.app.Fragment() {

    companion object {
        private const val NUMBER_OF_COLUMNS_PORTRAIT = 3
        private const val NUMBER_OF_COLUMNS_LANDSCAPE = 5
    }

    private val vm by activityViewModels<DrawingListNavigationViewModel>()

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

//        vm = activity?.let { ViewModelProviders.of(it)[DrawingListNavigationViewModel::class.java] }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_drawings_list, container, false)
        drawingsList = root.findViewById(R.id.drawings_grid)

        val orientation = requireActivity().resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            drawingsList.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(
                    activity,
                    NUMBER_OF_COLUMNS_PORTRAIT
                )
        } else {
            drawingsList.layoutManager =
                androidx.recyclerview.widget.GridLayoutManager(
                    activity,
                    NUMBER_OF_COLUMNS_LANDSCAPE
                )
        }

        adapter = DrawingsListAdapter(requireActivity(), ArrayList(0), drawingItemListener)
        drawingsList.adapter = adapter

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm?.drawingsListLiveData?.observe(viewLifecycleOwner, Observer { drawingListNavigationViewState -> handleViewState(drawingListNavigationViewState!!) })
    }

    private fun handleViewState(drawingsListNavigationViewState: DrawingsListNavigationViewState) {
        if (drawingsListNavigationViewState.list != null){
            adapter?.setDrawings(drawingsListNavigationViewState.list!!)
        }
    }

    interface DrawingItemListener {
        fun onItemClicked(clickedItem: DrawingUI)
    }
}
