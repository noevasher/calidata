package com.example.calidata.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.login.LoginActivity;
import com.example.calidata.main.adapters.ItemClickSupport;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.utilities.SettingsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckbookActivity extends ParentActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.floatingActionButton)
    public ImageView addCheckbookBtn;

    RecyclerViewAdapterCheckbook adapter;

    private ArrayList<String> checkbooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        setTheme(intent);
        String title = getResources().getString(R.string.checkbook_title);
        setToolbar(toolbar, title, false);

        checkbooks = new ArrayList<>();

        checkbooks.add("**** **** **** **** 1800");
        checkbooks.add("**** **** **** **** 1856");
        checkbooks.add("**** **** **** **** 7800");
        checkbooks.add("**** **** **** **** 9900");
//*/

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapterCheckbook(this, checkbooks);

        recyclerView.setAdapter(adapter);
        addCheckbookBtn.setOnClickListener(v -> {
            /*
            Toast.makeText(this, "Add new checkbook", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(this, CheckActiveActivity.class);
            startActivity(intent1);
            //*/
            openDialog();

        });
        addCheckbookBtn.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addCheckbookBtn.bringToFront();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                (recyclerView1, position, v) -> {
                    Toast.makeText(CheckbookActivity.this, "position: " + adapter.getItem(position), Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(CheckbookActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
        );

    }

    private void setTheme(Intent intent) {
        int positionBank = intent.getIntExtra("bank", 4);
        managerTheme = ManagerTheme.getInstance();

        switch (positionBank) {
            case 0:
                setTheme(R.style.AppTheme);
                managerTheme.setThemeId(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.AppThemeBanamex);
                managerTheme.setThemeId(R.style.AppThemeBanamex);
                break;
            case 2:
                setTheme(R.style.AppThemeSantander);
                managerTheme.setThemeId(R.style.AppThemeSantander);
                break;
            case 3:
                setTheme(R.style.AppThemeBancomer);
                managerTheme.setThemeId(R.style.AppThemeBancomer);
                break;
            case 4:
                setTheme(R.style.AppThemeOther);
                managerTheme.setThemeId(R.style.AppThemeOther);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.emit_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        TextView label = view.findViewById(R.id.textView_label);
        label.setText(getString(R.string.active_checkbook_label));
        Button scanBtn = view.findViewById(R.id.button_yes);
        scanBtn.setBackgroundColor(getPrimaryColorInTheme());
        scanBtn.setOnClickListener(v -> {
            //Intent intent = new Intent(v.getContext(), CheckEmitActivity.class);
            //intent.putExtra("QR", true);
            //startActivity(intent);
            readQR();
            alertDialog.dismiss();
        });

        Button searchBtn = view.findViewById(R.id.button_no);
        searchBtn.setText(getString(R.string.insert_data));
        searchBtn.setBackgroundColor(getPrimaryColorInTheme());
        searchBtn.setOnClickListener(v -> {
            Intent i = new Intent(this, CheckbookAddActivity.class);
            startActivityForResult(i, 0);
            alertDialog.dismiss();

        });
        //*/

        alertDialog.show();

    }

    private void readQR() {
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                String num = data.getStringExtra("NUM");

                if(num != null)
                    checkbooks.add(num);
                else
                    checkbooks.add(contents);
                adapter.notifyDataSetChanged();
                //TextView textView = fragmentQR.getView().findViewById(R.id.textView_content);
                //textView.setText(contents + "\n" + format);

                //Log.i("TAG-QR contents: ", contents);
                //Log.i("TAG-QR format: ", format);
                //Log.i("TAG-QR data sqcheme", data.getData().getScheme());

            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                Log.i("TAG-QR", "CANCELADO");
            }
        }
    }
}
