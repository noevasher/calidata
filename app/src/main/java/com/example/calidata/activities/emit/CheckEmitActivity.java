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
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        boolean isQR = intent.getBooleanExtra("QR", false);
        initPaging(isQR);
        initToolbar();
        /*
        if (isQR) {
            //readQR();
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);

        }

        //*/

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

    private void readQR() {
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");


                //TextView textView = fragmentQR.getView().findViewById(R.id.textView_content);
                //textView.setText(contents + "\n" + format);

                Log.i("TAG-QR contents: ", contents);
                Log.i("TAG-QR format: ", format);
                //Log.i("TAG-QR data sqcheme", data.getData().getScheme());

            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                Log.i("TAG-QR", "CANCELADO");
            }
        }
    }


    private void initToolbar(){
        if(toolbar != null){

            ManagerTheme managerTheme = ManagerTheme.getInstance();
            int themeId = managerTheme.getThemeId();
            setTheme(themeId);

            toolbar.setTitle(getResources().getString(R.string.emit_title));
            toolbar.setBackgroundColor(getPrimaryColorInTheme());
            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> {
                Log.i("TAG", "finish filter");
                finish();
            });

        }

    }

}
