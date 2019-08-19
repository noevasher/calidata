package com.example.calidata.utilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Settings", true);

    }
}
