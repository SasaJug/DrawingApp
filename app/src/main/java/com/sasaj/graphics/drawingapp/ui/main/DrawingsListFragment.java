package com.sasaj.graphics.drawingapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasaj.graphics.drawingapp.BuildConfig;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.ui.main.adapter.DrawingsListAdapter;
import com.sasaj.graphics.drawingapp.domain.Drawing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawingsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawingsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawingsListFragment extends Fragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUMBER_OF_COLUMNS_PORTRAIT = 3;
    private static final int NUMBER_OF_COLUMNS_LANDSCAPE = 5;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener listener;
    private RecyclerView drawingsList;



    DrawingItemListener drawingItemListener = new DrawingItemListener() {
        @Override
        public void onItemClicked(Drawing clickedItem) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + clickedItem.getImagePath()), "image/*");
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri photoUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",
                        new File(clickedItem.getImagePath()));
                intent.setData(photoUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        }
    };
    private DrawingsListAdapter adapter;

    public DrawingsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawingsListFragment.
     */
    public static DrawingsListFragment newInstance(String param1, String param2) {
        DrawingsListFragment fragment = new DrawingsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
           listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_drawings_list, container, false);

        drawingsList = (RecyclerView) root.findViewById(R.id.drawings_grid);

        int orientation = getActivity().getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT){
            drawingsList.setLayoutManager(new GridLayoutManager(getActivity(), NUMBER_OF_COLUMNS_PORTRAIT));
        }
        else{
            drawingsList.setLayoutManager(new GridLayoutManager(getActivity(), NUMBER_OF_COLUMNS_LANDSCAPE));
        }

        adapter = new DrawingsListAdapter(getActivity(), new ArrayList<Drawing>(0), drawingItemListener);
        drawingsList.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(listener != null){
           listener.getDrawings();
        }
    }

    public void setDrawingsList(List<Drawing>list){
        if (adapter != null) {
            adapter.setDrawings(list);
        }
    }

    public interface OnFragmentInteractionListener {
        void getDrawings();
    }

    public interface DrawingItemListener {
        void onItemClicked(Drawing clickedItem);
    }
}
