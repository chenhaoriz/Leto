package com.demo.kotlin.audiovisual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.demo.kotlin.R;

/**
 * 视听fragment
 */
public class AudioVisualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_visual2);

        // 创建 Fragment 实例
        AudioVisualFragment fragment = new AudioVisualFragment();

        // 获取 FragmentManager 并开启事务
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 添加或替换 Fragment
        fragmentTransaction.replace(R.id.frame_layout, fragment);

        // 提交事务
        fragmentTransaction.commit();
    }
}