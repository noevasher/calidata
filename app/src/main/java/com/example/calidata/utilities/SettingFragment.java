package com.example.calidata.utilities;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.calidata.R;
import com.example.calidata.session.SessionManager;
import com.example.calidata.utilities.controllers.SettingsController;

class SettingFragment extends Fragment {
    private String token;
    private int fragmentInt;
    private SessionManager sessionManager;

    SettingFragment(int option) {
        this.fragmentInt = option;
        sessionManager = SessionManager.getInstance(getContext());

    }

    public SettingFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);
        TextView termsText = view.findViewById(R.id.textView_terms);
        SettingsController controller = new SettingsController(getContext());
        token = sessionManager.getToken();
        Integer bankId = sessionManager.getBankId();
        //sessionManager.getBankId();
        if (token != null) {
            switch (fragmentInt) {
                case 0:
                    controller.getTermsConditions(token, bankId).subscribe(response -> {
                        termsText.setText(Html.fromHtml(response));

                    }, t -> {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    break;
                case 1:
                    controller.getPrivacyTerms(token, bankId).subscribe(response -> {
                        termsText.setText(Html.fromHtml(response));

                    }, t -> {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    break;
                case 2:
                    controller.getContactBank(token, bankId).subscribe(response -> {
                        termsText.setText(Html.fromHtml(response));

                    }, t -> {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    break;
            }

        }
        return view;
    }


}
