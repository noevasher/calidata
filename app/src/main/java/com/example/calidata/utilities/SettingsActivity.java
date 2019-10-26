package com.example.calidata.utilities;

import android.annotation.SuppressLint;
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
import com.example.calidata.login.managment.AESCrypt;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.utilities.controllers.UserController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import static android.media.MediaRecorder.VideoSource.CAMERA;

@SuppressLint("CheckResult")
public class SettingsActivity extends ParentActivity {

    private String oldPassword;
    private String newPassword;
    private String token;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.constraintLayout_image)
    public ConstraintLayout constraintImage;

    @BindView(R.id.textView_username)
    public TextView userNameText;

    @BindView(R.id.textView_phone)
    public TextView phoneText;


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
    private UserController userController;
    private String phone;
    private String phoneString = "Telefono: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setToolbar(toolbar, "Settings", true);
        userController = new UserController(this);
        constraintImage.setBackgroundColor(getPrimarySoftColorInTheme());
        //scrollview.setBackgroundColor(getPrimarySoftColorInTheme());

        String userName = sessionManager.getUserDetails().get("name");
        String email = sessionManager.getUserDetails().get("email");
        phone = sessionManager.getUserDetails().get("phone");


        userNameText.setText(userName);
        emailText.setText(email);
        if (phone == null || phone.isEmpty()) {
            phoneText.setVisibility(View.GONE);
        } else {
            phoneText.setText(phoneString + sessionManager.getKeyPhone());
        }

        if (sessionManager.getKeyImage64() != null) {
            putImage(sessionManager.getKeyImage64(), imageProfile);
        } else {
            UserController controller = new UserController(this);
            controller.getUserInformation(sessionManager.getUserId()).subscribe(response -> {
                String image64Response = response.getImage64();
                putImage(image64Response, imageProfile);

            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        }

        //if (sessionManager.getKeyUsername() != null) {
          //  userNameText.setText(sessionManager.getKeyUsername());
        //} else {
            UserController controller = new UserController(this);
            controller.getUserInformation(sessionManager.getUserId()).subscribe(response -> {
                String username = response.getUserName();
                phone = response.getPhone();

                userNameText.setText(username);
                phoneText.setVisibility(View.VISIBLE);
                phoneText.setText(phoneString + phone);
                sessionManager.saveProfileName(username);
                sessionManager.savePhone(phone.trim());
            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });

//        }

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
            showPictureDialog(progressBar, true);
        }
    };

    private boolean activeBtn;

    private void changeUsername() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.alert_change_username, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        Button saveBtn = view.findViewById(R.id.button_save);
        saveBtn.setEnabled(false);
        EditText usernameText = view.findViewById(R.id.editText_username);
        EditText phoneText = view.findViewById(R.id.edittext_phone);
        usernameText.setText(sessionManager.getKeyUsername());
        phoneText.setText(sessionManager.getKeyPhone());
        //phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        saveBtn.setEnabled(true);
        saveBtn.setBackgroundColor(SettingsActivity.this.getPrimaryColorInTheme());

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String username = usernameText.getText().toString();
                if (username.isEmpty()) {
                    usernameText.setError("En necesario escribir un nombre de usuario");
                    activeBtn = false;
                } else {
                    activeBtn = true;
                }
            }
        });

        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = phoneText.getText().toString();
                if (phone.isEmpty()) {
                    phoneText.setError("En necesario escribir un nombre de usuario");
                    activeBtn = false;
                } else {
                    activeBtn = true;
                }
            }
        });

        saveBtn.setOnClickListener(v -> {
            if (activeBtn) {
                String username = usernameText.getText().toString();
                String phone = phoneText.getText().toString();

                loadToSave(username, phone.trim(), null);
            } else {
                //listeners------
            }
            alertDialog.dismiss();
        });

        alertDialog.show();

    }

    private void loadToSave(String username, String phone, String image64) {
        token = sessionManager.getToken();
        if (token != null) {
            HashMap<String, Object> body = new HashMap<>();
            body.put("ID_Usuario", sessionManager.getUserId());
            if (username != null && !username.isEmpty())
                body.put("Usuario", username);
            if (phone != null && !phone.isEmpty())
                body.put("celular", phone);
            if (image64 != null && !image64.isEmpty())
                body.put("image64", image64);

            userController.saveProfile(body).subscribe(response -> {
                if (response.isSuccess()) {
                    if (username != null && !username.isEmpty()) {
                        userNameText.setText(username);
                        sessionManager.saveProfileName(username);
                    }
                    if (phone != null && !phone.isEmpty()) {
                        phoneText.setVisibility(View.VISIBLE);
                        phoneText.setText(phoneString + phone.trim());
                        sessionManager.savePhone(phone.trim());
                    }
                    if (image64 != null && !image64.isEmpty()) {
                        sessionManager.saveProfileImage(image64);
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(this, "Respuesta invalida", Toast.LENGTH_LONG).show();
                }
            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        } else {
            Toast.makeText(this, R.string.error_fail_login, Toast.LENGTH_LONG).show();
            logout();
        }
    }

    private boolean newActive;
    private boolean oldActive;

    @Override
    protected void onResume() {
        super.onResume();
        validLocationPermission();
    }

    private void openDialogPassword() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.alert_change_password, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        Button saveBtn = view.findViewById(R.id.button_save);
        saveBtn.setEnabled(false);
        EditText newPasswordText = view.findViewById(R.id.editText_newPassword);
        EditText oldPasswordText = view.findViewById(R.id.editText_oldPassword);

        newPasswordText.setOnFocusChangeListener((textView, hasFocus) -> {
            if (!hasFocus) {
                if (newPasswordText.getText().toString().isEmpty()) {
                    newPasswordText.setError(getString(R.string.required_field_label));
                    newActive = false;

                } else {
                    newActive = true;
                }
            }
        });

        oldPasswordText.setOnFocusChangeListener((textView, hasFocus) -> {
            if (!hasFocus) {
                if (oldPasswordText.getText().toString().isEmpty()) {
                    oldPasswordText.setError(getString(R.string.required_field_label));
                    oldActive = false;
                } else {
                    oldActive = true;
                }
            }
        });


        newPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = newPasswordText.getText().toString();

                if (text.isEmpty()) {
                    newPasswordText.setError(getString(R.string.error_empty_username));
                } else {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(SettingsActivity.this.getPrimaryColorInTheme());

                }
            }
        });

        saveBtn.setOnClickListener(v -> {
            //if (newActive && oldActive) {
            newPassword = newPasswordText.getText().toString();
            oldPassword = oldPasswordText.getText().toString();
            try {
                newPassword = AESCrypt.encrypt(newPasswordText.getText().toString());
                newPassword = newPassword
                        .replace("\n", "")
                        .replace("\r", "");
                oldPassword = AESCrypt.encrypt(oldPassword);
                oldPassword = oldPassword
                        .replace("\n", "")
                        .replace("\r", "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, Object> body = new HashMap<>();
            body.put("ID_Usuario", sessionManager.getUserId());
            body.put("newPassword", newPassword);
            body.put("oldPassword", oldPassword);

            userController.changePassword(token, body).subscribe(response -> {
                String message = response.getMessage();
                if (message.equals("OK")) {
                    Toast.makeText(SettingsActivity.this, R.string.success_change_password, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }, t -> {
                Toast.makeText(SettingsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            });
            alertDialog.dismiss();
            //}
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
                    loadToSave(null, null, encodedImageData);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Fallo al cargar Imagen!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            thumbnail = compressImage(thumbnail);

            imageProfile.setImageBitmap(thumbnail);
            imageProfile.buildDrawingCache();
            String encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);
            loadToSave(null, null, encodedImageData);
            //saveImage(thumbnail);
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


}
