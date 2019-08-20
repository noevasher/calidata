package com.example.calidata.activities.emit;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.calidata.R;
import com.example.calidata.activities.emit.adapters.PagerAdapter;
import com.example.calidata.activities.emit.fragments.FragmentQR;
import com.example.calidata.activities.emit.fragments.FragmentSearch;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckEmitActivity extends ParentActivity {
    private ZXingScannerView mScannerView;

    //@BindView(R.id.button_scann)
    //public Button scannBtn;

    //@BindView(R.id.textView_content)
    //public TextView contentText;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    private FragmentSearch fragmentSearch;
    private FragmentQR fragmentQR;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        boolean isQR = intent.getBooleanExtra("QR", false);
        initPaging(isQR);
        setToolbar(toolbar, getResources().getString(R.string.emit_title), true);

        //initToolbar();
    }

    private void initPaging(boolean isQR) {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if(isQR) {
            fragmentQR = new FragmentQR(this);
            pagerAdapter.addFragment(fragmentQR);

        }else{
            fragmentSearch = new FragmentSearch(this);
            pagerAdapter.addFragment(fragmentSearch);
        }
        viewPager.setAdapter(pagerAdapter);
    }

}
