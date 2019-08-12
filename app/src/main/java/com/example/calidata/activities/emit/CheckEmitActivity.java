package com.example.calidata.activities.emit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.calidata.R;
import com.example.calidata.activities.emit.adapters.PagerAdapter;
import com.example.calidata.activities.emit.fragments.FragmentSearch;
import com.example.calidata.activities.emit.fragments.FragmentQR;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckEmitActivity extends AppCompatActivity  {
    private ZXingScannerView mScannerView;

    //@BindView(R.id.button_scann)
    //public Button scannBtn;

    //@BindView(R.id.textView_content)
    //public TextView contentText;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    private FragmentSearch fragmentSearch;
    private FragmentQR fragmentQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);

        initPaging();

        Intent intent = getIntent();
        boolean isQR = intent.getBooleanExtra("QR", false);
        if(isQR){
            //readQR();
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);

        }
        //*/

    }

    private void initPaging() {

        fragmentSearch = new FragmentSearch(this);
        fragmentQR = new FragmentQR(this);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(fragmentSearch);
        pagerAdapter.addFragment(fragmentQR);

        fragmentSearch = (FragmentSearch) pagerAdapter.getItem(0);
        fragmentQR = (FragmentQR) pagerAdapter.getItem(1);

        viewPager.setAdapter(pagerAdapter);
    }

    private void readQR(){
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
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
            if(resultCode == RESULT_CANCELED){
                //handle cancel
                Log.i("TAG-QR", "CANCELADO");
            }
        }
    }


}
