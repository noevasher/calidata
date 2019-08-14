package com.example.calidata.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.calidata.R;
import com.example.calidata.activities.active.CheckActiveActivity;
import com.example.calidata.activities.emit.CheckEmitActivity;
import com.example.calidata.activities.query.CheckQueryActivity;
import com.example.calidata.login.managmentLogin.AESCrypt;
import com.example.calidata.main.ParentActivity;
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

    private int primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int positionBank = intent.getIntExtra("bank", 4);
        ManagerTheme managerTheme = ManagerTheme.getInstance();

        switch (positionBank){
            case 0:
                setTheme(R.style.AppTheme);
                setThemeResId(R.style.AppTheme);
                managerTheme.setThemeId(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppThemeBanamex);
                setThemeResId(R.style.AppThemeBanamex);
                managerTheme.setThemeId(R.style.AppThemeBanamex);
                break;
            case 2:
                setTheme(R.style.AppThemeSantander);
                setThemeResId(R.style.AppThemeSantander);
                managerTheme.setThemeId(R.style.AppThemeSantander);
                break;
            case 3:
                setTheme(R.style.AppThemeBancomer);
                setThemeResId(R.style.AppThemeBancomer);
                managerTheme.setThemeId(R.style.AppThemeBancomer);
                break;
            case 4:
                setTheme(R.style.AppThemeOther);
                setThemeResId(R.style.AppThemeOther);
                managerTheme.setThemeId(R.style.AppThemeOther);
                break;
        }


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;


        if(toolbar != null){

            toolbar.setTitle(getResources().getString(R.string.main_title));
            toolbar.setBackgroundColor(primaryColor);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrwawer, R.string.openDrwawer){

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
            switch (menuItem.getItemId()){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setImagesListeners(){
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

    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.emit_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        Button scanBtn = view.findViewById(R.id.button_scan);
        scanBtn.setBackgroundColor(getPrimaryColorInTheme());
        scanBtn.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), CheckEmitActivity.class);
            intent.putExtra("QR", true);
            startActivity(intent);
            alertDialog.dismiss();
        });

        Button searchBtn = view.findViewById(R.id.button_search);
        searchBtn.setBackgroundColor(getPrimaryColorInTheme());
        searchBtn.setOnClickListener(v->{
            Intent intent = new Intent(v.getContext(), CheckEmitActivity.class);
            intent.putExtra("QR", false);
            startActivity(intent);
            alertDialog.dismiss();

        });
        //*/

        alertDialog.show();

    }
}
