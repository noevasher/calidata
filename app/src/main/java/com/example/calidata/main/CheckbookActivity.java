package com.example.calidata.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.calidata.R;
import com.example.calidata.activities.query.RecyclerViewAdapterCheck;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.management.ManagerTheme;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckbookActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.floatingActionButton)
    public ImageView addCheckbookBtn;

    RecyclerViewAdapterCheckbook adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        setTheame(intent);
        setToolbar();

        ArrayList<String> checkbooks = new ArrayList<>();
        checkbooks.add("**** **** **** **** 1800");
        checkbooks.add("**** **** **** **** 1856");
        checkbooks.add("**** **** **** **** 7800");
        checkbooks.add("**** **** **** **** 9900");


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheckbook(this, checkbooks);
        //adapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), R.drawable.divider));

        //DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        //itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));

        //recyclerView.addItemDecoration(itemDecorator);

        recyclerView.setAdapter(adapter);
        addCheckbookBtn.setOnClickListener(v->{
            Toast.makeText(this, "Add new checkbook", Toast.LENGTH_LONG).show();
        });
        addCheckbookBtn.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addCheckbookBtn.bringToFront();
    }

    private void setTheame(Intent intent){
        int positionBank = intent.getIntExtra("bank", 4);
        ManagerTheme managerTheme = ManagerTheme.getInstance();

        switch (positionBank){
            case 0:
                setTheme(R.style.AppTheme);
                setThemeResId(R.style.AppTheme);
                managerTheme.setThemeId(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppThemeBanamex);
                setThemeResId(R.style.AppThemeBanamex);
                managerTheme.setThemeId(R.style.AppThemeBanamex);
                break;
            case 2:
                setTheme(R.style.AppThemeSantander);
                setThemeResId(R.style.AppThemeSantander);
                managerTheme.setThemeId(R.style.AppThemeSantander);
                break;
            case 3:
                setTheme(R.style.AppThemeBancomer);
                setThemeResId(R.style.AppThemeBancomer);
                managerTheme.setThemeId(R.style.AppThemeBancomer);
                break;
            case 4:
                setTheme(R.style.AppThemeOther);
                setThemeResId(R.style.AppThemeOther);
                managerTheme.setThemeId(R.style.AppThemeOther);
                break;
        }
    }

    private void setToolbar(){
        if(toolbar != null){
            toolbar.setTitle(getResources().getString(R.string.checkbook_title));
            toolbar.setBackgroundColor(getPrimaryColorInTheme());
            setSupportActionBar(toolbar);
        }
    }
}
