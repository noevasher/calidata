package com.example.calidata.activities.emit.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.emit.CheckEmitActivity;
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;

import java.util.List;

public class RecyclerViewAdapterEmit extends RecyclerView.Adapter<RecyclerViewAdapterEmit.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_DATA = 1;

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private ManagerTheme managerTheme;
    private int themeId;
    private Activity mActivity;

    // data is passed into the constructor
    public RecyclerViewAdapterEmit(Activity activity, List<String> data) {
        this.mInflater = LayoutInflater.from(activity.getApplicationContext());
        this.mData = data;
        this.mContext = activity.getApplicationContext();
        managerTheme = ManagerTheme.getInstance();
        themeId = managerTheme.getThemeId();
        mActivity = activity;

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.card_emit, parent, false);
        if (viewType == VIEW_TYPE_EMPTY) {
            view = mInflater.inflate(R.layout.card_checkbook_empty, parent, false);
        }
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        String bankName = managerTheme.getBankName();
        if (viewType == VIEW_TYPE_EMPTY) {
            holder.emptyText.setText(R.string.empty_checks_active);
        } else {
            Drawable logoDrawable = getLogoDrawable(bankName);

            int currentTextColor = holder.optionText.getCurrentTextColor();
            ColorFilter currentIconColor = holder.iconEdit.getColorFilter();

            holder.logo.setImageDrawable(logoDrawable);
            holder.textBank.setText(getBankName());
            holder.separator.setBackgroundColor(((CheckEmitActivity) mActivity).getPrimaryColorInTheme());
            holder.emitBtn.setBackgroundColor(((CheckEmitActivity) mActivity).getPrimaryColorInTheme());
            holder.emitBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.panelDownUp.setOnClickListener(v -> {
                ImageView image = v.findViewById(R.id.imageView_arrow);
                int visibility = holder.expandPanel.getVisibility();
                if (visibility == View.GONE) {
                    image.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_arrow_up_24px));
                    holder.expandPanel.setVisibility(View.VISIBLE);
                    holder.optionText.setText(mContext.getResources().getString(R.string.hide_options_label));
                    holder.optionText.setTextColor(((CheckEmitActivity) mActivity).getPrimaryColorInTheme());
                    holder.iconEdit.setColorFilter(((CheckEmitActivity) mActivity).getPrimaryColorInTheme(), PorterDuff.Mode.SRC_IN);

                } else {
                    image.setImageDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_arrow_down_24px));
                    holder.expandPanel.setVisibility(View.GONE);
                    holder.optionText.setText(mContext.getResources().getString(R.string.see_options_label));
                    holder.optionText.setTextColor(currentTextColor);
                    holder.iconEdit.setColorFilter(currentIconColor);

                }


            });
            holder.emitBtn.setOnClickListener(v -> {
                openDialog();
            });
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button emitBtn;
        private ImageView logo;
        private ConstraintLayout separator;
        private TextView textBank;
        private ConstraintLayout panelDownUp;
        private ConstraintLayout expandPanel;
        private TextView optionText;
        private ImageView iconEdit;
        private TextView emptyText;

        ViewHolder(View itemView) {
            super(itemView);
            emitBtn = itemView.findViewById(R.id.button_emit);
            logo = itemView.findViewById(R.id.logo);
            separator = itemView.findViewById(R.id.separator1);
            textBank = itemView.findViewById(R.id.textView_bank);
            panelDownUp = itemView.findViewById(R.id.constraintLayout_collapse);
            expandPanel = itemView.findViewById(R.id.constraintLayout_expand);
            optionText = itemView.findViewById(R.id.textView_options);
            iconEdit = itemView.findViewById(R.id.imageView_icon_edit);
            emptyText = itemView.findViewById(R.id.textView_empty);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
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


    private Drawable getLogoDrawable(String bankName) {

        switch (bankName) {
            case "santander":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_santander_logo);
            case "hsbc":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_hsbc_logo);
            case "scotiabank":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_scotiabank_logo);
            case "banorte":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_banorte_logo);
            case "autofin":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
            case "bansefi":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
            case "citibanamex":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_citibanamex_logo);
            case "bbva bancomer":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_bancomer_logo);
            case "famsa":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
            case "bancoppel":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
            case "monex":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
            case "compartamos":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_compartamos_logo);
            case "banbajio":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_banbajio_logo);
            case "inbursa":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_inbursa_logo);
            case "actinver":
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);

            default:
                return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);

        }
    }

    private String getBankName() {
        String bankName = managerTheme.getBankName();
        return bankName.toUpperCase();
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

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.cancel_dialog, null);
        TextView label = view.findViewById(R.id.textView_label);
        label.setText(mContext.getResources().getString(R.string.emit_delete_label));
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        Button yesBtn = view.findViewById(R.id.button_yes);
        yesBtn.setBackgroundColor(((ParentActivity) mActivity).getPrimaryColorInTheme());
        yesBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        Button noBtn = view.findViewById(R.id.button_no);
        noBtn.setBackgroundColor(mActivity.getColor(R.color.white));
        noBtn.setTextColor(((ParentActivity) mActivity).getPrimaryColorInTheme());
        noBtn.setOnClickListener(v -> {
            alertDialog.dismiss();

        });


        alertDialog.show();

/*
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    //*/

    }
}
