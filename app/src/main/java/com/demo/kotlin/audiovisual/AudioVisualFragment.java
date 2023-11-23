package com.demo.kotlin.audiovisual;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.cc.baselibrary.base.BaseFragment;
import com.demo.kotlin.R;
import com.demo.kotlin.audiovisual.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AudioVisualFragment extends BaseFragment {

    private int currentColor;
    private View topColorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 确保Fragment已附加到Activity
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            // 设置状态栏透明
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.audio_visual_fragment;
    }

    @Override
    protected void initView() {


        topColorView = findViewById(R.id.top_color_view);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // 创建适配器并设置给 ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
        setTopBgColorCallback(viewPager);
        viewPager.setAdapter(adapter);
        // 将TabLayout和ViewPager关联起来，并设置Tab的名称
        attachTabLayout(tabLayout, viewPager);
    }

    private static void attachTabLayout(TabLayout tabLayout, ViewPager2 viewPager) {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("试听");
                            break;
                        case 1:
                            tab.setText("音频");
                            break;
                        case 2:
                            tab.setText("推荐");
                            break;
                        default:
                            tab.setText("Tab " + (position + 1));
                            break;
                    }
                }).attach();
    }


    private void setTopBgColorCallback(ViewPager2 viewPager) {
        // 当前的TabLayout和状态栏颜色
        int[] tabColors = new int[]{
                getResources().getColor(R.color.fragment1_color),
                getResources().getColor(R.color.fragment2_color),
                getResources().getColor(R.color.fragment3_color)
        };
        currentColor = tabColors[0];
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int newColor = tabColors[position];

                // 创建一个颜色值动画，从当前颜色过渡到新颜色
                ValueAnimator colorAnimation = ValueAnimator.ofArgb(currentColor, newColor);
                colorAnimation.setDuration(300); // 设置动画持续时间
                colorAnimation.addUpdateListener(animator -> {
                    int color = (int) animator.getAnimatedValue();

                    // 更新TabLayout的背景色
                    topColorView.setBackgroundColor(color);

                    // 更新状态栏颜色
                    if (getActivity() != null) {
                        Window window = getActivity().getWindow();
                        window.setStatusBarColor(color);
                        // 自动调整状态栏中的文字和图标颜色
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 如果背景颜色是浅色，使用深色状态栏符号
                            if (isColorLight(color)) {
                                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            } else {
                                window.getDecorView().setSystemUiVisibility(0);
                            }
                        }
                    }
                });
                colorAnimation.start();
                // 更新当前颜色值
                currentColor = newColor;
            }
        });
    }

    // 辅助方法来检测颜色是不是浅色
    private boolean isColorLight(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        // It's a light color if darkness < 0.5
        return darkness < 0.5;
    }
    private void updateStatusBarIcons(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isColorLight(color)) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(0);
            }
        }
    }


}

