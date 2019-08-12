package com.example.calidata.activities.emit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.calidata.R;

public class FragmentSearch extends Fragment {

    private Context mContext;

    public FragmentSearch (Context context){
        this.mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emit_search, container, false);
        return view;
    }
}