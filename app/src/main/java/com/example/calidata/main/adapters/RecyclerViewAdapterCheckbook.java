package com.example.calidata.main.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.management.ManagerTheme;

import java.util.List;

public class RecyclerViewAdapterCheckbook extends RecyclerView.Adapter<RecyclerViewAdapterCheckbook.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_DATA = 1;

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private int themeId;

    // data is passed into the constructor
    public RecyclerViewAdapterCheckbook(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        ManagerTheme managerTheme = ManagerTheme.getInstance();
        themeId = managerTheme.getThemeId();

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

        } else {
            String number = mData.get(position);
            Drawable logoDrawable = getLogoDrawable(themeId);
            holder.logo.setImageDrawable(logoDrawable);
            holder.textBank.setText(getBankName(themeId));
            holder.textViewCheckNumber.setText(number);
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
            Object item = mData.get(position);
            return VIEW_TYPE_DATA;

        }
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
    public String getItem(int id) {
        return mData.get(id);
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


}
