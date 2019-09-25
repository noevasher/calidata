package com.example.calidata.activities.emit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.activities.emit.fragments.FragmentQR;
import com.example.calidata.activities.emit.fragments.FragmentSearch;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckEmitActivity extends ParentActivity {
    private ZXingScannerView mScannerView;


    private FragmentSearch fragmentSearch;
    private FragmentQR fragmentQR;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.textView_checkid)
    public TextView checkIdText;

    @BindView(R.id.button_emit)
    public Button emitBtn;

    @BindView(R.id.separator1)
    public ConstraintLayout separator1;

    @BindView(R.id.editText_client_name)
    public EditText clientText;

    @BindView(R.id.editText_description)
    public EditText description;

    @BindView(R.id.editText_mount)
    public EditText mount;


    private String checkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);


        setToolbar(toolbar, getResources().getString(R.string.emit_title), true);
        checkId = (String) getIntent().getExtras().get("checkId");
        checkIdText.setText(checkId);
        //initToolbar();
        emitBtn.setBackgroundColor(getPrimaryColorInTheme());
        emitBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                CheckbookController controller = new CheckbookController(CheckEmitActivity.this);
                String token = sessionManager.getToken();
                if (token != null) {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("ID_CheckID", checkId);
                    body.put("monto", mount.getText().toString());
                    String descriptionS = description.getText().toString();
                    String clientName = clientText.getText().toString();

                    //if(!descriptionS.isEmpty())
                        body.put("Descripcion", descriptionS);
                    //if(!clientName.isEmpty())
                        body.put("Beneficiario", clientName);


                    controller.emitCheckId(token, body).subscribe(response -> {
                        response.getData();
                        if (response.getSuccess() && response.getMessage().equals("OK")) {
                            Toast.makeText(CheckEmitActivity.this, "Cheque: "
                                    + checkId + " " + getString(R.string.success_emit_check), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(CheckEmitActivity.this, "Cheque: " +
                                    checkId + getString(R.string.error_emit_check), Toast.LENGTH_LONG).show();
                        }
                    }, t -> {
                        Toast.makeText(CheckEmitActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                    });
                }

                //*/
            }
        });

        separator1.setBackgroundColor(getPrimaryColorInTheme());
    }


}
