package com.sasaj.graphics.drawingapp.ui.drawing;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation;
import com.sasaj.graphics.drawingapp.ui.views.CustomActionBar;
import com.sasaj.graphics.drawingapp.ui.fragments.SelectPaintDialogFragment;

public class DrawingActivity extends AppCompatActivity implements DrawingFragment.OnFragmentInteractionListener, DrawingContract.View {

    public static final String ORIENTATION = "ORIENTATION";
    public static final int LANDSCAPE = 0;
    public static final int PORTRAIT = 1;

    private DrawingFragment drawingFragment;
    private DrawingPresenter actionsListener;
    private ProgressBar progress;

    public static Intent createIntent(Context context, int orientation) {
        Intent intent = new Intent(context, DrawingActivity.class);
        intent.putExtra(ORIENTATION, orientation);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreenOrientation(getIntent().getIntExtra(ORIENTATION, PORTRAIT));

        setContentView(R.layout.activity_drawing);
        progress = (ProgressBar) findViewById(R.id.progress);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        CustomActionBar customActionBar = new com.sasaj.graphics.drawingapp.ui.views.CustomActionBar(this);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        customActionBar.setLayoutParams(params);
        getSupportActionBar().setCustomView(customActionBar);
        customActionBar.setDelegate(mDelegate);
        Toolbar parent = (Toolbar) customActionBar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        actionsListener = new DrawingPresenter(this, DrawingsRepositoryImplementation.getInstance());


        drawingFragment = (DrawingFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (drawingFragment == null) {
            // Create the fragment
            drawingFragment = DrawingFragment.newInstance(null, null);
            initFragment(drawingFragment);
        }
    }


    private void initFragment(Fragment drawingFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, drawingFragment);
        transaction.commit();
    }

    public void saveImageToMemory(Bitmap bitmap) {
        actionsListener.saveDrawing(bitmap);
    }

    CustomActionBar.Delegate mDelegate = new CustomActionBar.Delegate() {

        @Override
        public void startToolsDialog() {
            DialogFragment newFragment = SelectPaintDialogFragment.newInstance(new Bundle());
            newFragment.show(getFragmentManager(), "com.sasaj.graphics.drawingapp.dialog");
        }


        @Override
        public void startSaveDialog() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DrawingActivity.this);

            // set title
            alertDialogBuilder.setTitle(R.string.app_title);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.save_mage_question)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (drawingFragment != null) {
                                saveImageToMemory(drawingFragment.getBitmap());
                            }
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    };

    private void setScreenOrientation(int orientation) {
        if (orientation == PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

