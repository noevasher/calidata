package com.example.calidata.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.calidata.R;
import com.example.calidata.management.ManagerTheme;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckbookAddActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.button_add)
    public Button addBtn;

    @OnClick(R.id.button_add)
    public void add(){
        //Intent intent = new Intent(this, CheckbookActivity.class);
        //startActivityForResult(intent, 0);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",0);
        returnIntent.putExtra("NUM","**** **** **** 3658");

        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_checkbook_add);
        ButterKnife.bind(this);

        setToolbar(toolbar, "Agregar", true);
        setTheme(getPrimaryColorInTheme());
        addBtn.setBackgroundColor(getPrimaryColorInTheme());
        addBtn.setTextColor(getColor(R.color.white));

    }
}
