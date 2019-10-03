package com.example.calidata.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.calidata.R;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;
import com.example.calidata.session.SessionManager;

import java.util.HashMap;
import java.util.UUID;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;
    private static final int PERMISSIONS_REQUEST_CAMERA = 555;
    public ManagerTheme managerTheme;
    public SessionManager sessionManager;
    public static final int PICK_IMAGE = 10;
    public static final int EMIT_CODE = 20;

    private static CountDownTimer timer;
    private String token;
    private static Integer TIME_EXPIRED_DEFAULT = 86400;
    private Integer TIME_EXPIRED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        sessionManager = SessionManager.getInstance(getApplicationContext());
        setTheme(managerTheme.getThemeId());
    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidLength(String target) {
        return target.length() >= WORD_LENGTH;
    }

    protected void displayEmptyField(Spinner spinner) {
        if (spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setTextColor(Color.RED);
            errorText.setText(getString(R.string.select_bank_label));
        }
    }

    protected void displayEmptyField(EditText editText) {
        if (editText.getText().toString().isEmpty())
            editText.setError(getString(R.string.required_field_label));
        //return editText.getText().toString().isEmpty();
    }

    protected boolean isEmptyField(EditText editText) {
        return editText.getText().toString().isEmpty();
        //return editText.getText().toString().isEmpty();
    }

    protected boolean comparePassword(EditText password, EditText passwordConfirm) {
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirm.setError(getString(R.string.match_password_title));
            return false;
        } else {
            return true;
        }
    }

    public int getPrimaryColorInTheme() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public int getPrimarySoftColorInTheme() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public void setToolbar(Toolbar toolbar, String title, boolean hasArrow) {
        if (toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setBackgroundColor(getPrimaryColorInTheme());
            setSupportActionBar(toolbar);
        }

        if (hasArrow) {
            setArrowToolbar(toolbar);
        }
    }

    public void setArrowToolbar(Toolbar toolbar) {
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24px);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

    }

    protected void hideItem(View[] view) {
        int gone = View.GONE;
        for (View v : view) {
            v.setVisibility(gone);
        }
    }

    protected void logout() {
        sessionManager.logoutUser();
        setTheme(managerTheme.getFirstTheme());
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("ACEPT");
                    takePhotoFromCamera();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("DENEGADO");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    protected void showPictureDialog(ProgressBar progressbar) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallary();
                            break;
                        case 1:
                            requestCameraPermission();
                            break;
                    }
                });
        pictureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                progressbar.setVisibility(View.GONE);
            }
        });

        pictureDialog.show();
    }

    public void requestCameraPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                System.out.println("PERMISOS CONSEDIDOS");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
                System.out.println("PERMISOS NO CONSEDIDOS");

            }
        } else {
            // Permission has already been granted
            takePhotoFromCamera();

        }
        //*/
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    protected void pickBankAndOpenCheckbookByName(String bankName, String user, Integer userId, String username) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);
        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankName, userId, username);
        startActivity(intent);
    }

    protected void pickBankAndOpenCheckbookByName(String bankName, String user, Integer userId) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);
        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankName, userId);
        startActivity(intent);
    }

    protected void initCountdown() {
        if (timer == null) {
            if (TIME_EXPIRED == null)
                setExpireTime(TIME_EXPIRED_DEFAULT);
            timer = new CountDownTimer(TIME_EXPIRED, 1000) {
                public void onTick(long millisUntilFinished) {
                    //int mod = 60;
                    //if (millisUntilFinished / 1000 % mod == 0) {
                    //Toast.makeText(ParentActivity.this, "tu sesión terminará en: " + millisUntilFinished / 1000, Toast.LENGTH_LONG).show();
                    //}
                }

                public void onFinish() {
                    Toast.makeText(ParentActivity.this, "Sesión Terminada", Toast.LENGTH_LONG).show();
                    logout();
                }
            };
            timer.start();
        }
    }

    public Drawable getLogoDrawableByBankName(String bankName) {

        switch (bankName) {
            case "santander":
                return ContextCompat.getDrawable(this, R.drawable.ic_santander_logo);
            case "banamex":
                return ContextCompat.getDrawable(this, R.drawable.ic_citibanamex_logo);
            case "citibanamex":
                return ContextCompat.getDrawable(this, R.drawable.ic_citibanamex_logo);
            case "hsbc":
                return ContextCompat.getDrawable(this, R.drawable.ic_hsbc_logo);
            case "bancomer":
                return ContextCompat.getDrawable(this, R.drawable.ic_bancomer_logo);
            case "banbajio":
                return ContextCompat.getDrawable(this, R.drawable.ic_banbajio_logo);
            case "scotiabank":
                return ContextCompat.getDrawable(this, R.drawable.ic_scotiabank_logo);
            case "banorte":
                return ContextCompat.getDrawable(this, R.drawable.ic_banorte_logo);
            case "inbursa":
                return ContextCompat.getDrawable(this, R.drawable.ic_inbursa_logo);
            case "compartamos":
                return ContextCompat.getDrawable(this, R.drawable.ic_compartamos_logo);
            default:
                return ContextCompat.getDrawable(this, R.drawable.ic_default_logo);
        }
    }


    protected void setThemeByName(Intent intent) {
        String bankName = intent.getStringExtra("bankName");
        managerTheme = ManagerTheme.getInstance();

        if (bankName != null) {
            managerTheme.setBankName(bankName);
            switch (bankName) {
                case "santander":
                case "hsbc":
                case "scotiabank":
                case "banorte":
                case "autofin":
                case "bansefi":
                    setTheme(R.style.AppThemeRed);
                    managerTheme.setThemeId(R.style.AppThemeRed);
                    break;
                case "citibanamex":
                case "banamex":
                case "bbva bancomer":
                case "famsa":
                case "bancoppel":
                case "monex":
                    setTheme(R.style.AppThemeBlue);
                    managerTheme.setThemeId(R.style.AppThemeBlue);
                    break;
                case "compartamos":
                case "banbajio":
                    setTheme(R.style.AppThemeBanbajio);
                    managerTheme.setThemeId(R.style.AppThemeBanbajio);
                    break;
                case "inbursa":
                case "actinver":
                    setTheme(R.style.AppThemeDarkBlue);
                    managerTheme.setThemeId(R.style.AppThemeDarkBlue);
                    break;
                default:
                    setTheme(R.style.AppThemeOther);
                    managerTheme.setThemeId(R.style.AppThemeOther);
                    break;
            }
        } else {
            Log.e("Error", "bankName is null");
            String bank = managerTheme.getBankName();
            if (bank != null) {
                intent.putExtra("bankName", bank);
                setThemeByName(intent);
            }
        }
    }

    protected void putImage(String image64, ImageView imageView) {
        if (image64 != null) {
            byte[] decodedString = Base64.decode(image64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
            sessionManager.saveProfileImage(image64);

        }
    }


    protected void setExpireTime(Integer expire) {
        this.TIME_EXPIRED = expire * 1000;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    protected String pickStatus(String status) {
        switch (status) {
            case "1":
                return "Solicitado";
            case "2":
                return "Asignado";
            case "3":
                return "Entregado";
            case "4":
                return "Activado";
            case "5":
                return "Liberado";
            case "6":
                return "Pagado";
            case "7":
                return "Cancelado";
            case "8":
                return "Bloqueado";
            default:
                return "Otro Estatus";
        }
    }

    protected CheckModel loadChecks(HashMap<String, Object> item) {
        CheckModel check = new CheckModel();
        String[] date = ((String) item.get("fecha")).split("T");
        String checkId = (String) item.get("iD_CheckID");
        System.out.println("cheque: " + checkId);
        check.setCheckId(checkId);
        check.setDescription((String) item.get("description"));
        check.setQuantity((Double) item.get("monto"));
        check.setDate(date[0]);
        String status = (String) item.get("estatus");

        check.setStatus(pickStatus(status));
        return check;
    }
}
