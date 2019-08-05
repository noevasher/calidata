package com.example.calidata.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.MainActivity;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;

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

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.labels_bank,bank);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        //adapter.setDropDownViewResource(R.layout.spinner_text_color);

        adapter.setDropDownViewResource(R.layout.labels_bank);

        spin.setAdapter(adapter);

        registerBtn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            if(spin.getSelectedItemPosition() == 0){
                //Toast.makeText(getApplicationContext(), "Selecciona una entidad", Toast.LENGTH_LONG).show();
                //if(spinActive){
                    TextView errorText = (TextView)spin.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Selecciona una entidad");//changes the selected item text to this
                    //spinActive = true;
                //}
            }
            if(ifValidForm()) {
                intent.putExtra("bank", spin.getSelectedItemPosition());
                startActivity(intent);
                finish();
            }
        });

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setTitle("Registrar");
            final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24px);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            toolbar.setNavigationIcon(upArrow);
            toolbar.setNavigationOnClickListener(view -> finish());
            //getSupportActionBar().setHomeAsUpIndicator(upArrow);
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

        return !isEmptyField(nameText) || !isEmptyField(emailText) || !isEmptyField(passText) || !isEmptyField(confirmPassText);
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
