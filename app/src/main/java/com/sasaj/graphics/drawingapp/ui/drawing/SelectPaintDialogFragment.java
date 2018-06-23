package com.sasaj.graphics.drawingapp.ui.drawing;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sasaj.graphics.drawingapp.R;

/**
 * Created by User on 9/11/2016.
 */
public class SelectPaintDialogFragment extends DialogFragment {

    private static final String TAG = SelectPaintDialogFragment.class.getSimpleName();


    public static SelectPaintDialogFragment newInstance(int index) {

        SelectPaintDialogFragment df = new SelectPaintDialogFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        df.setArguments(args);
        return df;
    }

    public static SelectPaintDialogFragment newInstance(Bundle bundle) {
        int index = bundle.getInt("index", 0);
        return newInstance(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.select_paint_dialog_fragment_layout, container, false);

        return v;
    }
}
