package com.example.fakecall.Fagment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fakecall.CustomDialog;
import com.example.fakecall.R;

public class HomeFragment extends Fragment {
    private static final int REQUEST_READ_PERMISSION = 786;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    int ON_CHAR_CLICK = 2;
    int ON_CLICK;
    int ON_DIALOG_CLICK = 0;
    int ON_MORE_CLICK = 3;
    int ON_SHADLE_CLICK = 1;
    ImageView callerImage;
    CustomDialog customDialog;
    int dialogId;
    EditText nameEditText;
    EditText phoneEditText;
    int picker;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test , container , false) ;
        nameEditText = (EditText)view.findViewById(R.id.caller_name);
        phoneEditText = (EditText) view.findViewById(R.id.caller_number);
        callerImage = (ImageView) view.findViewById(R.id.caller_image);
        sharedPref = this.getContext().getSharedPreferences("file", 0);
        return view;

    }
}
