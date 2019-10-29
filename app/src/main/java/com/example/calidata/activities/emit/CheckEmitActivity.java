package com.example.calidata.activities.emit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;

import java.text.DecimalFormatSymbols;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.MediaRecorder.VideoSource.CAMERA;

@SuppressLint("CheckResult")
public class CheckEmitActivity extends ParentActivity {
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

    @BindView(R.id.textView_photo_name)
    public TextView photoName;

    @BindView(R.id.textView_bank)
    public TextView bankNameText;

    @OnClick(R.id.imageView_camera)
    public void addPhoto() {
        showPictureDialog(null, false);
    }

    @BindView(R.id.imageView_delete_photo)
    public ImageView deletePhotoImg;

    @BindView(R.id.textView_status)
    public TextView statusText;

    private Integer userId;

    @Override
    protected void onResume() {
        super.onResume();
        validLocationPermission();

    }

    private String checkId;
    private OnSingleClickListener clickListener;
    private String encodedImageData;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);
        userId = sessionManager.getUserId();
        bankNameText.setText(sessionManager.getBankName());

        //mount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
        mount.setFilters(new InputFilter[]{new InputFilter() {

            DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                int indexPoint = dest.toString().indexOf(decimalFormatSymbols.getDecimalSeparator());

                if (indexPoint == -1)
                    return source;

                int decimals = dend - (indexPoint + 1);
                return decimals < 2 ? source : "";
            }
        }
        });

        deletePhotoImg.setColorFilter(getColor(R.color.colorDelete), PorterDuff.Mode.SRC_IN);

        setToolbar(toolbar, getResources().getString(R.string.emit_title), true);
        checkId = (String) getIntent().getExtras().get("checkId");
        status = (String) getIntent().getExtras().get("status");
        String checkbookIdCut = checkId.substring(checkId.length() - 6);

        checkIdText.setText("**" + checkbookIdCut);
        statusText.setText(status);
        //initToolbar();
        emitBtn.setBackgroundColor(getPrimaryColorInTheme());
        emitBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                CheckbookController controller = new CheckbookController(CheckEmitActivity.this);
                String token = sessionManager.getToken();
                if (token != null && !isEmptyField(mount)) {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("ID_CheckID", checkId);
                    body.put("idUsuario", userId);
                    body.put("monto", mount.getText().toString());
                    body.put("Imagen64", encodedImageData);
                    String descriptionS = description.getText().toString();
                    String clientName = clientText.getText().toString();

                    //if(!descriptionS.isEmpty())
                    body.put("descripcion", descriptionS);
                    //if(!clientName.isEmpty())
                    body.put("beneficiario", clientName);


                    controller.emitCheckId(token, body).subscribe(response -> {

                        response.getData();
                        if (response.getSuccess() && response.getMessage().equals("OK")) {
                            Toast.makeText(CheckEmitActivity.this, "Cheque: "
                                    + getString(R.string.success_emit_check), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(CheckEmitActivity.this, "Cheque: "
                                    + getString(R.string.error_emit_check), Toast.LENGTH_LONG).show();
                        }
                        finish();

                    }, t -> {
                        if (t.getMessage().equals("Unauthorized")) {
                            Toast.makeText(CheckEmitActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                            logout();
                        } else {
                            Log.e("", t.getMessage());
                            Toast.makeText(CheckEmitActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    displayEmptyField(mount);
                }

                //*/
            }
        });

        separator1.setBackgroundColor(getPrimaryColorInTheme());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clickListener = null;
        if (resultCode == this.RESULT_CANCELED) {
            return;
        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            thumbnail = compressImage(thumbnail);

            //imageProfile.setImageBitmap(thumbnail);
            //imageProfile.buildDrawingCache();
            encodedImageData = getEncoded64ImageStringFromBitmap(thumbnail);
            photoName.setText("foto_cheque.jpg");
            deletePhotoImg.setVisibility(View.VISIBLE);
            clickListener = new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    encodedImageData = null;
                    photoName.setText("");
                    deletePhotoImg.setVisibility(View.GONE);
                }
            };
            deletePhotoImg.setOnClickListener(clickListener);

            //loadToSave(null, encodedImageData);
            //saveImage(thumbnail);
        }
    }

}
