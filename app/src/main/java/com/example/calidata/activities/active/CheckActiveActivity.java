package com.example.calidata.activities.active;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckActiveActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_active);
        ButterKnife.bind(this);
        ManagerTheme managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setToolbar();
    }

    private void setToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.checkbook_active_title));
            toolbar.setBackgroundColor(getPrimaryColorInTheme());
            setSupportActionBar(toolbar);

            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> {
                Log.i("TAG", "finish Active");
                finish();
            });
        }
    }

}
