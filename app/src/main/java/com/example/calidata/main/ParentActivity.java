package com.example.calidata.main;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.calidata.R;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.session.SessionManager;

import java.util.UUID;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;
    public ManagerTheme managerTheme;
    public SessionManager sessionManager;
    public static final int PICK_IMAGE = 1;
    private static CountDownTimer timer;
    private String token;
    //private static long TIME_EXPIRED = 3600000;
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

    public void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE);
    }

    /*
    protected void pickBankAndOpenCheckbook(int bank, String user) {
        Intent intent = new Intent(this, CheckbookActivity.class);

        switch (bank) {
            case 1:
                intent.putExtra("bank", 1);
                break;
            case 2:
                intent.putExtra("bank", 2);
                break;
            case 3:
                intent.putExtra("bank", 3);
                break;
            default:
                intent.putExtra("bank", 2);
                break;
        }
        sessionManager.createLoginSession(user, bank);
        startActivity(intent);
    }

//*/
    protected void pickBankAndOpenCheckbookByName(String bankName, String user, Double userId, String username) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);
        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankName, userId, username);
        startActivity(intent);
    }

    protected void pickBankAndOpenCheckbookByName(String bankName, String user, Double userId) {
        Intent intent = new Intent(this, CheckbookActivity.class);
        bankName = bankName.toLowerCase();
        intent.putExtra("bankName", bankName);
        //sessionManager.createLoginSession(user, bankName);
        sessionManager.createLoginSessionBank(user, bankName, userId);
        startActivity(intent);
    }

    protected void initCountdown() {
        if (timer == null) {
            timer = new CountDownTimer(TIME_EXPIRED, 1000) {
                public void onTick(long millisUntilFinished) {
                    int mod = 60;
                    //if (millisUntilFinished / 1000 % mod == 0) {
                    Toast.makeText(ParentActivity.this, "tu sesión terminará en: " + millisUntilFinished / 1000, Toast.LENGTH_LONG).show();
                    //}
                }

                public void onFinish() {
                    Toast.makeText(ParentActivity.this, "Sesión Terminada", Toast.LENGTH_LONG).show();
                    logout();
                    //finish();
                    //timer = null;
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
                    setTheme(R.style.AppThemeSantander);
                    managerTheme.setThemeId(R.style.AppThemeSantander);
                    break;
                case "hsbc":
                case "scotiabank":
                case "banorte":
                case "banamex":
                    setTheme(R.style.AppThemeBanamex);
                    managerTheme.setThemeId(R.style.AppThemeBanamex);
                    break;
                case "bancomer":
                    setTheme(R.style.AppThemeBancomer);
                    managerTheme.setThemeId(R.style.AppThemeBancomer);
                    break;
                case "banbajio":
                    setTheme(R.style.AppThemeBanbajio);
                    managerTheme.setThemeId(R.style.AppThemeBanbajio);
                    break;
                case "inbursa":
                case "compartamos":
                case "bancoppel":
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
        }
    }


    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 15);
    }

    protected void setExpireTime(Integer expire) {
        this.TIME_EXPIRED = expire * 1000;
    }

    protected void setToken(String token) {
        this.token = token;
    }
}
