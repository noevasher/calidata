package com.example.calidata.activities.emit.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calidata.R;
import com.example.calidata.activities.emit.CheckEmitActivity;
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
        if (viewType == VIEW_TYPE_EMPTY) {
            holder.emptyText.setText(R.string.empty_checks_active);
        } else {
            Drawable logoDrawable = getLogoDrawable(themeId);

            int currentTextColor = holder.optionText.getCurrentTextColor();
            ColorFilter currentIconColor = holder.iconEdit.getColorFilter();

            holder.logo.setImageDrawable(logoDrawable);
            holder.textBank.setText(getBankName(themeId));
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


}
