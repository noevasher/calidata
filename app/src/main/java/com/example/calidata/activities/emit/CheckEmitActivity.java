package com.example.calidata.activities.emit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.activities.emit.fragments.FragmentQR;
import com.example.calidata.activities.emit.fragments.FragmentSearch;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.main.controllers.CheckbookController;
import com.example.calidata.management.ManagerTheme;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

@SuppressLint("CheckResult")
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

    @BindView(R.id.textView_photo_name)
    public TextView photoName;

    @BindView(R.id.textView_bank)
    public TextView bankNameText;

    @OnClick(R.id.imageView_camera)
    public void addPhoto() {
        showPictureDialog(null);
    }

    @BindView(R.id.imageView_delete_photo)
    public ImageView deletePhotoImg;

    private Integer userId;

    private String checkId;
    private OnSingleClickListener clickListener;
    private String encodedImageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerTheme = ManagerTheme.getInstance();
        setTheme(managerTheme.getThemeId());
        setContentView(R.layout.emit_activity);
        ButterKnife.bind(this);
        userId = sessionManager.getUserId();
        bankNameText.setText(sessionManager.getBankName());

        deletePhotoImg.setColorFilter(getColor(R.color.colorDelete), PorterDuff.Mode.SRC_IN);

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
                if (token != null && !isEmptyField(mount)) {
                    HashMap<String, Object> body = new HashMap<>();
                    body.put("ID_CheckID", checkId);
                    body.put("idUsuario", userId);
                    body.put("monto", mount.getText().toString());
                    body.put("image", encodedImageData);
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
                        if (t.getMessage().equals("Unauthorized")) {
                            Toast.makeText(CheckEmitActivity.this, getString(R.string.start_session), Toast.LENGTH_LONG).show();
                            logout();
                        } else {
                            Log.e("", t.getMessage());
                            Toast.makeText(CheckEmitActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
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
