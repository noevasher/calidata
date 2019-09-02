package com.example.calidata.utilities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class SettingsActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.constraintLayout_image)
    public ConstraintLayout constraintImage;

    @BindView(R.id.textView_username)
    public TextView userNameText;

    @BindView(R.id.textView_email)
    public TextView emailText;

    @BindView(R.id.scrollview)
    public ScrollView scrollview;

    @BindView(R.id.imageView_addImage)
    public ImageView addImage;

    @BindView(R.id.imageView_editUserName)
    public ImageView editUserName;

    @BindView(R.id.imageView_profile)
    public de.hdodenhof.circleimageview.CircleImageView imageProfile;

    @BindView(R.id.constraintLayout7_change_password)
    public ConstraintLayout changePasswordPanel;

    @BindView(R.id.constraintLayout_issue)
    public ConstraintLayout issuePanel;

    public static Observable<String> imageObs;
    public static Observable<String> usernameObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Settings", true);

        constraintImage.setBackgroundColor(getPrimarySoftColorInTheme());
        //scrollview.setBackgroundColor(getPrimarySoftColorInTheme());

        String userName = sessionManager.getUserDetails().get("name");
        String email = sessionManager.getUserDetails().get("email");

        userNameText.setText("Noe Vasquez Herrera");
        emailText.setText(email);

        if(sessionManager.getKeyImage64() != null){
            putImage(sessionManager.getKeyImage64(), imageProfile);

        }

        if (sessionManager.getKeyUsername() != null){
            userNameText.setText(sessionManager.getKeyUsername());
        }

        imageObs = Observable.create(emitter -> {
            String image64 = sessionManager.getKeyImage64();
            if (image64 != null) {
                putImage(image64, imageProfile);
                emitter.onNext(image64);
            }
            emitter.onComplete();
        });
        usernameObs = Observable.create(emitter -> {
            String username = sessionManager.getKeyUsername();
            if (username != null){
                userNameText.setText(username);
                emitter.onNext(username);
            }
            emitter.onComplete();
        });
        addImage.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        editUserName.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);

        //addImage.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        //editUserName.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addImage.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                pickFromGallery();
            }
        });

        imageProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                pickFromGallery();
            }
        });

        editUserName.setOnClickListener(v -> {
            changeUsername();
        });


        changePasswordPanel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                openDialogPassword();
            }
        });

    }

    private void changeUsername() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.alert_change_username, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        Button saveBtn = view.findViewById(R.id.button_save);
        saveBtn.setEnabled(false);
        EditText editText = view.findViewById(R.id.editText_username);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText.getText().toString();

                if (text.isEmpty()) {
                    editText.setError("En necesario escribir un nombre de usuario");
                } else {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(SettingsActivity.this.getPrimaryColorInTheme());
                    saveBtn.setOnClickListener(v -> {
                        userNameText.setText(text);
                        sessionManager.saveProfileName(text);
                        alertDialog.dismiss();
                    });
                }
            }
        });


        alertDialog.show();

    }

    private void openDialogPassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.alert_change_password, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        Button saveBtn = view.findViewById(R.id.button_save);
        saveBtn.setEnabled(false);
        EditText editText = view.findViewById(R.id.editText_newPassword);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText.getText().toString();

                if (text.isEmpty()) {
                    editText.setError("En necesario escribir un nombre de usuario");
                } else {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(SettingsActivity.this.getPrimaryColorInTheme());
                    saveBtn.setOnClickListener(v -> {
                        //userNameText.setText(text);
                        alertDialog.dismiss();
                    });
                }
            }
        });


        alertDialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            Log.i("DATA", "data: " + data);
            if (data != null && data.getData() != null) {
                try {
                    imageProfile.setImageURI(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    imageProfile.buildDrawingCache();
                    //Bitmap bmap = imageProfile.getDrawingCache();
                    String encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    sessionManager.saveProfileImage(encodedImageData);
                    //imageProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //Log.i("BASE", encodedImageData);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
        }
    }

    private String convertToBase64(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        return encodedImage;
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
}
