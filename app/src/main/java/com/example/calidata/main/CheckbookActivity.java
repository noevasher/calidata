package com.example.calidata.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.utilities.HelpActivity;
import com.example.calidata.utilities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("CheckResult")
public class CheckbookActivity extends ParentActivity {

    @BindView(R.id.drawer)
    public DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.floatingActionButton)
    public ImageView addCheckbookBtn;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    RecyclerViewAdapterCheckbook adapter;

    //private ArrayList<String> checkbooks;
    private ArrayList<CheckbookModel> checkbooksList;

    public CircleImageView imageProfile;
    private CheckbookController controller;
    private Double userId;
    private TextView textName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        //setTheme(intent);
        setThemeByName(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbook);
        ButterKnife.bind(this);
        controller = new CheckbookController(this);
        String title = getResources().getString(R.string.checkbook_title);
        setToolbar(toolbar, title, false);
        userId = sessionManager.getUserId();

        initNavBar();
        checkbooksList = new ArrayList<>();


        // set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //adapter = new RecyclerViewAdapterCheckbook(this, checkbooks);
        adapter = new RecyclerViewAdapterCheckbook(this, checkbooksList);

        recyclerView.setAdapter(adapter);

        addCheckbookBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //openDialog();
                progressBar.setVisibility(View.VISIBLE);
                //readQR();
                String token = sessionManager.getToken();
                String checkId = "452185E8-3E69-484D-AC1D-AA97B9C59B4B-52-0-12";
                checkId = "A2DCBC76-99A3-4F2E-9916-733E5999BEA6-52-00-116";

                if (token != null) {
                    String finalCheckId = checkId;
                    controller.addCheckbook(token, checkId, userId.intValue()).subscribe(response -> {
                        //if(response.getData() != null && response.getData().isEmpty()){
                        //HashMap<String, Object> dataResponse = new HashMap<>();
                        if (response.getMessage().equals("Ok")) {
                            HashMap<String, Object> dataResponse = response.getData();
                            CheckbookModel checkbookModel = new CheckbookModel();
                            checkbookModel.setTypeDoc("00");
                            //checkbookModel.setCheckId("" + dataResponse.get("checkbookId"));
                            //checkbookModel.setCheckbookId("" + dataResponse.get("checkbookId"));
                            checkbookModel.setCheckId(finalCheckId);
                            checkbookModel.setCheckbookId(finalCheckId);
                            checkbooksList.add(checkbookModel);

                            adapter.notifyItemInserted(checkbooksList.size());

                            recyclerView.postDelayed(() -> {
                                recyclerView.scrollToPosition(checkbooksList.size() - 1);
                            }, 500);

                        }
                        else{
                            Toast.makeText(CheckbookActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);



                    }, t -> {
                        Log.e("", t.getMessage());
                    });
                    //*/
                } else {
                    Toast.makeText(CheckbookActivity.this, getString(R.string.error_invalid_auth), Toast.LENGTH_LONG).show();
                }

            }
        });

        addCheckbookBtn.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addCheckbookBtn.bringToFront();
        progressBar.bringToFront();

        //Get All checkbooks
        readCheckBooks();

    }

    private void readCheckBooks() {
        if (userId != 0) {
            String token = sessionManager.getToken();
            controller.getCheckbooks(token, userId.intValue()).subscribe(response -> {
                List<HashMap<String, Object>> data = response.getData();
                for (HashMap<String, Object> checkbook : data) {
                    //Log.i("", checkbook.toString());
                    CheckbookModel checkbookModel = new CheckbookModel();
                    String checkbookId = (String) checkbook.get("iD_CheckID");
                    Log.i("", checkbookId);
                    checkbookModel.setCheckbookId(checkbookId);
                    checkbookModel.setCheckId(checkbookId);
                    checkbookModel.setTypeDoc("00");
                    checkbooksList.add(checkbookModel);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                recyclerView.postDelayed(() -> {
                    recyclerView.scrollToPosition(checkbooksList.size() - 1);
                }, 500);

            }, t -> {
                if (t.getMessage().equals("Unauthorized")) {
                    Toast.makeText(CheckbookActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                    logout();
                } else {
                    Toast.makeText(CheckbookActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
                //logout();

            });
        }
    }

    private String hideCheckId(String checkId) {
        int size = checkId.length();
        String hidden = "";
        String startSubString = checkId.substring(0, size - 4);
        String endSubString = checkId.substring(size - 4, size);
        for (int i = 0; i < size - 4; i++) {
            hidden += "*";
        }
        return hidden + endSubString;
    }

    private void initNavBar() {
        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(getPrimarySoftColorInTheme());
        textName = header.findViewById(R.id.textView_username);
        textName.setTextColor(getColor(R.color.white));
        imageProfile = header.findViewById(R.id.imageView_profile);


        if (sessionManager.getKeyUsername() != null) {
            textName.setText(sessionManager.getKeyUsername());
        }
        if (sessionManager.getKeyImage64() != null) {
            putImage(sessionManager.getKeyImage64(), imageProfile);

        }
        //llamar servicio de informacion de usuario
        /*
        controller.getUserInformation(userId).subscribe(response ->{
            textName.setText(response.getUserName());
            String image64 = response.getImage64();
            putImage(image64, imageProfile);
        });
        //*/


        imageProfile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //pickFromGallery();
                Intent intent = new Intent(CheckbookActivity.this, ImageViewActivity.class);
                startActivity(intent);
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
                case R.id.nav_settings:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_help:
                    intent = new Intent(this, HelpActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_close:
                    logout();
                    return true;
                default:
                    return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
                logout();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");


                if (contents != null) {
                    String token = sessionManager.getToken();
                    String checkId = "452185E8-3E69-484D-AC1D-AA97B9C59B4B-52-0-12";
                    checkId = "21BBB2D8-0D91-4C71-8A80-BF85498446A1-52-00-116";

                    if (token != null) {
                        controller.addCheckbook(token, checkId, userId.intValue()).subscribe(response -> {
                            //if(response.getData() != null && response.getData().isEmpty()){
                            HashMap<String, Object> dataResponse = new HashMap<>();
                            //HashMap<String, Object> dataResponse = response.getData();
                            if (data != null) {
                                CheckbookModel checkbookModel = new CheckbookModel();
                                checkbookModel.setTypeDoc("00");
                                checkbookModel.setCheckId("" + dataResponse.get("checkbookId"));
                                checkbookModel.setCheckbookId("" + dataResponse.get("checkbookId"));
                                checkbooksList.add(checkbookModel);

                                adapter.notifyItemInserted(checkbooksList.size());

                                recyclerView.postDelayed(() -> {
                                    recyclerView.scrollToPosition(checkbooksList.size() - 1);
                                }, 500);
                                progressBar.setVisibility(View.GONE);

                            }

                            //}


                        }, t -> {
                            Log.e("", t.getMessage());
                        });
                        //*/
                    } else {
                        Toast.makeText(CheckbookActivity.this, getString(R.string.error_invalid_auth), Toast.LENGTH_LONG).show();
                    }
                }
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
                Log.i("TAG-QR", "CANCELADO");
            }
        }
        if (requestCode == EMIT_CODE) {
            Log.i("DATA", "data: " + data);
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (contents != null) {
                    String token = sessionManager.getToken();
                    String checkId = "452185E8-3E69-484D-AC1D-AA97B9C59B4B-52-0-12";
                    if (token != null) {
                        controller.emitCheckId(token, checkId).subscribe(response -> {
                            response.getData();
                            if (response.getSuccess() && response.getMessage().equals("Ok")) {
                                Toast.makeText(CheckbookActivity.this, "Cheque: "
                                        + checkId + " " + getString(R.string.success_emit_check), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(CheckbookActivity.this, "Cheque: " +
                                        checkId + getString(R.string.error_emit_check), Toast.LENGTH_LONG).show();
                            }
                        }, t -> {
                            Toast.makeText(CheckbookActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        });
                    }
                }
            }
        }
        if (requestCode == PICK_IMAGE) {
            Log.i("DATA", "data: " + data);
            if (data != null && data.getData() != null) {
                imageProfile.setImageURI(data.getData());
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    private String hex2String(String hexString) throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
        return new String(bytes, "UTF-8");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SettingsActivity.imageObs != null) {
            SettingsActivity.imageObs.subscribe(response -> {
                if (response != null) {
                    putImage(response, imageProfile);
                }
            });
        }
        if (SettingsActivity.usernameObs != null) {
            SettingsActivity.usernameObs.subscribe(response -> {
                if (response != null) {
                    textName.setText(response);
                }
            });
        }

    }
}
