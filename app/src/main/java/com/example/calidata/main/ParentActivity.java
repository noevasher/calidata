package com.example.calidata.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class ParentActivity extends AppCompatActivity {
    public static ParentActivity parentActivity;

    private static final int WORD_LENGTH = 6;
    private static final int PERMISSIONS_REQUEST_CAMERA = 555;
    private static final int PERMISSIONS_LOCATION_CODE_CHECKBOOK = 1111;
    public ManagerTheme managerTheme;
    public SessionManager sessionManager;
    protected int myTheme;
    protected String bankName;
    public static final int PICK_IMAGE = 10;
    public static final int EMIT_CODE = 20;
    public static final int CANCEL_CODE = 30;

    private static CountDownTimer timer;
    private Integer TIME_EXPIRED;
    protected HashMap<String, Object> bankIds = new HashMap<>();

    protected FusedLocationProviderClient mFusedLocationClient;
    protected static double wayLatitude = 0.0, wayLongitude = 0.0;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("theme", myTheme);
        savedInstanceState.putString("bank", bankName);
        System.out.println("save instance: " + myTheme + " " + bankName);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        myTheme = savedInstanceState.getInt("theme");
        bankName = savedInstanceState.getString("bank");
        System.out.println("restore instance: " + myTheme + " " + bankName);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        managerTheme = ManagerTheme.getInstance();
        sessionManager = SessionManager.getInstance(getApplicationContext());
        myTheme = managerTheme.getThemeId();
        bankName = sessionManager.getBankName();
        System.out.println("onCreate Parent instance: " + + myTheme + " " + bankName);
        setThemeByName(bankName);

        super.onCreate(savedInstanceState);
        //Necessary to getIP()
        StrictMode.enableDefaults();

        parentActivity = this;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /*
        if(managerTheme.getThemeId() == 0)
            setTheme(sessionManager.getTheme());
        else
            setTheme(managerTheme.getThemeId());

        //*/
        myTheme = sessionManager.getTheme();
        setTheme(sessionManager.getTheme());
        bankName = sessionManager.getBankName();
        System.out.println("TEMA " + myTheme);

    }

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPhone(String target) {
        return target.length() == 10;
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

    public void logout() {
        sessionManager.logoutUser();
        setTheme(managerTheme.getFirstTheme());
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume Parent");

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
            case PERMISSIONS_LOCATION_CODE_CHECKBOOK: {
                // If request is cancelled, the result arrays are empty.
                logout();
                /*
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
                //*/

            }
        }
    }

    public void saveLocationData() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                setWayLatitude(location.getLatitude());
                setWayLongitude(location.getLongitude());
            }
        });


    }

    protected void showPictureDialog(ProgressBar progressbar, boolean activeGallery) {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Seleciona Opción");
        String[] pictureDialogItems;
        String gallery = "Selecciona Imagen de tu Galeria";
        String camera = "Captura Foto de Camara";
        if (activeGallery) {
            pictureDialogItems = new String[]{gallery, camera};
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
        } else {
            pictureDialogItems = new String[]{camera};
            pictureDialog.setItems(pictureDialogItems,
                    (dialog, which) -> {
                        if (which == 0) {
                            requestCameraPermission();
                        }
                    });
        }

        pictureDialog.setOnDismissListener(dialogInterface -> {
            if (progressbar != null)
                progressbar.setVisibility(View.GONE);
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

    protected void pickBankAndOpenCheckbookByName(String bankName, Integer bankId, String user,
                                                  Integer userId, String username, String phone) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);

        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankId, bankName, userId, username, phone);
        startActivity(intent);
    }

    protected void pickBankAndOpenCheckbookByName(String bankName, Integer bankId, String
            user, Integer userId) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);
        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankId, bankName, userId);
        startActivity(intent);
    }

    protected void initCountdown() {
        if (timer == null) {
            Integer TIME_EXPIRED_DEFAULT = 86400;
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
        Log.i("PARENT", "bankName: " + bankName);
        if (bankName != null) {
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
        } else {
            Log.e("Error", "bankName is null");
            String bank = sessionManager.getBankName();
            if (bank != null) {
                Log.e("Error", "bankName is " + bank);
                return getLogoDrawableByBankName(bank);
            } else
                return null;
            //return  ContextCompat.getDrawable(this, R.drawable.ic_default_logo);
        }
    }

    protected void setThemeByName(String bankName) {
        managerTheme = ManagerTheme.getInstance();

        if (bankName != null && !bankName.isEmpty()) {
            bankName = bankName.toLowerCase();
            managerTheme.setBankName(bankName);
            switch (bankName) {
                case "santander":
                case "hsbc":
                case "scotiabank":
                case "banorte":
                case "autofin":
                case "bansefi":
                    myTheme = R.style.AppThemeRed;
                    break;
                case "citibanamex":
                case "banamex":
                case "bbva bancomer":
                case "famsa":
                case "bancoppel":
                case "monex":
                    myTheme = R.style.AppThemeBlue;
                    break;
                case "compartamos":
                case "banbajio":
                    myTheme = R.style.AppThemeBanbajio;
                    break;
                case "inbursa":
                case "actinver":
                    myTheme = R.style.AppThemeDarkBlue;
                    break;
                default:
                    myTheme = R.style.AppThemeOther;
                    break;
            }
            setTheme(myTheme);
            sessionManager.setTheme(myTheme);
            managerTheme.setThemeId(myTheme);

        } else {
            Log.e("Error", "bankName is null");
            String bank = sessionManager.getBankName();
            if (bank != null) {
                bank = bank.toLowerCase();
                Log.e("Error", "bankName is " + bank);
                setThemeByName(bank);
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
        String checkIdCut = checkId.substring(checkId.length() - 6);

        check.setCheckId("**" + checkIdCut);
        check.setDescription((String) item.get("description"));
        check.setQuantity((Double) item.get("monto"));
        check.setBeneficiary((String) item.get("beneficiario"));
        check.setDate(date[0]);
        String status = (String) item.get("estatus");
        System.out.println("cheque: " + checkId + " --> " + pickStatus(status));
        //check.setStatus();
        check.setStatus(pickStatus(status));
        return check;
    }

    protected int getIdBank(String value) {
        for (Map.Entry<String, Object> entry : bankIds.entrySet()) {
            String name = entry.getKey();
            int id = (int) entry.getValue();
            if (name.equals(value))
                return id;
        }

        return 0;
    }

    protected Bitmap compressImage(Bitmap original) {
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

    protected String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public static double getWayLatitude() {
        return wayLatitude;
    }

    public static void setWayLatitude(double wayLatitude) {
        ParentActivity.wayLatitude = wayLatitude;
    }

    public static double getWayLongitude() {
        return wayLongitude;
    }

    public static void setWayLongitude(double wayLongitude) {
        ParentActivity.wayLongitude = wayLongitude;
    }

    protected void validLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            logout();
        } else {
            saveLocationData();
        }


    }
}
