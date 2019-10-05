package com.example.calidata.utilities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpActivity extends ParentActivity {

    private String title;
    private int fragment;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        title = (String) getIntent().getExtras().get("title");
        fragment = (int) getIntent().getExtras().get("fragment");
        if (title != null) {
            setToolbar(toolbar, title, true);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.list, new SettingFragment(fragment));
/*
        switch (fragment) {
            case 0:
                ft.replace(R.id.list, new TermsFragment());
                break;
            case 1:
                ft.replace(R.id.list, new PrivacyFragment());
                break;
            case 2:
                ft.replace(R.id.list, new ContactFragment());
                break;

        }
        //*/
        ft.commit();
    }


}
