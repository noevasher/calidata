package com.example.calidata.utilities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.media.MediaRecorder.VideoSource.CAMERA;

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

    @BindView(R.id.constraintLayout_terms)
    public ConstraintLayout changePasswordPanel;

    @BindView(R.id.constraintLayout_issue)
    public ConstraintLayout issuePanel;

    @BindView(R.id.imageView_close)
    public ImageView close;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.imageView_report)
    public ImageView report;

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

        if (sessionManager.getKeyImage64() != null) {
            putImage(sessionManager.getKeyImage64(), imageProfile);

        }

        if (sessionManager.getKeyUsername() != null) {
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
            if (username != null) {
                userNameText.setText(username);
                emitter.onNext(username);
            }
            emitter.onComplete();
        });
        addImage.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        editUserName.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        close.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        report.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        //addImage.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        //editUserName.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addImage.setOnClickListener(imageListener);

        imageProfile.setOnClickListener(imageListener);

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

    private OnSingleClickListener imageListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            showPictureDialog(progressBar);
        }
    };

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
                    editText.setError(getString(R.string.error_empty_username));
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    imageProfile.setImageURI(data.getData());
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    bitmap = compressImage(bitmap);
                    imageProfile.buildDrawingCache();
                    String encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                    sessionManager.saveProfileImage(encodedImageData);
                    progressBar.setVisibility(View.GONE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Fallo al cargar Imagen!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageProfile.setImageBitmap(thumbnail);
            imageProfile.buildDrawingCache();
            String encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);
            sessionManager.saveProfileImage(encodedImageData);
            progressBar.setVisibility(View.GONE);
            //saveImage(thumbnail);
        }


    }

    private Bitmap compressImage(Bitmap original) {
        int ONE_MB = 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap resized = Bitmap.createScaledBitmap(original, (int) (original.getWidth() * 0.8), (int) (original.getHeight() * 0.8), true);

        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int realSize = baos.toByteArray().length;
        System.out.println("realSize: " + realSize);

        while (realSize / ONE_MB > ONE_MB) {
            baos.reset();
            resized = Bitmap.createScaledBitmap(resized, (int) (resized.getWidth() * 0.8), (int) (resized.getHeight() * 0.8), true);
            resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            realSize = baos.toByteArray().length;
            System.out.println("realSize: " + realSize);

        }

        return resized;
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
