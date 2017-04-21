package com.sasaj.graphics.drawingapp.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.data.Drawing;
import com.sasaj.graphics.drawingapp.data.source.DrawingsRepositoryImplementation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawingsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawingsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawingsListFragment extends Fragment implements DrawingsListContract.View {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DrawingsListPresenter actionsListener;
    private RecyclerView drawingsList;
    private ProgressBar progressBar;

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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_drawings_list, container, false);

        drawingsList = (RecyclerView)root.findViewById(R.id.drawings_grid);
        progressBar = (ProgressBar)root.findViewById(R.id.progress_bar);

        actionsListener = new DrawingsListPresenter(this, new DrawingsRepositoryImplementation(getActivity()));
//
//        reportsList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_drawable);
//        RecyclerView.ItemDecoration dividerItemDecoration = new CustomDivider(dividerDrawable);
//        reportsList.addItemDecoration(dividerItemDecoration);
//        adapter = new HistoryAdapter(getActivity(), new ArrayList<ReportModel>(0), mHistoryItemListener);
//        reportsList.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setDrawingsData(List<Drawing> drawings) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
