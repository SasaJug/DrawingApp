package com.sasaj.graphics.drawingapp.ui.main;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.sasaj.graphics.drawingapp.R;
import com.sasaj.graphics.drawingapp.domain.Drawing;
import com.sasaj.graphics.drawingapp.repository.DrawingsRepositoryImplementation;
import com.sasaj.graphics.drawingapp.ui.drawing.DrawingActivity;

import java.util.List;

public class DrawingListActivity extends AppCompatActivity implements DrawingsListFragment.OnFragmentInteractionListener, DrawingsListContract.View {

    private static final String TAG = DrawingListActivity.class.getSimpleName();

    private DrawingsListPresenter actionsListener;
    private DrawingsListFragment drawingsListFragment;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionsListener = new DrawingsListPresenter(this, DrawingsRepositoryImplementation.getInstance());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.app_title);
        }

        setFabButton();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        drawingsListFragment = (DrawingsListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (drawingsListFragment == null) {
            // Create the fragment
            drawingsListFragment = DrawingsListFragment.newInstance(null, null);
            initFragment(drawingsListFragment);
        }

    }

    private void initFragment(Fragment drawingsListFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, drawingsListFragment);
        transaction.commit();
    }

    private void setFabButton() {

        final ImageView icon = new ImageView(this);
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_bg))
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.portrait_icon));
        itemIcon1.setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_item_bg));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DrawingActivity.createIntent(DrawingListActivity.this, DrawingActivity.PORTRAIT);
                startActivity(intent);
            }
        });

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.landscape_icon));
        itemIcon2.setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_item_bg));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DrawingActivity.createIntent(DrawingListActivity.this, DrawingActivity.LANDSCAPE);
                startActivity(intent);
            }
        });


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button2)
                .addSubActionView(button1)
                .attachTo(actionButton)
                .build();

        // Listen menu open and close events to animate the button content view
        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                icon.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(icon, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                icon.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(icon, pvhR);
                animation.start();
            }
        });

    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setDrawingsData(List<Drawing> drawings) {
        drawingsListFragment.setDrawingsList(drawings);
    }

    @Override
    public void getDrawings() {
        actionsListener.getDrawings();
    }
}
