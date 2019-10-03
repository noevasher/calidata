package com.example.calidata.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.login.controller.LoginController;
import com.example.calidata.login.managment.AESCrypt;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.models.BankModel;
import com.example.calidata.models.User;
import com.example.calidata.register.RegisterActivity;
import com.example.calidata.utilities.controllers.UserController;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("CheckResult")
public class LoginActivity extends ParentActivity {

    public static LoginActivity loginActivity;

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

    @OnClick(R.id.textView_forgot_password)
    public void forgotPassword() {
        showAlertPassword();
    }

    private static final int DEFAULT_BANK = 2;
    private String user;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = this;

        //logout();
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
                Log.e("Error", getString(R.string.error_get_theme) + ": " + e.getMessage());
            }

        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (!sessionManager.isLoggedIn()) {
            usernameEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    if (usernameEditText.getText().toString().isEmpty()) {
                        usernameEditText.setError(getString(R.string.required_field_label));
                    } else {
                        if (!isValidEmail(usernameEditText.getText().toString())) {
                            usernameEditText.setError(getString(R.string.invalid_email));
                        }
                    }
                }
            });
            passwordEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    if (passwordEditText.getText().toString().isEmpty()) {
                        passwordEditText.setError(getString(R.string.required_field_label));
                    } else {
                        if (!isValidLength(passwordEditText.getText().toString())) {
                            //passwordEditText.setError(getString(R.string.invalid_password));
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

                    if (isValidForm()) {
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
                                    for (BankModel bank : list) {
                                        if (bank.getIdBank() == bankId) {
                                            Double userId = response.getUserId();
                                            setExpireTime(response.getExpiteIn());
                                            User newUser = User.getInstance(response.getUserId(), user, bankId);
                                            pickBankAndOpenCheckbookByName(bank.getNameBank(), user, userId.intValue());
                                            sessionManager.setAccessToken(response.getTokenType()
                                                    + " " + response.getAccessToken());
                                            finish();
                                            showLoginFailed(R.string.success_login);
                                            progressBar.setVisibility(View.GONE);
                                            initCountdown();
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

    private boolean isValidForm() {
        displayEmptyField(usernameEditText);
        displayEmptyField(passwordEditText);

        return !isEmptyField(usernameEditText) && !isEmptyField(passwordEditText)
                && isValidEmail(usernameEditText.getText().toString());
    }


    public static LoginActivity getInstance() {
        return loginActivity;
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showAlertPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.alert_change_username, null);
        builder.setView(view);

        EditText password = view.findViewById(R.id.editText_username);
        password.setHint(getString(R.string.prompt_email));
        Button sendBtn = view.findViewById(R.id.button_save);
        sendBtn.setText(getString(R.string.send_email));
        AlertDialog alertDialog = builder.create();

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = password.getText().toString();

                if (text.isEmpty()) {
                    password.setError(getString(R.string.error_empty_email));
                    sendBtn.setEnabled(false);
                } else if (!isValidEmail(password.getText().toString())) {
                    password.setError(getString(R.string.invalid_email));
                    sendBtn.setEnabled(false);
                } else {
                    sendBtn.setEnabled(true);
                    sendBtn.setBackgroundColor(ContextCompat.getColor(LoginActivity.this.getApplicationContext(), R.color.colorPrimary));

                }

            }
        });

        sendBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                UserController controller = new UserController(LoginActivity.this);
                HashMap<String, Object> body = new HashMap<>();
                body.put("Correo", password.getText().toString());
                controller.forgotPassword(body).subscribe(response -> {
                    if (response.equals("OK")) {
                        Toast.makeText(LoginActivity.this, "Se envió un correo a: "
                                + password.getText().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                }, t -> {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                });
            }
        });
        alertDialog.show();
    }
}
