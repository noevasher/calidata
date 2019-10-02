package com.example.calidata.utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.calidata.OnSingleClickListener;
import com.example.calidata.R;

class ContactFragment extends Fragment {

    public ContactFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ImageView call = view.findViewById(R.id.imageView_call);
        ImageView message = view.findViewById(R.id.imageView_message);
        ConstraintLayout containerEmail = view.findViewById(R.id.container_email);
        TextView emailText = view.findViewById(R.id.textView_email);
        String email = emailText.getText().toString();

        TextView numberText = view.findViewById(R.id.textView_number);
        String number = numberText.getText().toString();

        call.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });

        message.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                Uri data = Uri.parse("sms:" + number);
                intent.setData(data);
                startActivity(intent);
            }
        });

        containerEmail.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");

                //i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


}
