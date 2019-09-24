package com.example.calidata.activities.cancel.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.query.CheckQueryActivity;
import com.example.calidata.management.ManagerTheme;

import java.util.List;

public class RecyclerViewAdapterCancelCheck extends RecyclerView.Adapter<RecyclerViewAdapterCancelCheck.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private ManagerTheme managerTheme;
    private int themeId;

    // data is passed into the constructor
    RecyclerViewAdapterCancelCheck(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        managerTheme = ManagerTheme.getInstance();
        themeId = managerTheme.getThemeId();

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = mInflater.inflate(R.layout.check_card, parent, false);
        View view = mInflater.inflate(R.layout.card_check, parent, false);

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData.get(position);
        String bankName = managerTheme.getBankName();
        holder.myTextView.setText(animal);
        Drawable logoDrawable = getLogoDrawable(bankName);
        holder.logo.setImageDrawable(logoDrawable);
        holder.textBank.setText(getBankName());
        holder.separator.setBackgroundColor(((CheckQueryActivity) mContext).getPrimaryColorInTheme());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView logo;
        ConstraintLayout separator;
        TextView textBank;

        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            separator = itemView.findViewById(R.id.separator1);
            textBank = itemView.findViewById(R.id.textView_bank);
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

    /*
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
    //*/
    private String getBankName() {
        String bankName = managerTheme.getBankName();
        return bankName.toUpperCase();
    }

}
