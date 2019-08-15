package com.example.calidata.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.calidata.R;
import com.example.calidata.activities.active.CheckActiveActivity;
import com.example.calidata.activities.cancel.CheckCancelActivity;
import com.example.calidata.activities.emit.CheckEmitActivity;
import com.example.calidata.activities.query.CheckQueryActivity;
import com.example.calidata.management.ManagerTheme;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ParentActivity {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.drawer)
    public DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @BindView(R.id.imageView_query)
    public ImageView imageViewQuery;

    @BindView(R.id.imageView_active)
    public ImageView imageViewActive;

    @BindView(R.id.imageView_emit)
    public ImageView imageViewEmit;

    @BindView(R.id.imageView_cancel)
    public ImageView imageViewCancel;

    @BindView(R.id.textView_active)
    public TextView textViewActive;

    @BindView(R.id.separator2)
    public ConstraintLayout separator2;

    private int primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        imageViewActive.setVisibility(View.GONE);
        textViewActive.setVisibility(View.GONE);
        separator2.setVisibility(View.GONE);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;


        String title = getString(R.string.main_title);
        setToolbar(toolbar, title, false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrwawer, R.string.openDrwawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                /*case R.id.action_favorite:
                    return true;
                case R.id.action_favorite2:
                    return true;
                //*/
                case R.id.action_settings:
                    return true;
                default:
                    return false;
            }
        });

        setImagesListeners();

    }

    private void setImagesListeners() {
        imageViewQuery.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        imageViewEmit.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        imageViewActive.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        imageViewCancel.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        imageViewQuery.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckQueryActivity.class);
            startActivity(intent);
        });


        imageViewEmit.setOnClickListener(v -> {
            //Intent intent = new Intent(this, CheckEmitActivity.class);
            //startActivity(intent);
            openDialog();
        });


        imageViewActive.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckActiveActivity.class);
            startActivity(intent);
        });

        imageViewCancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckCancelActivity.class);
            startActivity(intent);
        });
    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.emit_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        Button scanBtn = view.findViewById(R.id.button_yes);
        scanBtn.setBackgroundColor(getPrimaryColorInTheme());
        scanBtn.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CheckEmitActivity.class);
            intent.putExtra("QR", true);
            startActivity(intent);
            alertDialog.dismiss();
        });

        Button searchBtn = view.findViewById(R.id.button_no);
        searchBtn.setBackgroundColor(getPrimaryColorInTheme());
        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CheckEmitActivity.class);
            intent.putExtra("QR", false);
            startActivity(intent);
            alertDialog.dismiss();

        });
        //*/

        alertDialog.show();

    }
}
