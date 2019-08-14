package com.example.calidata.register;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.main.MainActivity;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.models.UserModel;
import com.google.gson.Gson;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends ParentActivity{

    String[] bank = { "Selecciona Entidad", "Banamex", "Santander", "Bancomer", "Otro"};
    private boolean spinActive = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.labels_bank,bank);
        adapter.setDropDownViewResource(R.layout.labels_bank);

        spin.setAdapter(adapter);

        registerBtn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            if(ifValidForm()) {
                intent.putExtra("bank", spin.getSelectedItemPosition());
                startActivity(intent);
                finish();
            }
            String name  = nameText.getText().toString();
            String email  = emailText.getText().toString();
            String password = passText.getText().toString();

            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setEmail(email);
            userModel.setPassword(password);
            userModel.setToken(UUID.randomUUID().toString());
            Gson gson = new Gson();
            //gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(userModel);

            System.out.println(json);

        });

        if(toolbar != null){

            toolbar.setTitle(getResources().getString(R.string.register_title));
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            setSupportActionBar(toolbar);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> finish());
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    spinActive = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Selecciona una entidad", Toast.LENGTH_LONG).show();
            }
        });
        validEmptyFields();
    }

    private boolean ifValidForm() {
        displayEmptyField(nameText);
        displayEmptyField(emailText);
        displayEmptyField(passText);
        displayEmptyField(confirmPassText);
        displayEmptyField(spin);

        return !isEmptyField(nameText) && !isEmptyField(emailText) && !isEmptyField(passText)
                && !isEmptyField(confirmPassText) && !isValidSpinner(spin)
                && comparePassword(passText, confirmPassText);
    }

    private boolean isValidSpinner(Spinner spinner){
        return spinner.getSelectedItemPosition() == 0;
    }

    private void validEmptyFields(){
        nameText.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(nameText);
            }
        });
        emailText.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(emailText);
            }
        });

        passText.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(passText);
                if(!isValidLength(passText.getText().toString())){
                    passText.setError("Contraseña debe de tener 6 o más caracteres");
                }
            }
        });
        confirmPassText.setOnFocusChangeListener((view, focus) -> {
            if(!focus){
                displayEmptyField(confirmPassText);

                if(!isValidLength(confirmPassText.getText().toString())){
                    confirmPassText.setError("Contraseña debe de tener 6 o más caracteres");
                }else {
                    comparePassword(passText, confirmPassText);
                }
            }
        });
    }
}
