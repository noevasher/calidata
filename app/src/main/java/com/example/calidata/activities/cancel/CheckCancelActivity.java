package com.example.calidata.activities.cancel;

import android.os.Bundle;
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

public class CheckCancelActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;

    private RecyclerViewAdapterCheck adapter;
    private String checkbookId;

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
        checkbookId = getIntent().getExtras().getString("checkbookId", null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, checks, R.layout.card_cancel);
        recyclerView.setAdapter(adapter);


        CheckbookController controller = new CheckbookController(this);
        HashMap<String, Object> body = new HashMap<>();
        body.put("ID_CheckID", checkbookId);
        body.put("idUsuario", sessionManager.getUserId().intValue());
        body.put("status", "4");
        String token = sessionManager.getToken();
        if (token != null) {
            controller.getChecksByFilters(token, body).subscribe(response -> {
                List<HashMap<String, Object>> data = response.getData();
                for (HashMap<String, Object> item: data) {
                    CheckModel check = new CheckModel();
                    check.setCheckId((String) item.get("iD_CheckID"));
                    check.setCheckModelId((String) item.get("iD_CheckID"));
                    check.setDescription("check --> " + item.get("descripcion"));
                    check.setQuantity((Double) item.get("monto"));
                    check.setDate((String) item.get("fecha"));
                    check.setStatus("Activo");
                    System.out.println(item.get("status"));
                    checks.add(check);
                }
                adapter.notifyDataSetChanged();
            }, t -> {
                Toast.makeText(CheckCancelActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }
}
