package com.example.calidata.activities.cancel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.query.RecyclerViewAdapterCheck;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;
import com.example.calidata.models.CheckbookModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckCancelActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;

    private RecyclerViewAdapterCheck adapter;

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheck(this, checks, R.layout.card_cancel);
        recyclerView.setAdapter(adapter);



    }
}
