package com.example.calidata.register;

import android.annotation.SuppressLint;
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
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends ParentActivity {

    //String[] bank = {"Selecciona Entidad", "Banamex", "Santander", "Bancomer", "Otro"};
    ArrayList<String> bankNames = new ArrayList<>();
    private boolean spinActive = false;
    private HashMap<String, Object> bankIds = new HashMap<>();
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
                        registerController.registerUserByBody(newUser).subscribe(response -> {
                            //registerController_.registerUserByJson(json).subscribe(response -> {
                            Double userId = (Double) response.getData().get("userId");
                            String username = newUser.getUserName();

                            LoginController loginController = new LoginController(getApplicationContext());
                            loginController.authentication(email, encryptPassword).subscribe(loginResponse -> {
                                pickBankAndOpenCheckbookByName(bankNameSpin, email, userId, username);
                                progressBar.setVisibility(View.GONE);
                                LoginActivity.getInstance().finish();
                                finish();
                                initCountdown();

                            }, t -> {
                                Log.e("Error", t.getMessage());
                                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            });

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

    private int attemps = 5;

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
        displayEmptyField(emailText);
        displayEmptyField(passText);
        displayEmptyField(confirmPassText);
        displayEmptyField(spin);

        return !isEmptyField(nameText) && !isEmptyField(emailText) && !isEmptyField(passText)
                && !isEmptyField(confirmPassText) && !isValidSpinner(spin)
                && isValidEmail(emailText.getText().toString())
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

    private int getIdBank(String value) {
        long i = 0;
        for (Iterator<Map.Entry<String, Object>> entries = bankIds.entrySet().iterator(); entries.hasNext(); ) {
            Map.Entry<String, Object> entry = entries.next();
            String name = entry.getKey();
            int id = (int) entry.getValue();
            if (name.equals(value))
                return id;
        }

        return 0;
    }
}
