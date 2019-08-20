package com.example.calidata.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.example.calidata.R;
import com.example.calidata.login.controller.LoginController;
import com.example.calidata.login.managmentLogin.RijndaelCrypt;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.register.RegisterActivity;
import com.example.calidata.session.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends ParentActivity {

    @BindView(R.id.button_register)
    public Button registerBtn;

    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        session = new SessionManager(getApplicationContext());

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        //final Button registerBtn = findViewById(R.id.button_register);

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
            String user = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            try {
                RijndaelCrypt encryptRijndael = new RijndaelCrypt(password);

                String encryptRij = encryptRijndael.encrypt(password.getBytes());
                String decryptRij = encryptRijndael.decrypt(encryptRij);
                Log.i("TAG- encrypt", encryptRij);
                Log.i("TAG- decrypt", decryptRij);

                String jsonResponse = "{'name' : 'noe', 'password': " + encryptRij + "}";

                Log.i("Response: ", jsonResponse);
                LoginController loginController = new LoginController();
                loginController.loadJSON();
                /*
                String encrypt = AESCrypt.encrypt(password);
                Log.i("TAG- encrypt", encrypt);
                String random = UUID.randomUUID().toString();
                Log.i("TAG- random", random);
                //*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            Intent intent = new Intent(LoginActivity.this, CheckbookActivity.class);
            String word = usernameEditText.getText().toString();
            switch (user) {
                case "banamex":
                    intent.putExtra("bank", 1);
                    break;
                case "santander":
                    intent.putExtra("bank", 2);
                    break;
                case "bancomer":
                    intent.putExtra("bank", 3);
                    break;
                default:
                    intent.putExtra("bank", 2);
                    break;
            }
            startActivity(intent);

            //session.createLoginSession("ABC", "abc@gmail.com");

            //sessionManager.createSession(user,user);

        });

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}
