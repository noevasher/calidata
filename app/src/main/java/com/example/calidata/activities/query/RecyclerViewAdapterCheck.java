package com.example.calidata.activities.query;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.CheckController;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;
import com.example.calidata.models.CheckModel;
import com.example.calidata.session.SessionManager;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressLint("CheckResult")
public class RecyclerViewAdapterCheck extends RecyclerView.Adapter<RecyclerViewAdapterCheck.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_DATA = 1;

    //private List<String> mData;
    private List<CheckModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private ManagerTheme managerTheme;
    private int themeId;
    private int cardLayout;
    private String bankName;
    private SessionManager sessionManager;

    // data is passed into the constructor
    public RecyclerViewAdapterCheck(Context context, List<CheckModel> data, int cardCancel) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        managerTheme = ManagerTheme.getInstance();
        themeId = managerTheme.getThemeId();
        bankName = managerTheme.getBankName();
        cardLayout = cardCancel;
        sessionManager = SessionManager.getInstance(context);
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.check_card, parent, false);
        View view = mInflater.inflate(cardLayout, parent, false);
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
            if (cardLayout == R.layout.card_cancel) {
                holder.emptyText.setText(R.string.empty_checks_empty_cancel);
            } else {
                holder.emptyText.setText(R.string.empty_checks_label);

            }
        } else {
            CheckModel model = mData.get(position);
            final char[] delimiters = {' ', '_'};

            String status = model.getStatus();
            String date = model.getDate();
            String description = model.getDescription();
            String quantity = "" + model.getQuantity();
            String originalCheckId = model.getCheckId();
            String checkId = model.getCheckId();
            checkId = "****" + checkId.substring(checkId.length() - 15, checkId.length() - 9);
            holder.statusText.setText(status);
            holder.dateText.setText(date);
            holder.descriptionText.setText(description);
            holder.quantityText.setText(quantity);
            holder.checkIdText.setText(checkId);
            Drawable logoDrawable = ((ParentActivity) (mContext)).getLogoDrawableByBankName(bankName);
            holder.logo.setImageDrawable(logoDrawable);
            holder.textBank.setText(WordUtils.capitalizeFully(bankName, delimiters));
            holder.separator.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
            if (holder.cancelBtn != null) {
                holder.cancelBtn.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
                holder.cancelBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                String finalCheckId = originalCheckId;
                holder.cancelBtn.setOnClickListener(v -> {
                    openDialogToDelete(finalCheckId, position);
                });
            }
        }
    }

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
            return VIEW_TYPE_DATA;

        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView logo;
        ConstraintLayout separator;
        TextView textBank;
        Button cancelBtn;
        TextView emptyText;
        TextView dateText;
        TextView statusText;
        TextView descriptionText;
        TextView quantityText;
        TextView checkIdText;

        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            separator = itemView.findViewById(R.id.separator1);
            textBank = itemView.findViewById(R.id.textView_bank);
            cancelBtn = itemView.findViewById(R.id.button_cancel);
            emptyText = itemView.findViewById(R.id.textView_empty);
            dateText = itemView.findViewById(R.id.textView_date);
            statusText = itemView.findViewById(R.id.textView_status);
            descriptionText = itemView.findViewById(R.id.textView_description);
            quantityText = itemView.findViewById(R.id.textView_quantity);
            checkIdText = itemView.findViewById(R.id.textView_folio);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public CheckModel getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void openDialogToDelete(String checkId, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((ParentActivity) mContext).getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.cancel_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        TextView label = view.findViewById(R.id.textView_label);
        label.setText(label.getText().toString() + ": " + checkId);
        Button yesBtn = view.findViewById(R.id.button_yes);
        yesBtn.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
        yesBtn.setOnClickListener(v -> {
            CheckController checkController = new CheckController(mContext);
            //removeAt(position);
            String token = ((ParentActivity) mContext).sessionManager.getToken();
            if (token != null) {
                HashMap<String, Object> body = new HashMap<>();
                body.put("ID_CheckID", checkId);
                body.put("idUsuario", sessionManager.getUserId());
                checkController.cancelCheckId(token, body).subscribe(response -> {
                    if (response.getSuccess()) {
                        removeAt(position);
                    } else {
                        Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }, throwable -> {
                    Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
            //*/
            alertDialog.dismiss();
        });

        Button noBtn = view.findViewById(R.id.button_no);
        noBtn.setBackgroundColor(mContext.getColor(R.color.white));
        noBtn.setTextColor(((ParentActivity) mContext).getPrimaryColorInTheme());
        noBtn.setOnClickListener(v -> {
            alertDialog.dismiss();

        });


        alertDialog.show();

    }

    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    void addAndSort(CheckModel model) {
        mData.add(model);
        Collections.sort(mData, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

    }

    void addAndSort(List<CheckModel> list) {
        mData.clear();
        mData.addAll(list);
        Collections.sort(mData, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

    }

    void addAndSort_(CheckModel model) {
        mData.add(model);
        Collections.sort(mData, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));

    }
}
