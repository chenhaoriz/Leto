package com.demo.kotlin;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.cc.baselibrary.base.BaseActivity;
import com.cc.baselibrary.base.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.model.LetoViewModel;
import com.demo.kotlin.utils.LottoNumberGenerator;
import com.demo.kotlin.view.LetoAddDialog;
import com.demo.kotlin.view.LetoListAdapter;

import java.util.List;

public class LetoActivity extends BaseActivity {
    public static final String TAG = "LetoActivity_cc";
    private LinearLayout mProbabilityView;
    private LetoViewModel viewModel;
    private HeaderAndFooterWrapper<Object> wrapper;
    private LetoAddDialog addDialog;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_leto;
    }

    @Override
    protected void intData() {
        checkPermession(this, PERMESSION_WRITE, 0);
        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LetoViewModel.class);
        viewModel.letoListLiveData.observe(this, this::getHistoryList);
        viewModel.saveHistoryData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(mContext, "save:" + aBoolean, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.addLetoLiveData.observe(this, le -> wrapper.notifyDataSetChanged());
        viewModel.updateLetoLiveData.observe(this, le -> wrapper.notifyDataSetChanged());
        viewModel.removeLetoLiveData.observe(this, position -> wrapper.notifyItemRemoved(position));
        viewModel.ballProbabilityLiveData.observe(this, this::updateHeadView);
        viewModel.getHistoryLetoList();

    }

    private void updateHeadView(List<String> strings) {
        mProbabilityView.removeAllViews();
        for (int i = 0; i < strings.size(); i++) {
            TextView title = new TextView(mContext);
            title.setText("-----------" + "ball:" + (i + 1) + "-----------");
            mProbabilityView.addView(title);
            TextView oneProbability = new TextView(mContext);
            oneProbability.setText(strings.get(i));
            mProbabilityView.addView(oneProbability);
        }
    }

    @Override
    protected void initView() {
        View addBallBtn = findViewById(R.id.add_ball_btn);
        addDialog = new LetoAddDialog(this);
        addDialog.setOnConfirmClickListener(new LetoAddDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm(boolean isEditBall, LeToBean letoBean) {
                if (isEditBall) {
                    viewModel.updateLetoItem(letoBean);
                } else {
                    viewModel.addLetoItem(letoBean);
                }
            }
        });
        addBallBtn.setOnClickListener(v -> addDialog.show());
        addBallBtn.setOnLongClickListener(v -> {
            viewModel.saveHistory();
            return true;
        });
    }

    private void getHistoryList(List<LeToBean> list) {
        Log.d(TAG, "[getHistoryList]:" + list.size());
        mProbabilityView = new LinearLayout(mContext);
        mProbabilityView.setOrientation(LinearLayout.VERTICAL);
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LetoListAdapter letoListAdapter = new LetoListAdapter(list);
        letoListAdapter.setOnItemRemovedListen(position -> viewModel.removeItem(position));
        letoListAdapter.setOnItemEditListener(leToBean -> {
            if (addDialog == null) {
                addDialog = new LetoAddDialog(mContext);
            }
            addDialog.show(leToBean);
        });
        wrapper = new HeaderAndFooterWrapper<>(letoListAdapter);
        wrapper.addHeaderView(mProbabilityView);
        recyclerView.setAdapter(wrapper);
    }


    public static void checkPermession(Activity context, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            //验证是否许可权限
            for (String str : permissions) {
                if (context.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    context.requestPermissions(permissions, requestCode);
                    return;
                }
            }
        }
    }

    /**
     * 读写权限
     */
    public static final String[] PERMESSION_WRITE = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android
            .Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.saveHistory();
        if (addDialog != null) {
            addDialog.dismiss();
        }
    }
}