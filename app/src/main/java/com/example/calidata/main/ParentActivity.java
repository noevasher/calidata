package com.example.calidata.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.calidata.R;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;
    private int themeResId;

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidLength(String target) {
        return target.length() >= WORD_LENGTH;
    }

    public static void displayEmptyField(Spinner spinner) {
        if (spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) spinner.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Selecciona una entidad");//changes the selected item text to this
        }
    }

    public static void displayEmptyField(EditText editText) {
        if (editText.getText().toString().isEmpty())
            editText.setError("Campo Obligatorio");
        //return editText.getText().toString().isEmpty();
    }

    public static boolean isEmptyField(EditText editText) {
        return editText.getText().toString().isEmpty();
        //return editText.getText().toString().isEmpty();
    }

    public static boolean comparePassword(EditText password, EditText passwordConfirm) {
        if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirm.setError("La contraseña debe de coincidir");
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

    public Drawable getLogoDrawable(int themeId) {

        switch (themeId) {
            case R.style.AppThemeBanamex:
                Log.i("TAG", "tema banamex");
                return ContextCompat.getDrawable(this, R.drawable.ic_citibanamex_logo);
            case R.style.AppThemeSantander:
                Log.i("TAG", "tema Santander");
                return ContextCompat.getDrawable(this, R.drawable.ic_santander_logo);

            case R.style.AppThemeBancomer:
                Log.i("TAG", "tema Bancomer");
                return ContextCompat.getDrawable(this, R.drawable.ic_bancomer_logo);

            case R.style.AppTheme:
                Log.i("TAG", "tema default");
                break;
            default:
                break;
        }
        return ContextCompat.getDrawable(this, R.drawable.ic_default_logo);
    }

    public String getThemeName() {
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            switch (themeResId) {
                case R.style.AppThemeBanamex:
                    break;
                case R.style.AppThemeBancomer:
                    break;
                case R.style.AppThemeSantander:
                    break;
                case R.style.AppTheme:
                    Log.i("TAG", "tema default");
                    break;
                default:
                    break;
            }
            return getResources().getResourceEntryName(themeResId);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        //*/
    }

    public int getThemeResId() {
        return themeResId;
    }

    public void setThemeResId(int themeResId) {
        this.themeResId = themeResId;
    }
}