package com.demo.kotlin.audiovisual.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.demo.kotlin.audiovisual.IndexFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    // 假设我们有三个不同的Fragment对应三个Tab
    private static final int TAB_COUNT = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 根据position返回不同的Fragment
        switch (position) {
            case 0:
                // 返回第一个Tab对应的Fragment
                return IndexFragment.newInstance(Color.RED);
            case 1:
                // 返回第二个Tab对应的Fragment
                return IndexFragment.newInstance(Color.BLUE);
            case 2:
                // 返回第三个Tab对应的Fragment
                return IndexFragment.newInstance(Color.GREEN);
            default:
                // 默认情况下（这应该永远不会发生），我们可以返回第一个Fragment或抛出异常
                return IndexFragment.newInstance(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        // Tab的数量
        return TAB_COUNT;
    }
}
