package com.sasaj.graphics.drawingapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.Utilities.Utilities;
import com.sasaj.graphics.drawingapp.data.Drawing;
import com.sasaj.graphics.drawingapp.main.DrawingsListFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DS on 4/22/2017.
 */

public class DrawingsListAdapter extends RecyclerView.Adapter<DrawingsListAdapter.ViewHolder> {

    private final Context context;
    private List<Drawing> data = new ArrayList<>();
    private LayoutInflater inflater;
    private DrawingsListFragment.DrawingItemListener listener;

    public DrawingsListAdapter(Context context, List<Drawing> list, DrawingsListFragment.DrawingItemListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawings_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Drawing drawing = data.get(position);

        Picasso.with(context)
                .load(new File(drawing.getImagePath()))
                .resize(100,100)
                .centerCrop()
                .placeholder(R.drawable.ic_no_image)
                .error(R.drawable.ic_no_image)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(drawing);
            }
        });
    }

    public void setDrawings(List<Drawing> list){
        data = list;
        Collections.sort(data);
        notifyDataSetChanged();
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return data.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.drawing_preview);
        }
    }

    private Bitmap getBitmap(String path) {
        int targetWidth = 100;
        int targetHeight = 100;
        Bitmap bmp = null;

        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmpOptions);
        int currHeight = bmpOptions.outHeight;
        int currWidth = bmpOptions.outWidth;
        int sampleSize = 1;
        if (currHeight > targetHeight || currWidth > targetWidth) {
            if (currWidth > currHeight)
                sampleSize = Math.round((float) currHeight
                        / (float) targetHeight);
            else
                sampleSize = Math.round((float) currWidth
                        / (float) targetWidth);
        }
        bmpOptions.inSampleSize = sampleSize;
        bmpOptions.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, bmpOptions);
        return bmp;
    }
}
