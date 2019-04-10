package com.example.feng.version1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CountFragment extends Fragment {
    public static CountFragment newInstance() {

        Bundle args = new Bundle();

        CountFragment fragment = new CountFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
