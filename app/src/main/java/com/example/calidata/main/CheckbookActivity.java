package com.example.calidata.main;

import android.content.Intent;
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
import android.widget.TextView;

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
import com.example.calidata.login.LoginActivity;
import com.example.calidata.login.managment.RSA;
import com.example.calidata.main.adapters.RecyclerViewAdapterCheckbook;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.models.CheckbookModel;
import com.example.calidata.utilities.HelpActivity;
import com.example.calidata.utilities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    private CheckbookController controller;
    private Double userId;
    private TextView textName;
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
        //readCheckBooks();

    }

    private void readCheckBooks() {
        if (userId != 0) {
            controller.getCheckbooks(userId).subscribe(response -> {
                for (CheckbookModel checkbookModel : response) {
                    checkbooks.add(checkbookModel.getCheckId());
                }

            });
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                String num = data.getStringExtra("NUM");
                Bundle bundle = data.getExtras();

                /////////////////////////////////////////
                try {
                    RSA rsa = new RSA();

                    System.out.println("contents: " + "686f6c61206d69206e6f6d627265206573206e6f65");
                    String result = hex2String("686f6c61206d69206e6f6d627265206573206e6f65");
                    System.out.println("Result String: " + result);

                    //result = rsa.encrypt("15279355-FF18-4264-ADC7-000BE28B0C15-52-00-014");

                    System.out.println("encriptacion: " + result);
                    System.out.println("Desencriptacion: " + rsa.decrypt("u\u0093T\u0013í_\u0098ö\u0012;Æj\u0080L*Q!§\u0087\u000B1M^\u000ERØ\u0093mÄ^è&¾\u007F{g\u0018$Ó>ô,\u0004°?*Î[ñmå«\u008Fc$hGÉ\u0098¥¢\u0017Äq\u0080áÆñ\u0080ü9\u0017mþÚy\u009Ez2åü;/|ÙµÀ;·8¸ÿ$\u001B\u001E\u009B\u0081&µç¾+\u0001\u008F\u000BQ¹\u0004ná\u0092\u0092PR,ó\u001AøO\u001D\u0005¸£a\u008C\u0007Õ´"));
                    rsa.decrypt(contents);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (DecoderException e) {
                    e.printStackTrace();
                }

                ///////////////////////////////////////////

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

    private String hex2String(String hexString) throws DecoderException, UnsupportedEncodingException {
        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
        return new String(bytes, "UTF-8");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(SettingsActivity.imageObs!= null) {
            SettingsActivity.imageObs.subscribe(response -> {
                if (response != null) {
                    putImage(response, imageProfile);
                }
            });
        }
        if(SettingsActivity.usernameObs != null) {
            SettingsActivity.usernameObs.subscribe(response -> {
                if (response != null) {
                    textName.setText(response);
                }
            });
        }

    }
}
