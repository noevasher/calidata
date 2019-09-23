package com.example.calidata.utilities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Ayuda", true);
    }
}
