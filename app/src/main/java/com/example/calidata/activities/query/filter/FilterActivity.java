package com.example.calidata.activities.query.filter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.activities.query.CheckQueryActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckArrayModel;
import com.github.guilhe.views.SeekBarRangedView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("CheckResult")
public class FilterActivity extends ParentActivity implements AdapterView.OnItemSelectedListener {
    private final static int MAX = 1000000;
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private final static int FILTER_CODE = 222;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.button_apply)
    public Button applyBtn;


    @BindView(R.id.textView_min)
    public TextView minText;

    @BindView(R.id.textView_max)
    public TextView maxText;

    @BindView(R.id.spinner)
    public Spinner spinner;

    @BindView(R.id.textView_date_start)
    public TextView dateText;

    @BindView(R.id.textView_date_end)
    public TextView endText;

    private String startTextDate;
    private String endTextDate;

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private SeekBarRangedView rangebar;
    private ColorStateList colorText;
    private String checkbookId;
    private float minVal;
    private float maxVal;

    private CheckbookController controller;

    @OnClick(R.id.constraintLayout_date_start)
    public void getStartDate() {
        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
/**
 *También puede cargar los valores que usted desee
 */
        DatePickerDialog recogerFecha = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

            applyBtn.setEnabled(true);
            applyBtn.setBackgroundColor(getPrimaryColorInTheme());

            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado
            dateText.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            dateText.setTextColor(getPrimaryColorInTheme());

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    @OnClick(R.id.constraintLayout_date_end)
    public void getEndDate() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

            applyBtn.setEnabled(true);
            applyBtn.setBackgroundColor(getPrimaryColorInTheme());

            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado
            endText.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
            endText.setTextColor(getPrimaryColorInTheme());

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        startTextDate = dateText.getText().toString();
        endTextDate = endText.getText().toString();
        colorText = dateText.getTextColors();
        String title = getResources().getString(R.string.filter_title);
        setToolbar(toolbar, title, true);

        rangebar = findViewById(R.id.rangebar1);
        initSeekBar();
        //applyBtn.setBackgroundColor(getPrimaryColorInTheme());
        applyBtn.setBackgroundColor(getColor(R.color.colorPrimaryGray));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_check, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        checkbookId = getIntent().getExtras().getString("checkbookId", null);


        controller = new CheckbookController(this);


        applyBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                int position = spinner.getSelectedItemPosition();
                Log.i("", startTextDate + " -> " + endTextDate + " -> " +position);
                System.out.println( "LOG");
                System.out.println( startTextDate + " -> " + endTextDate + " -> " +position);
                String token = sessionManager.getToken();
                Integer userId = sessionManager.getUserId().intValue();
                if (token != null && userId != null && checkbookId != null) {
                    HashMap<String, Object> body = buildBody(userId);
                    controller.getChecksByFilters(token, body).subscribe(response -> {
                        ArrayList<HashMap<String, Object>> checks = (ArrayList<HashMap<String, Object>>) response.getData();
                        Intent intent = new Intent();
                        intent.putExtra("list", checks);

                        setResult(FILTER_CODE,intent);
                        finish();//finishing activity

                    });
                }
            }
        });
    }

    private HashMap<String, Object> buildBody(Integer userId){
        HashMap<String, Object> body = new HashMap<>();
        body.put("idUsuario", userId);
        body.put("ID_CheckID", checkbookId);
        if(minVal != 0 )
            body.put("min", minVal);
        if(maxVal != 0 )
            body.put("max", maxVal);
        if(spinner.getSelectedItemPosition() > 0){
            body.put("status", spinner.getSelectedItemPosition());
        }
        if(startTextDate != null && !startTextDate.isEmpty()){
            body.put("dateStart", startTextDate);
        }
        if(endTextDate != null && !endTextDate.isEmpty()){
            body.put("dateEnd", endTextDate);
        }
        return body;
    }
    private void initSeekBar() {
        String min = "min: 0";
        String max = "max: " + MAX;
        minText.setText(min);
        maxText.setText(max);

        rangebar.setRounded(true);
        rangebar.setProgressColor(getPrimaryColorInTheme());
        rangebar.setMaxValue(MAX);
        rangebar.setMinValue(0);
        rangebar.setOnSeekBarRangedChangeListener(new SeekBarRangedView.OnSeekBarRangedChangeListener() {
            @Override
            public void onChanged(SeekBarRangedView view, float minValue, float maxValue) {
                updateLayout(minValue, maxValue);
            }

            @Override
            public void onChanging(SeekBarRangedView view, float minValue, float maxValue) {
                applyBtn.setEnabled(true);
                applyBtn.setBackgroundColor(getPrimaryColorInTheme());

                updateLayout(minValue, maxValue);
            }

            private void updateLayout(float minValue, float maxValue) {
                minVal = minValue;
                maxVal = maxValue;
                String min = String.format(Locale.getDefault(), "min: %2.0f", minValue);
                String max = String.format(Locale.getDefault(), "max: %2.0f", maxValue);

                minText.setText(min);
                maxText.setText(max);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        //menu.setGroupEnabled(0, false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            Log.i("TAG", "reset filters");
            spinner.setSelection(0);
            endText.setText(endTextDate);
            dateText.setText(startTextDate);
            dateText.setTextColor(colorText);
            endText.setTextColor(colorText);

            rangebar.setSelectedMaxValue(MAX);
            rangebar.setSelectedMinValue(0);

            applyBtn.setEnabled(false);
            applyBtn.setBackgroundColor(getColor(R.color.colorPrimaryGray));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i > 0){
            applyBtn.setEnabled(true);
            applyBtn.setBackgroundColor(getPrimaryColorInTheme());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
