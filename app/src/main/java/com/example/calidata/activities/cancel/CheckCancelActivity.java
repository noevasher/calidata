package com.example.calidata.activities.cancel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.query.RecyclerViewAdapterCheck;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("CheckResult")
public class CheckCancelActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    private RecyclerViewAdapterCheck adapter;

    @Override
    protected void onResume() {
        super.onResume();
        validLocationPermission();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());

        setContentView(R.layout.activity_check_cancel);
        ButterKnife.bind(this);

        String title = getString(R.string.cancel_check_title);
        setToolbar(toolbar, title, true);

        ArrayList<CheckModel> checks = new ArrayList<>();
        String checkbookId = getIntent().getExtras().getString("checkbookId", null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, checks, R.layout.card_cancel);
        recyclerView.setAdapter(adapter);


        CheckbookController controller = new CheckbookController(this);
        HashMap<String, Object> body = new HashMap<>();
        body.put("ID_CheckID", checkbookId);
        body.put("idUsuario", sessionManager.getUserId());
        //body.put("status", "4");
        String token = sessionManager.getToken();
        if (token != null) {
            controller.getChecksByFilters(token, body).subscribe(response -> {
                List<HashMap<String, Object>> data = response.getData();
                for (HashMap<String, Object> item : data) {
                    CheckModel check = loadChecks(item);
                    if (check.getStatus().equals("Activado")) {
                        checks.add(check);
                    }
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }, t -> {
                if (t.getMessage().equals("Unauthorized")) {
                    Toast.makeText(CheckCancelActivity.this,
                            getString(R.string.start_session), Toast.LENGTH_LONG).show();
                    logout();
                } else {
                    Log.e("", t.getMessage());
                    Toast.makeText(CheckCancelActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
