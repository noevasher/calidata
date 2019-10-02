package com.example.calidata.activities.query.filter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;
import com.github.guilhe.views.SeekBarRangedView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("CheckResult")
public class FilterActivity extends ParentActivity implements AdapterView.OnItemSelectedListener {
    private final static int MAX = 1000000;
    private static final String CERO = "0";
    private static final String BARRA = "-";
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

    @BindView(R.id.imageView_clear_date)
    public ImageView clearDates;

    private String startTextDate = "";
    private String endTextDate = "";

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

    private Date startDate;
    private Date endDate;
    Calendar calendar = Calendar.getInstance();

    private boolean activeStartDate;
    private boolean activeEndDate;

    @OnClick(R.id.constraintLayout_date_start)
    public void getStartDate() {
        activeStartDate = true;
        configPicker(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);
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
                int status = spinner.getSelectedItemPosition();
                System.out.println(startTextDate + " -> " + endTextDate + " -> " + status);
                String token = sessionManager.getToken();
                Integer userId = sessionManager.getUserId();
                if (token != null && userId != null && checkbookId != null) {
                    HashMap<String, Object> body = buildBody(userId, status);
                    controller.getChecksByFilters(token, body).subscribe(response -> {
                        ArrayList<HashMap<String, Object>> checks = (ArrayList<HashMap<String, Object>>) response.getData();

                        Intent intent = new Intent();
                        intent.putExtra("list", checks);
                        setResult(FILTER_CODE, intent);
                        finish();//finishing activity

                    }, t -> {
                        if (t.getMessage().equals("Unauthorized")) {
                            Toast.makeText(FilterActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                            logout();
                        } else {
                            Log.e("", t.getMessage());
                            Toast.makeText(FilterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        clearDates.setOnClickListener(l -> {
            endText.setText("");
            endText.setVisibility(View.GONE);
            dateText.setText("");
            l.setVisibility(View.GONE);
        });

    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private HashMap<String, Object> buildBody(Integer userId, Integer status) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("idUsuario", userId);
        body.put("ID_CheckID", checkbookId);
        if (status != 0)
            body.put("status", "" + status);

        if (minVal != 0)
            body.put("min", minVal);
        if (maxVal != 0)
            body.put("max", maxVal);
        if (startTextDate != null && !startTextDate.isEmpty()) {
            body.put("dateStart", startTextDate);
        }
        if (endTextDate != null && !endTextDate.isEmpty()) {
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
        if (i > 0) {
            applyBtn.setEnabled(true);
            applyBtn.setBackgroundColor(getPrimaryColorInTheme());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private long dateStartInit;

    private void configPicker(boolean start) {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

            clearDates.setVisibility(View.VISIBLE);
            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado

            if (start) {
                startDate = calendar.getTime();
                startTextDate = diaFormateado + BARRA + mesFormateado + BARRA + year;
                //startTextDate = formatDate(startTextDate);
                dateText.setText(startTextDate);
                dateText.setTextColor(getPrimaryColorInTheme());

                calendar.set(year, month, dayOfMonth);
                dateStartInit = calendar.getTime().getTime();
                configPicker(false);
            } else {
                endText.setVisibility(View.VISIBLE);

                endDate = calendar.getTime();
                endTextDate = diaFormateado + BARRA + mesFormateado + BARRA + year;

                endText.setText(endTextDate);
                endText.setTextColor(getPrimaryColorInTheme());

            }

        }, anio, mes, dia);
        recogerFecha.getDatePicker().setMaxDate(System.currentTimeMillis());
        if (!start) {
            recogerFecha.getDatePicker().setMinDate(dateStartInit);
            applyBtn.setEnabled(true);
            applyBtn.setBackgroundColor(getPrimaryColorInTheme());

        }
        recogerFecha.show();
    }

    private boolean isValidDate() {
        if (endDate != null && startDate != null) {
            return endDate.getTime() >= startDate.getTime();
        } else return false;
    }

    public String formatDate(String target) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //String dateInString = "07/06/2013";
        try {

            Date date = formatter.parse(target);
            System.out.println(date);
            System.out.println(formatter.format(date));
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


}
