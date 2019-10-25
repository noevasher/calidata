package com.example.calidata.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.activities.CheckController;
import com.example.calidata.activities.emit.CheckEmitActivity;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.models.CheckModel;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.utilities.HelpActivity;
import com.example.calidata.utilities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("CheckResult")
public class CheckbookActivity extends ParentActivity {

    private static final int LOCATION_CODE = 1111;
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
    private Integer userId;
    private TextView textName;
    private RecyclerView recyclerView;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        setThemeByName(intent);
        System.out.println("onCreate Checkbook");

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
                readQR();

            }
        });

        addCheckbookBtn.setColorFilter(getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        addCheckbookBtn.bringToFront();
        progressBar.bringToFront();
        //Get All checkbooks
        readCheckBooks();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy Checkbook");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause Checkbook");

    }


    private void readCheckBooks() {
        if (userId != 0) {
            String token = sessionManager.getToken();
            System.out.println("token" + token);
            System.out.println("controller: " + controller);
            System.out.println("userId: " + userId);

            if (controller != null) {
                controller.getCheckbooks(token, userId).subscribe(response -> {
                    List<HashMap<String, Object>> data = response.getData();
                    for (HashMap<String, Object> checkbook : data) {
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
                    recyclerView.setVisibility(View.VISIBLE);
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

    }

    private void initNavBar() {
        View header = navigationView.getHeaderView(0);
        header.setBackgroundColor(getPrimarySoftColorInTheme());
        textName = header.findViewById(R.id.textView_username);
        textName.setTextColor(getColor(R.color.white));
        imageProfile = header.findViewById(R.id.imageView_profile);


        if (sessionManager.getKeyUsername() != null) {
            textName.setText(sessionManager.getKeyUsername());
        } else {
            controller.getUserInformation(sessionManager.getUserId()).subscribe(response -> {
                String username = response.getUserName();
                textName.setText(username);
                sessionManager.saveProfileName(username);
            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
        if (sessionManager.getKeyImage64() != null) {
            putImage(sessionManager.getKeyImage64(), imageProfile);

        } else {
            controller.getUserInformation(userId).subscribe(response -> {
                textName.setText(response.getUserName());
                String image64 = response.getImage64();
                putImage(image64, imageProfile);
            }, t -> {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
            });
        }


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
                case R.id.nav_profile:
                    intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.nav_terms:
                    intent = new Intent(this, HelpActivity.class);
                    intent.putExtra("title", "Terminos y Condiciones");
                    intent.putExtra("fragment", 0);

                    startActivity(intent);
                    return true;
                case R.id.nav_privacy:
                    intent = new Intent(this, HelpActivity.class);
                    intent.putExtra("title", "Privacidad");
                    intent.putExtra("fragment", 1);

                    startActivity(intent);
                    return true;
                case R.id.nav_contact:
                    intent = new Intent(this, HelpActivity.class);
                    intent.putExtra("title", "Contacto");
                    intent.putExtra("fragment", 2);

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

    public void readQR() {
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

    private String token;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult in checkbookActiviy");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String checkId = data.getStringExtra("SCAN_RESULT");


                if (checkId != null) {
                    token = sessionManager.getToken();
                    if (token != null) {
                        controller.addCheckbook(token, checkId, userId).subscribe(response -> {
                            if (response.getSuccess()) {
                                CheckbookModel checkbookModel = new CheckbookModel();
                                checkbookModel.setTypeDoc("00");
                                checkbookModel.setCheckId(checkId);
                                checkbookModel.setCheckbookId(checkId);
                                checkbooksList.add(checkbookModel);

                                adapter.notifyItemInserted(checkbooksList.size());

                                recyclerView.postDelayed(() -> {
                                    recyclerView.scrollToPosition(checkbooksList.size() - 1);
                                }, 500);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(CheckbookActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();

                            }

                        }, t -> {
                            if (t.getMessage().equals("Unauthorized")) {
                                Toast.makeText(CheckbookActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                                logout();
                            } else {
                                Log.e("", t.getMessage());
                                Toast.makeText(CheckbookActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
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
                String checkId = data.getStringExtra("SCAN_RESULT");
                token = sessionManager.getToken();
                if (token != null) {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("idUsuario", sessionManager.getUserId());
                    body.put("ID_CheckId", checkId);

                    controller.getCheckById(token, body).subscribe(response -> {
                        List<HashMap<String, Object>> checkData = response.getData();
                        if (!checkData.isEmpty()) {
                            HashMap<String, Object> item = checkData.get(0);
                            CheckModel check = loadChecks(item);
                            if (response.getMessage().equals("OK") && check.getStatus() != null
                                    && check.getStatus().equals("Activado")) {
                                Intent intent = new Intent(CheckbookActivity.this,
                                        CheckEmitActivity.class);
                                intent.putExtra("checkId", checkId);
                                intent.putExtra("status", check.getStatus());

                                CheckbookActivity.this.startActivity(intent);
                            } else {
                                Toast.makeText(CheckbookActivity.this, getString(R.string.error_not_available_emit_check), Toast.LENGTH_LONG).show();

                            }
                        }
                    }, t -> {
                        Toast.makeText(CheckbookActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    });

                }
            }
        }
        if (requestCode == CANCEL_CODE) {
            Log.i("DATA", "data: " + data);
            if (resultCode == RESULT_OK) {
                String checkId = data.getStringExtra("SCAN_RESULT");
                token = sessionManager.getToken();
                if (token != null) {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("idUsuario", sessionManager.getUserId());
                    body.put("ID_CheckId", checkId);
                    CheckController checkController = new CheckController(this);

                    controller.getCheckById(token, body).subscribe(response -> {
                        List<HashMap<String, Object>> checkData = response.getData();
                        if (!checkData.isEmpty()) {
                            HashMap<String, Object> item = checkData.get(0);
                            CheckModel check = loadChecks(item);

                            checkController.cancelCheckId(token, body).subscribe(responseCancel -> {
                                if (responseCancel.getMessage().equals("OK") && check.getStatus() != null && check.getStatus().equals("Activo")) {
                                    Toast.makeText(CheckbookActivity.this, "Cheque " +
                                            "Eliminado Correctamente", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(CheckbookActivity.this, responseCancel.getMessage(),
                                            Toast.LENGTH_LONG).show();

                                }
                            }, throwable -> {
                                Toast.makeText(CheckbookActivity.this, throwable.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                        }

                    }, t -> {
                        Toast.makeText(CheckbookActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });
                }
            }
        }
        if (requestCode == PICK_IMAGE) {
            Log.i("DATA", "data: " + data);
            if (data != null && data.getData() != null) {
                imageProfile.setImageURI(data.getData());
            }
        } else {
            Log.w("DATA", "request not match: " + requestCode);

        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume Checkbook");

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
        validLocationPermission();
    }


}
