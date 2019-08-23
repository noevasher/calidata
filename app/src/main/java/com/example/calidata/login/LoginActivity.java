package com.example.calidata.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.example.calidata.R;
import com.example.calidata.login.controller.LoginController;
import com.example.calidata.login.managment.RijndaelCrypt;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends ParentActivity {

    @BindView(R.id.button_register)
    public Button registerBtn;

    @BindView(R.id.login)
    public Button loginButton;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.username)
    public EditText usernameEditText;

    @BindView(R.id.password)
    public EditText passwordEditText;


    private static final int DEFAULT_BANK = 3;
    private String user;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (managerTheme.getThemeId() != 0) {
            setTheme(managerTheme.getFirstTheme());
        } else {
            PackageInfo packageInfo;
            try {
                packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
                int themeResId = packageInfo.applicationInfo.theme;
                //String theme = getResources().getResourceEntryName(themeResId);
                managerTheme.saveFirstTheme(themeResId);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Error", "error al obtener tema de aplicacion: " + e.getMessage());
            }

        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Toast.makeText(this, "" + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();

        if (!sessionManager.isLoggedIn()) {
            usernameEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    if (usernameEditText.getText().toString().isEmpty()) {
                        usernameEditText.setError("Campo Requerido");
                    } else {
                        if (!isValidEmail(usernameEditText.getText().toString())) {
                            usernameEditText.setError("Email No valido");
                        }
                    }
                }
            });
            passwordEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    if (passwordEditText.getText().toString().isEmpty()) {
                        passwordEditText.setError("Campo Requerido");
                    } else {
                        if (!isValidLength(passwordEditText.getText().toString())) {
                            //passwordEditText.setError("Longitud de Contraseña debe ser mayor a 5 caracteres");
                        }
                    }
                }
            });

            passwordEditText.setOnEditorActionListener((v, actionId, event) -> false);

            loginButton.setOnClickListener(v -> {
                user = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                progressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);

                try {
                    RijndaelCrypt encryptRijndael = new RijndaelCrypt(password);

                    String encryptRij = encryptRijndael.encrypt(password.getBytes());

                    LoginController loginController = new LoginController(getApplicationContext());
                    loginController.loadJson(user, password).subscribe(response -> {
                        sessionManager.isLoggedIn();
                        Integer bankId = response.getBankId() == null ? DEFAULT_BANK : response.getBankId();
                        pickBank(bankId);
                        finish();
                        showLoginFailed(R.string.success_login);
                        progressBar.setVisibility(View.GONE);

                    }, t -> {
                        showLoginFailed(R.string.fail_login);
                        //Toast.makeText(LoginActivity.this, "Error al accesar", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            registerBtn.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        } else {
            Intent intent = new Intent(this, CheckbookActivity.class);
            int themeId = sessionManager.getTheme();
            intent.putExtra("bank", themeId);
            managerTheme.setThemeId(themeId);
            startActivity(intent);
            finish();
        }
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    private String generateRequest(String encryptRij) {
        return "{'User' : 'noe', 'IdPass': " + encryptRij + "}";
    }

    private void pickBank(int bank) {
        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        Intent intent = new Intent(LoginActivity.this, CheckbookActivity.class);
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

    private boolean validSession() {
        sessionManager.checkLogin();
        return false;
    }
}
