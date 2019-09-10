package com.example.calidata.activities.query;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.CheckController;
import com.example.calidata.activities.query.filter.FilterActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckQueryActivity extends ParentActivity {

    /*
    @BindView(R.id.imageView_back)
    public ImageView backImg;

    @BindView(R.id.constraint_search)
    public ConstraintLayout constraintSearch;

    @BindView(R.id.filter_icon)
    public ImageView filterIcon;

//*/
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    RecyclerViewAdapterCheck adapter;

    private Double userId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.activity_check_query);
        ButterKnife.bind(this);
        setToolbar(toolbar, getResources().getString(R.string.query_check_title), true);
        //setArrowToolbar(too);
        // data to populate the RecyclerView with

        userId = sessionManager.getUserId();
        token = sessionManager.getToken();
        ArrayList<CheckModel> checks = new ArrayList<>();

        CheckbookController controller = new CheckbookController(this);
        if(token != null && userId != null) {
            controller.getChecksByUserId(token, userId.intValue()).subscribe(response -> {
                List<HashMap<String,Object>> data = response.getData();
                for(HashMap<String,Object> item :data){
                    CheckModel check = new CheckModel();
                    check.setCheckId((String) item.get("checkId"));
                    //check.setDescription("check --> " + i);
                    //check.setQuantity(Double.parseDouble((String) item.get("count")));
                    check.setQuantity(0.0);
                    //check.setDate(i+10 + "/05/2019");
                    check.setStatus("activo");
                    checks.add(check);
                }
                adapter.notifyDataSetChanged();

            }, t -> {
                Log.e("", t.getMessage());
                t.printStackTrace();
            });

        }

        /*
        for(int i = 0; i < 5; i++) {
            CheckModel check = new CheckModel();
            check.setCheckId(shortUUID());
            check.setCheckModelId(shortUUID());
            check.setDescription("check --> " + i);
            check.setQuantity((double) (100 * (i+1)));
            check.setDate(i+10 + "/05/2019");
            check.setStatus("activo");
            checks.add(check);
        }
        for(int i = 0; i < 5; i++) {
            CheckModel check = new CheckModel();
            check.setCheckId(shortUUID());
            check.setCheckModelId(shortUUID());
            check.setDescription("check --> " + i);
            check.setQuantity((double) (200 * (i+1)));
            check.setDate(i+10 + "/05/2019");
            check.setStatus("pagado");
            checks.add(check);
        }

        for(int i = 0; i < 5; i++) {
            CheckModel check = new CheckModel();
            check.setCheckId(shortUUID());
            check.setCheckModelId(shortUUID());
            check.setDescription("check --> " + i);
            check.setQuantity((double) (200 * (i+1)));
            check.setDate(i+10 + "/05/2019");
            check.setStatus("cancelado");
            checks.add(check);
        }
        //*/
        // set up the RecyclerView-
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, checks, R.layout.card_check);
        recyclerView.setAdapter(adapter);

        printConstraintSearch();
        setImageListener();
    }

    private void setImageListener(){
        /*
        backImg.setOnClickListener(v->{
            finish();
        });

        filterIcon.setOnClickListener(v->{
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);
        });
        //*/
    }


    private void printConstraintSearch(){
        managerTheme = ManagerTheme.getInstance();
        int themeId = managerTheme.getThemeId();
        setTheme(themeId);
        //constraintSearch.setBackgroundColor(getPrimaryColorInTheme());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.query_menu, menu);

        Drawable drawable = menu.findItem(R.id.action_filter).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, FilterActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handl0e it.
                return super.onOptionsItemSelected(item);

        }
    }

}
