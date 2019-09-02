package com.example.calidata.login;

import android.annotation.SuppressLint;
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

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.login.controller.LoginController;
import com.example.calidata.login.managment.AESCrypt;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.models.Bank;
import com.example.calidata.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("ResultOfMethodCallIgnored")
@SuppressLint("CheckResult")
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


    private static final int DEFAULT_BANK = 2;
    private String user;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;

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
            loginButton.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    user = usernameEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);

                    try {
                        String encryptPassword = AESCrypt.encrypt(password);
                        encryptPassword = encryptPassword
                                .replace("\n", "")
                                .replace("\r", "");
                        LoginController loginController = new LoginController(getApplicationContext());
                        loginController.authentication(user, encryptPassword).subscribe(response -> {
                            sessionManager.isLoggedIn();
                            Integer bankId = response.getBankId() == null ? DEFAULT_BANK : response.getBankId();
                            loginController.getBanks().subscribe(list -> {
                                for (Bank bank : list) {
                                    if (bank.getIdBank() == bankId) {
                                        Double userId = response.getUserId();
                                        pickBankAndOpenCheckbookByName(bank.getNameBank(), user, userId);
                                        finish();
                                        showLoginFailed(R.string.success_login);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });


                        }, t -> {
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            registerBtn.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });

        } else {
            Intent intent = new Intent(this, CheckbookActivity.class);
            int themeId = sessionManager.getTheme();
            String bankName = sessionManager.getBankName();
            intent.putExtra("bank", bankName);
            managerTheme.setThemeId(themeId);
            managerTheme.setBankName(bankName);
            startActivity(intent);
            finish();
        }
    }

    static LoginActivity loginActivity;

    public static LoginActivity getInstance() {
        return loginActivity;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    private String generateRequest(String encryptRij) {
        return "{'User' : 'noe', 'IdPass': " + encryptRij + "}";
    }


    private boolean validSession() {
        sessionManager.checkLogin();
        return false;
    }
}
