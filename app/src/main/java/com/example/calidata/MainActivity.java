package com.example.calidata;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.calidata.activities.CheckQuery.CheckQueryActivity;
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
                //setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
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

        ImageView image1 = findViewById(R.id.imageView);
        ImageView image2 = findViewById(R.id.imageView2);
        ImageView image3 = findViewById(R.id.imageView3);
        ImageView image4 = findViewById(R.id.imageView4);


        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;

        String themeName = getLogoDrawable();
        image1.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        image2.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        image3.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);
        image4.setColorFilter(primaryColor, PorterDuff.Mode.SRC_IN);


        if(toolbar != null){

            toolbar.setTitle(getResources().getString(R.string.main_title));
            toolbar.setBackgroundColor(primaryColor);
            toolbar.setSubtitle("....");

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

        drawerLayout.setDrawerListener(toggle);
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

        image1.setOnClickListener(v -> {
            Intent intentQ = new Intent(this, CheckQueryActivity.class);
            startActivity(intentQ);
        });
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
}
