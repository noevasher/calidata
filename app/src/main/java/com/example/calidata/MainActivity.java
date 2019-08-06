package com.example.calidata;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private int primaryColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeSantander);
        Intent intent = getIntent();
        int positionBank = intent.getIntExtra("bank", 4);

        switch (positionBank){
            case 0:
                break;
            case 1:
                setTheme(R.style.AppThemeBanamex);
                break;
            case 2:
                setTheme(R.style.AppThemeSantander);
                break;
            case 3:
                //setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
                setTheme(R.style.AppThemeBancomer);
                break;
            case 4:
                setTheme(R.style.AppThemeOther);
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

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
