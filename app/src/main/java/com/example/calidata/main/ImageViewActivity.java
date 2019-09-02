package com.example.calidata.main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.calidata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewActivity extends ParentActivity {

    @BindView(R.id.imageView_profile)
    public ImageView profile;

    @BindView(R.id.imageView_close)
    public ImageView close;

    @OnClick(R.id.imageView_close)
    public void close() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        String image64 = sessionManager.getKeyImage64();
        putImage(image64, profile);
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        //profile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_circle_24px));
    }
}
