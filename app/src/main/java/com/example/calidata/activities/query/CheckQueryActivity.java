package com.example.calidata.activities.query;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.query.filter.FilterActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckQueryActivity extends ParentActivity {

    @BindView(R.id.imageView_back)
    public ImageView backImg;

    @BindView(R.id.constraint_search)
    public ConstraintLayout constraintSearch;

    @BindView(R.id.filter_icon)
    public ImageView filterIcon;

    @BindView(R.id.search_icon)
    public ImageView searchIcon;

    RecyclerViewAdapterCheck adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_query);
        ButterKnife.bind(this);
        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the RecyclerView-
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, animalNames, R.layout.card_check);
        recyclerView.setAdapter(adapter);

        printConstraintSearch();
        setImageListener();
    }

    private void setImageListener(){
        backImg.setOnClickListener(v->{
            finish();
        });

        filterIcon.setOnClickListener(v->{
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);
        });
    }

    private void printConstraintSearch(){
        managerTheme = ManagerTheme.getInstance();
        int themeId = managerTheme.getThemeId();
        setTheme(themeId);
        constraintSearch.setBackgroundColor(getPrimaryColorInTheme());

    }
}
