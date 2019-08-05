package com.example.calidata.main;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ParentActivity extends AppCompatActivity {
    private static final int WORD_LENGTH = 6;

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
        boolean b = editText.getText().toString().isEmpty();
        return b;
        //return editText.getText().toString().isEmpty();
    }

    public static void comparePassword(EditText password, EditText passwordConfirm){
        if(password.getText().toString().equals(passwordConfirm.getText().toString())){
            passwordConfirm.setError("La contraseña debe de coincidir");
        }
    }
}
