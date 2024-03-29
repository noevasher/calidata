package com.example.calidata.register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.login.LoginActivity;
import com.example.calidata.login.controller.LoginController;
import com.example.calidata.login.managment.AESCrypt;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.models.BankModel;
import com.example.calidata.models.User;
import com.example.calidata.register.controller.RegisterController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("CheckResult")
public class RegisterActivity extends ParentActivity {

    //String[] bank = {"Selecciona Entidad", "Banamex", "Santander", "Bancomer", "Otro"};
    ArrayList<String> bankNames = new ArrayList<>();
    private boolean spinActive = false;
    private int attemps = 5;

    private static final int LOCATION_CODE_REGISTER = 1001;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.editText_name)
    public EditText nameText;

    @BindView(R.id.editText_email)
    public EditText emailText;

    @BindView(R.id.editText_password)
    public EditText passText;

    @BindView(R.id.editText_password_confirm)
    public EditText confirmPassText;

    @BindView(R.id.button_register)
    public Button registerBtn;

    @BindView(R.id.spinner)
    public Spinner spin;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.edittext_phone)
    public EditText phoneText;

    private RegisterController registerController;
    private ArrayAdapter adapter;

    private String encryptPassword;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (managerTheme.getThemeId() != 0) {
            setTheme(managerTheme.getFirstTheme());

        }
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        bankNames.add("Selecciona Entidad");
        adapter = new ArrayAdapter(this, R.layout.labels_bank, bankNames);
        adapter.setDropDownViewResource(R.layout.labels_bank);
        spin.setAdapter(adapter);

        //phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if (toolbar != null) {
            toolbar.setTitle(getResources().getString(R.string.register_title));
            final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> finish());
        }
        registerController = new RegisterController(getApplicationContext());
        loadBanks();

        registerBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String email = emailText.getText().toString();
                String password = passText.getText().toString();
                String name = nameText.getText().toString();
                String phone = phoneText.getText().toString();
                String bankNameSpin = (String) spin.getItemAtPosition(spin.getSelectedItemPosition());
                int bankId = getIdBank(bankNameSpin);

                try {
                    encryptPassword = AESCrypt.encrypt(password);
                    encryptPassword = encryptPassword.replace("\n", "").replace("\r", "");

                    if (ifValidForm()) {
                        progressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);
                        progressBar.setVisibility(View.VISIBLE);

                        User newUser = User.getInstance(name, email, encryptPassword, bankId);

                        Gson gson = new Gson();
                        String json = gson.toJson(newUser);

                        System.out.println(json);
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("userName", name);
                        body.put("email", email);
                        body.put("password", encryptPassword);
                        body.put("bankId", bankId);
                        body.put("CELULAR", phone.trim());

                        registerController.registerUserByBody(body).subscribe(response -> {
                            //registerController_.registerUserByJson(json).subscribe(response -> {
                            Double userId = (Double) response.getData().get("userId");
                            String username = newUser.getUserName();
                            LoginController loginController = new LoginController(getApplicationContext());
                            loginController.authentication(email, encryptPassword).subscribe(loginResponse -> {
                                sessionManager.setAccessToken(response.getTokenType()
                                        + " " + response.getAccessToken());
                                pickBankAndOpenCheckbookByName(bankNameSpin, bankId, email,
                                        userId.intValue(), username, phone);
                                progressBar.setVisibility(View.GONE);
                                setThemeByName(bankName);// set myTheme -->
                                sessionManager.setTheme(myTheme); //save theme

                                LoginActivity.getInstance().finish();
                                finish();
                                Toast.makeText(RegisterActivity.this, R.string.success_login, Toast.LENGTH_LONG).show();
                                initCountdown();

                            }, t -> {
                                Log.e("Error", t.getMessage());
                                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            });
//*/
                        }, t -> {
                            Log.e("Error", t.getMessage());
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);

                }


            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    spinActive = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), getString(R.string.select_bank_label), Toast.LENGTH_LONG).show();
            }
        });
        validEmptyFields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_CODE_REGISTER);

        } else {
            saveLocationData();
        }
    }

    private void loadBanks() {
        registerController.getBanks().subscribe(response -> {
            for (BankModel bank : response) {
                Log.i("TAG", bank.getIdBank() + "--> " + bank.getNameBank());
                bankNames.add(bank.getNameBank());
                bankIds.put(bank.getNameBank(), bank.getIdBank());
            }
            adapter.notifyDataSetChanged();

        }, t -> {
            Log.e("Error", "problema al cargar bancos");
            if (attemps > 0) {
                attemps--;
                loadBanks();
            }
        });
    }

    private boolean ifValidForm() {
        displayEmptyField(nameText);
        displayEmptyField(phoneText);
        displayEmptyField(emailText);
        displayEmptyField(passText);
        displayEmptyField(confirmPassText);
        displayEmptyField(spin);

        return !isEmptyField(nameText) && !isEmptyField(emailText) && !isEmptyField(passText)
                && !isEmptyField(confirmPassText) && !isValidSpinner(spin) && !isEmptyField(phoneText)
                && isValidEmail(emailText.getText().toString())
                && isValidPhone(phoneText.getText().toString())
                && comparePassword(passText, confirmPassText);
    }

    private boolean isValidSpinner(Spinner spinner) {
        return spinner.getSelectedItemPosition() == 0;
    }

    private void validEmptyFields() {
        nameText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                displayEmptyField(nameText);
            }
        });

        phoneText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                displayEmptyField(phoneText);
                if (!isValidPhone(phoneText.getText().toString())) {
                    phoneText.setError(getString(R.string.invalid_phone));
                }
            }
        });

        emailText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                displayEmptyField(emailText);
                if (!isValidEmail(emailText.getText().toString())) {
                    emailText.setError(getString(R.string.invalid_email));
                }

            }
        });

        passText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                displayEmptyField(passText);
                if (!isValidLength(passText.getText().toString())) {
                    passText.setError(getString(R.string.invalid_password));
                }
            }
        });
        confirmPassText.setOnFocusChangeListener((view, focus) -> {
            if (!focus) {
                displayEmptyField(confirmPassText);

                if (!isValidLength(confirmPassText.getText().toString())) {
                    confirmPassText.setError("Contraseña debe de tener 6 o más caracteres");
                } else {
                    comparePassword(passText, confirmPassText);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_CODE_REGISTER) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_CODE_REGISTER);
                    //return;
                }

            } else {
                Toast.makeText(this, getString(R.string.error_location_message), Toast.LENGTH_LONG).show();
                finish();
                LoginActivity.getInstance().finish();
            }
        }
    }


}
