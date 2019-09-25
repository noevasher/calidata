package com.example.calidata.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;
import com.example.calidata.activities.cancel.CheckCancelActivity;
import com.example.calidata.activities.emit.CheckEmitActivity;
import com.example.calidata.activities.query.CheckQueryActivity;
import com.example.calidata.main.CheckbookActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckbookModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterCheckbook extends RecyclerView.Adapter<RecyclerViewAdapterCheckbook.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_DATA = 1;
    private static final int EMIT_CODE = 2;

    //private List<String> mData;
    private static List<CheckbookModel> mData;
    //private List<CheckbookModel> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private String bankName;

    public RecyclerViewAdapterCheckbook(Context context, List<CheckbookModel> data) {
        this.mInflater = LayoutInflater.from(context);
        mData = data;
        this.mContext = context;
        ManagerTheme managerTheme = ManagerTheme.getInstance();
        bankName = managerTheme.getBankName();

    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.check_card, parent, false);
        View view = mInflater.inflate(R.layout.card_checkbook, parent, false);
        //ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_EMPTY) {
            view = mInflater.inflate(R.layout.card_checkbook_empty, parent, false);
            //return viewHolder = new ViewHolder(view);
        }

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_EMPTY) {
            //ProgressBar progressBar = ((CheckbookActivity)mContext).progressBar;
            //progressBar.setVisibility(View.VISIBLE);
            //holder.textBank.setVisibility(View.GONE);

        } else {
            String checkbookId = mData.get(position).getCheckId();
            String checkbookIdCut = checkbookId.substring(0, checkbookId.length() - 10);

            //Drawable logoDrawable = getLogoDrawable(themeId);
            Drawable logoDrawable = ((ParentActivity) (mContext)).getLogoDrawableByBankName(bankName);
            holder.logo.setImageDrawable(logoDrawable);
            //holder.textBank.setText(getBankName(themeId));
            String output = bankName.substring(0, 1).toUpperCase() + bankName.substring(1);

            holder.textBank.setText(output);
            holder.textViewCheckNumber.setText(checkbookIdCut);
            String finalCheckbookId = checkbookId;
            holder.itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    openActions(finalCheckbookId);
                }
            });
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (mData.size() == 0) {
            return 1;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            //Object item = mData.get(position);
            return VIEW_TYPE_DATA;

        }
    }

    public void setList(ArrayList<CheckbookModel> checkbooksList) {
        mData = checkbooksList;
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView textBank;
        TextView textViewCheckNumber;

        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            textBank = itemView.findViewById(R.id.textView_empty);
            textViewCheckNumber = itemView.findViewById(R.id.textView_check_number);

        }
    }

    // convenience method for getting data at click position
    public CheckbookModel getItem(int id) {
        return mData.get(id);
    }


    private void openActions(String checkbookId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((CheckbookActivity) mContext).getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
        ConstraintLayout viewToolbar = view.findViewById(R.id.constraintLayout_toolbar);
        TextView titleText = view.findViewById(R.id.textView_title_toolbar);
        ImageView closeWindow = view.findViewById(R.id.imageView_close);

        builder.setView(view);
        Toolbar actionToolbar = view.findViewById(R.id.toolbar);

        String title = mContext.getString(R.string.main_title);

        titleText.setText(title);
        viewToolbar.setBackgroundColor(((CheckbookActivity) mContext).getPrimaryColorInTheme());

        AlertDialog alertDialog = builder.create();
        setImagesListeners(view, checkbookId, alertDialog);
        closeWindow.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                alertDialog.dismiss();
            }
        });
        //*/

        alertDialog.show();

    }


    private void openCardEmit(String checkbookId) {
        Intent intent = new Intent(mContext, CheckEmitActivity.class);
        mContext.startActivity(intent);
    }


    private void setImagesListeners(View view, String checkbookId, AlertDialog alertDialog) {
        ImageView imageViewQuery = view.findViewById(R.id.imageView_query);
        ImageView imageViewEmit = view.findViewById(R.id.imageView_emit);
        ImageView imageViewCancel = view.findViewById(R.id.imageView_cancel);

        imageViewQuery.setColorFilter(((CheckbookActivity) mContext).getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        imageViewEmit.setColorFilter(((CheckbookActivity) mContext).getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);
        imageViewCancel.setColorFilter(((CheckbookActivity) mContext).getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

        imageViewQuery.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(mContext, CheckQueryActivity.class);
                intent.putExtra("checkbookId", checkbookId);
                mContext.startActivity(intent);
            }
        });

        imageViewEmit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                readCheckQR();

                alertDialog.dismiss();
            }
        });

        imageViewCancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(mContext, CheckCancelActivity.class);
                intent.putExtra("checkbookId", checkbookId);
                mContext.startActivity(intent);

            }
        });

    }

    private void readCheckQR() {

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            ((CheckbookActivity) mContext).startActivityForResult(intent, EMIT_CODE);
        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            mContext.startActivity(marketIntent);

        }
    }

}
