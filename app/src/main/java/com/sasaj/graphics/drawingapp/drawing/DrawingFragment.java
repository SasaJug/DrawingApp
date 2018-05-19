package com.sasaj.graphics.drawingapp.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sasaj.graphics.drawingapp.DrawingApplication;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.drawing.di.PaintComponent;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawingFragment extends Fragment {
    private static final String TAG = DrawingFragment.class.getSimpleName();
    private com.sasaj.graphics.drawingapp.views.layers.DrawingView drawing;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    @Inject
    Paint paint;

    public DrawingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawingFragment.
     */
    public static DrawingFragment newInstance(String param1, String param2) {
        DrawingFragment fragment = new DrawingFragment();
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
            mListener = (OnFragmentInteractionListener) context;
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
            setupInjection();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drawing, container, false);
        drawing = (com.sasaj.graphics.drawingapp.views.layers.DrawingView) root.findViewById(R.id.drawing);
        drawing.setPaint(paint);
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Bitmap getBitmap() {
        return drawing.getBitmapFromView();
    }


    private void setupInjection() {
        Log.e(TAG, "setupInjection: ");
        DrawingApplication app = (DrawingApplication) getActivity().getApplication();
        PaintComponent paintComponent = app.getPaintComponent();
        //presenter = imagesComponent.getPresenter();
        paintComponent.inject(this);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
