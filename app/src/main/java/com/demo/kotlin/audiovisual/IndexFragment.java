package com.demo.kotlin.audiovisual;

import android.os.Bundle;
import android.widget.TextView;

import com.cc.baselibrary.base.BaseFragment;
import com.demo.kotlin.R;

public class IndexFragment extends BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static IndexFragment newInstance(int position) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_audio_visual2;
    }

    @Override
    protected void initView() {
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        TextView viewById = findViewById(R.id.text_view);
        viewById.setText("sectionNumber:" + sectionNumber);

    }
}
