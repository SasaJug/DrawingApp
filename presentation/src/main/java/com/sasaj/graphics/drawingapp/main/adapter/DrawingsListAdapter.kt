package com.sasaj.graphics.drawingapp.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sasaj.domain.entities.Drawing
import com.sasaj.graphics.drawingapp.R
import com.sasaj.graphics.drawingapp.entities.DrawingUI
import com.sasaj.graphics.drawingapp.main.DrawingsListFragment
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

/**
 * Created by sjugurdzija on 4/22/2017.
 */

class DrawingsListAdapter(context: Context, list: List<DrawingUI>, private val listener: DrawingsListFragment.DrawingItemListener) : RecyclerView.Adapter<DrawingsListAdapter.ViewHolder>() {
    private var data: MutableList<DrawingUI> = ArrayList()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    init {
        this.data = list.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.drawings_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val drawing = data[position]

        Picasso.get()
                .load(File(drawing.imagePath))
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.ic_no_image)
                .error(R.drawable.ic_no_image)
                .into(holder.imageView)

        holder.imageView.setOnClickListener { listener.onItemClicked(drawing) }
    }

    fun setDrawings(list: List<DrawingUI>) {
        data = list.toMutableList()
        data.sort()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<View>(R.id.drawing_preview) as ImageView
    }

}