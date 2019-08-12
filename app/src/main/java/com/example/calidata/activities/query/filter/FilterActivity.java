package com.example.calidata.activities.query.filter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calidata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.appyvet.materialrangebar.RangeBar;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;
import com.github.guilhe.views.SeekBarRangedView;

import java.util.Locale;

public class FilterActivity extends ParentActivity {
    private final static int MAX = 500000;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.button_apply)
    public Button applyBtn;


    @BindView(R.id.textView_min)
    public TextView minText;

    @BindView(R.id.textView_max)
    public TextView maxText;

    @BindView(R.id.editText)
    public EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);
        initToolbar();
        SeekBarRangedView rangebar = findViewById(R.id.rangebar1);
        initSeekBar(rangebar);

    }

    private void initSeekBar(SeekBarRangedView rangebar){
        String min = "min: 0";
        String max = "max: " + MAX;
        //editText.setHighlightColor(getPrimaryColorInTheme());
        //editText.setHintTextColor(getPrimaryColorInTheme());
        minText.setText(min);

        //editText.setBackgroundColor(getPrimaryColorInTheme());
        //editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.colorBackgroundSantander), PorterDuff.Mode.SRC_ATOP);

        maxText.setText(max);

        rangebar.setRounded(true);
        rangebar.setProgressColor(getPrimaryColorInTheme());
        rangebar.setMaxValue(MAX);
        rangebar.setMinValue(0);

        rangebar.setOnSeekBarRangedChangeListener(new SeekBarRangedView.OnSeekBarRangedChangeListener() {
            @Override
            public void onChanged(SeekBarRangedView view, float minValue, float maxValue) {
                updateLayout(minValue, maxValue);
            }

            @Override
            public void onChanging(SeekBarRangedView view, float minValue, float maxValue) {
                updateLayout(minValue, maxValue);
            }

            private void updateLayout(float minValue, float maxValue) {
                //binding.activitySeekbarCCurrentTextView.setText(String.format(Locale.getDefault(), "min: %2.0f max: %2.0f", minValue, maxValue));
                //Toast.makeText(FilterActivity.this, "min: " + minValue + " max value: " + maxValue, Toast.LENGTH_LONG).show();
                String min = String.format(Locale.getDefault(), "min: %2.0f", minValue);
                String max = String.format(Locale.getDefault(), "max: %2.0f", maxValue);

                minText.setText(min);
                maxText.setText(max);
            }
        });

    }

    private void printTheme(){
        ManagerTheme managerTheme = ManagerTheme.getInstance();
        int themeId = managerTheme.getThemeId();
        setTheme(themeId);
        toolbar.setBackgroundColor(getPrimaryColorInTheme());
        applyBtn.setBackgroundColor(getPrimaryColorInTheme());

    }
    private void initToolbar(){
        if(toolbar != null){
            toolbar.setTitle(getResources().getString(R.string.filter_title));
            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> {
                Log.i("TAG", "finish filter");
                finish();
            });
            printTheme();

            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            Log.i("TAG", "reset filters");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
