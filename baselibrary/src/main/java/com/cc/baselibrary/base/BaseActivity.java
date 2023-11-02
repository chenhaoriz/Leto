package com.cc.baselibrary.base;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        initView();
        intData();
    }

    protected void intData() {
    }

    protected abstract int getContentLayoutId();

    protected abstract void initView();

}