package com.example.calidata.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.login.LoginActivity;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.utilities.HelpActivity;
import com.example.calidata.utilities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CheckbookActivity extends ParentActivity {

    @BindView(R.id.drawer)
    public DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.floatingActionButton)
    public ImageView addCheckbookBtn;

    RecyclerViewAdapterCheckbook adapter;

    private ArrayList<String> checkbooks;

    public CircleImageView imageProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        //setTheme(intent);
        setThemeByName(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook);
        ButterKnife.bind(this);

        String title = getResources().getString(R.string.checkbook_title);
        setToolbar(toolbar, title, false);

        initNavBar();

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

        addCheckbookBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                openDialog();
            }
        });

        addCheckbookBtn.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addCheckbookBtn.bringToFront();

        /*
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                (recyclerView1, position, v) -> {
                    Toast.makeText(CheckbookActivity.this, "position: " + adapter.getItem(position), Toast.LENGTH_LONG).show();
                    openActions();

                }
        );
        //*/

    }

    private void initNavBar() {
        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(getPrimarySoftColorInTheme());
        TextView textName = header.findViewById(R.id.textView_username);
        textName.setTextColor(getColor(R.color.white));
        imageProfile = header.findViewById(R.id.imageView_profile);

        imageProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                pickFromGallery();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrwawer, R.string.openDrwawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawers();
            Intent intent;
            switch (menuItem.getItemId()) {
                /*
                case R.id.nav_query:
                    intent = new Intent(this, CheckQueryActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_emit:
                    intent = new Intent(this, CheckEmitActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_cancel:
                    intent = new Intent(this, CheckCancelActivity.class);
                    startActivity(intent);
                    return true;
                    //*/
                case R.id.nav_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_help:
                    intent = new Intent(this, HelpActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_close:
                    intent = new Intent(this, LoginActivity.class);
                    logout(intent);
                    return true;
                default:
                    return false;
            }
        });

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

    private void setThemeByName(Intent intent) {
        String bankName = intent.getStringExtra("bankName");
        managerTheme = ManagerTheme.getInstance();

        if (bankName != null) {
            managerTheme.setBankName(bankName);
            switch (bankName) {
                case "santander":
                    setTheme(R.style.AppThemeSantander);
                    managerTheme.setThemeId(R.style.AppThemeSantander);
                    break;
                case "hsbc":
                case "scotiabank":
                case "banorte":
                case "banamex":
                    setTheme(R.style.AppThemeBanamex);
                    managerTheme.setThemeId(R.style.AppThemeBanamex);
                    break;
                case "bancomer":
                    setTheme(R.style.AppThemeBancomer);
                    managerTheme.setThemeId(R.style.AppThemeBancomer);
                    break;
                case "banbajio":
                    setTheme(R.style.AppThemeBanbajio);
                    managerTheme.setThemeId(R.style.AppThemeBanbajio);
                    break;
                case "inbursa":
                case "compartamos":
                case "bancoppel":
                default:
                    setTheme(R.style.AppThemeOther);
                    managerTheme.setThemeId(R.style.AppThemeOther);
                    break;
            }
        } else {
            Log.e("Error", "bankName is null");
            String bank = managerTheme.getBankName();
            if (bank != null) {
                intent.putExtra("bankName", bank);
                setThemeByName(intent);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.main_menu, menu);
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
                logout(intent);
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
        scanBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                readQR();
                alertDialog.dismiss();
            }
        });

        Button searchBtn = view.findViewById(R.id.button_no);
        searchBtn.setText(getString(R.string.insert_data));
        searchBtn.setBackgroundColor(getPrimaryColorInTheme());
        searchBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(v.getContext(), CheckbookAddActivity.class);
                startActivityForResult(i, 0);
                alertDialog.dismiss();
            }
        });

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
                Bundle bundle = data.getExtras();

                //SCAN_RESULT=ntI1Rct8JNC1HfTgKMnO3hRIfdckeVeybG1ACMmRJzruA8v1vzq2TW3dvgk5/58E2VFOfoceqnC4x8ckorpeTg==
                //SCAN_RESULT_BYTE_SEGMENTS_0=[B@1548ca6, SCAN_RESULT_BYTES=[B@23ea6e7
                //SCAN_RESULT_FORMAT=QR_CODE
                // SCAN_RESULT_ERROR_CORRECTION_LEVEL=M


                if (num != null)
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
        if (requestCode == PICK_IMAGE) {
            Log.i("DATA", "data: " + data);
            if (data != null && data.getData() != null) {
                imageProfile.setImageURI(data.getData());
            }
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            Log.i("DATA", "data: " + data);
            if (data != null && data.getData() != null) {
                imageProfile.setImageURI(data.getData());
            }
        }
    }

 //*/

}
