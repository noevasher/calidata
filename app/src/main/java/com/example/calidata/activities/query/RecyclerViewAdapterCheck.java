package com.example.calidata.activities.query;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.example.calidata.main.ParentActivity;
import com.example.calidata.management.ManagerTheme;

import java.util.List;

public class RecyclerViewAdapterCheck extends RecyclerView.Adapter<RecyclerViewAdapterCheck.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_DATA = 1;

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private ManagerTheme managerTheme;
    private int themeId;
    private int cardLayout;

    // data is passed into the constructor
    public RecyclerViewAdapterCheck(Context context, List<String> data, int cardCancel) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        managerTheme = ManagerTheme.getInstance();
        themeId = managerTheme.getThemeId();
        cardLayout = cardCancel;
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
            String animal = mData.get(position);

            holder.myTextView.setText(animal);
            Drawable logoDrawable = getLogoDrawable(themeId);
            holder.logo.setImageDrawable(logoDrawable);
            holder.textBank.setText(getBankName(themeId));
            holder.separator.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
            if (holder.cancelBtn != null) {
                holder.cancelBtn.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
                holder.cancelBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.cancelBtn.setOnClickListener(v -> {
                    openDialog(animal);
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
        TextView myTextView;
        ImageView logo;
        ConstraintLayout separator;
        TextView textBank;
        Button cancelBtn;
        TextView emptyText;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView_name);
            logo = itemView.findViewById(R.id.logo);
            separator = itemView.findViewById(R.id.separator1);
            textBank = itemView.findViewById(R.id.textView_bank);
            cancelBtn = itemView.findViewById(R.id.button_cancel);
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


    private Drawable getLogoDrawable(int themeId) {

        switch (themeId) {
            case R.style.AppThemeBanamex:
                Log.i("TAG", "tema banamex");
                return ContextCompat.getDrawable(mContext, R.drawable.ic_citibanamex_logo);
            case R.style.AppThemeSantander:
                Log.i("TAG", "tema Santander");
                return ContextCompat.getDrawable(mContext, R.drawable.ic_santander_logo);

            case R.style.AppThemeBancomer:
                Log.i("TAG", "tema Bancomer");
                return ContextCompat.getDrawable(mContext, R.drawable.ic_bancomer_logo);

            case R.style.AppTheme:
                Log.i("TAG", "tema default");
                break;
            default:
                break;
        }
        return ContextCompat.getDrawable(mContext, R.drawable.ic_default_logo);
    }

    private String getBankName(int themeId) {

        switch (themeId) {
            case R.style.AppThemeBanamex:
                Log.i("TAG", "tema banamex");
                return "Banamex";
            case R.style.AppThemeSantander:
                Log.i("TAG", "tema Santander");
                return "Santander";

            case R.style.AppThemeBancomer:
                Log.i("TAG", "tema Bancomer");
                return "Bancomer";

            case R.style.AppTheme:
                Log.i("TAG", "tema default");
                break;
            default:
                break;
        }
        return "Otro Banco";
    }


    private void openDialog(String data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((ParentActivity) mContext).getLayoutInflater();
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.cancel_dialog, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        TextView label = view.findViewById(R.id.textView_label);
        label.setText(label.getText().toString() + ": " + data);
        Button yesBtn = view.findViewById(R.id.button_yes);
        yesBtn.setBackgroundColor(((ParentActivity) mContext).getPrimaryColorInTheme());
        yesBtn.setOnClickListener(v -> {
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

}
