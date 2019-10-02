package com.example.calidata.activities.query;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.query.filter.FilterActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("CheckResult")
public class CheckQueryActivity extends ParentActivity {
    private final static int FILTER_CODE = 222;
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

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;


    RecyclerViewAdapterCheck adapter;

    private Integer userId;
    private String token;
    private String checkbookId;
    public ArrayList<CheckModel> checks = new ArrayList<>();

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
        checkbookId = getIntent().getExtras().getString("checkbookId", null);
        userId = sessionManager.getUserId();
        token = sessionManager.getToken();


        getChecks();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, checks, R.layout.card_check);
        recyclerView.setAdapter(adapter);

        printConstraintSearch();
        setImageListener();
    }


    private void getChecks() {
        CheckbookController controller = new CheckbookController(this);
        if (token != null && userId != null && checkbookId != null) {
            controller.getChecksByUserIdAndCheckId(token, checkbookId, userId).subscribe(response -> {
                List<HashMap<String, Object>> data = response.getData();
                for (HashMap<String, Object> item : data) {
                    CheckModel check = loadChecks(item);
                    /*
                    CheckModel check = new CheckModel();
                    String[] date = ((String) item.get("fecha")).split("T");
                    String checkId = (String) item.get("iD_CheckID");
                    System.out.println("cheque: " + checkId);
                    check.setCheckId(checkId);
                    check.setDescription((String) item.get("description"));
                    check.setQuantity((Double) item.get("monto"));
                    check.setDate(date[0]);
                    String status = (String) item.get("estatus");

                    check.setStatus(pickStatus(status));
                    //*/
                    adapter.addAndSort(check);
                }
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();

            }, t -> {
                if (t.getMessage().equals("Unauthorized")) {
                    Toast.makeText(CheckQueryActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                    logout();
                } else {
                    Log.e("", t.getMessage());
                    Toast.makeText(CheckQueryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    private void setImageListener() {
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


    private void printConstraintSearch() {
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
                intent.putExtra("checkbookId", checkbookId);
                startActivityForResult(intent, FILTER_CODE);
                //startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handl0e it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        Gson gson = new Gson();
        if (requestCode == FILTER_CODE) {
            if (resultCode == FILTER_CODE) {

                List list = (List<CheckModel>) data.getSerializableExtra("list");
                checks.clear();

                for (Object item : list) {
                    JsonElement jsonElement = gson.toJsonTree(item);
                    CheckModel model = gson.fromJson(jsonElement, CheckModel.class);
                    String date = model.getDate();
                    model.setDate(date.split("T")[0]);
                    String status = model.getStatus();
                    model.setStatus(selectStatus(status));

                    checks.add(model);
                }
                adapter.notifyDataSetChanged();

            }
        }
    }

    private String selectStatus(String statusNumber) {
        switch (statusNumber) {
            case "1":
                return "Solicitado";
            case "2":
                return "Asignado";
            case "3":
                return "Entregado";
            case "4":
                return "Activado";
            case "5":
                return "Liberado";
            case "6":
                return "Pagado";
            case "7":
                return "Cancelado";
            case "8":
                return "Bloqueado";
            default:
                return "";
        }
    }
}
