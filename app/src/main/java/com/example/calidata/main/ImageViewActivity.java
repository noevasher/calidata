package com.example.calidata.main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.calidata.R;
import com.example.calidata.utilities.controllers.UserController;

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

    public Integer userId;

    @Override
    protected void onResume() {
        super.onResume();
        validLocationPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        String image64 = sessionManager.getKeyImage64();
        userId = sessionManager.getUserId();

        if (image64 == null) {
            UserController controller = new UserController(this);
            controller.getUserInformation(userId).subscribe(response -> {
                String image64Response = response.getImage64();
                putImage(image64Response, profile);

            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            putImage(image64, profile);
        }
        close.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        //profile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_circle_24px));
    }
}
