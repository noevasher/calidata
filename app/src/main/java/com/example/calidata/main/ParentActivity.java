package com.example.calidata.main;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calidata.R;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;
    private int themeResId;

    public static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidLength(String target){
        return target.length() >= WORD_LENGTH;
    }

    public static void displayEmptyField(EditText editText){
        if(editText.getText().toString().isEmpty())
            editText.setError("Campo Obligatorio");
        //return editText.getText().toString().isEmpty();
    }
    public static boolean isEmptyField(EditText editText){
        return editText.getText().toString().isEmpty();
        //return editText.getText().toString().isEmpty();
    }

    public static void comparePassword(EditText password, EditText passwordConfirm){
        if(password.getText().toString().equals(passwordConfirm.getText().toString())){
            passwordConfirm.setError("La contraseña debe de coincidir");
        }
    }

    public int getPrimaryColorInTheme(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public String getLogoDrawable(){

        switch (themeResId){
            case R.style.AppThemeBanamex:
                Log.i("TAG", "tema banamex");
                break;
            case R.style.AppTheme:
                Log.i("TAG", "tema default");
                break;
            default:
                break;
        }
        return getResources().getResourceEntryName(themeResId);
        //*/

    }

    public String getThemeName(){
        PackageInfo packageInfo;
        try{
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            switch (themeResId){
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
        }
        catch (PackageManager.NameNotFoundException e) {
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
