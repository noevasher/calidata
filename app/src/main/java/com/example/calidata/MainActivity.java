package com.example.calidata;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.check.CheckActivity;
import com.example.calidata.main.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private int primaryColor;
    //String[] bank = { "Banamex", "Santander", "Bancomer", "Otro"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sessionManager.checkLogin();
        Intent intent = getIntent();
        String message = intent.getStringExtra("tema");
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
                setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
                //setTheme(R.style.AppThemeBancomer);
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

        image1.setOnClickListener(v->{
            Intent intentCheck = new Intent(MainActivity.this, CheckActivity.class);
            startActivity(intentCheck);
        });
        initToolbar();
    }


    private void initToolbar(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setTitle("Actividad");
            toolbar.setBackgroundColor(primaryColor);
            //final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24px);
            //upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            //toolbar.setNavigationIcon(upArrow);
            //toolbar.setNavigationOnClickListener(view -> finish());
            //getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
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
