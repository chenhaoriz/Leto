package com.demo.kotlin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.view.LetoAddDialog;
import com.demo.kotlin.view.LetoListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LetoActivity extends AppCompatActivity {

    private LetoListAdapter letoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leto);
        View addBallBtn = findViewById(R.id.add_ball_btn);
        LetoAddDialog addDialog = new LetoAddDialog(this);
        addDialog.setOnConfirmClickListener(new LetoAddDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm(LeToBean letoBean) {
                letoListAdapter.getLeToBeanList().add(0, letoBean);
                letoListAdapter.notifyDataSetChanged();
            }
        });
        addBallBtn.setOnClickListener(v -> addDialog.show());
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        letoListAdapter = new LetoListAdapter();
        recyclerView.setAdapter(letoListAdapter);


        boolean b = checkPermession(this, PERMESSION_WRITE, 0);
        Toast.makeText(this, "checkPermession:" + b, Toast.LENGTH_SHORT).show();
    }


    public static boolean checkPermession(Activity context, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            //验证是否许可权限
            for (String str : permissions) {
                if (context.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    context.requestPermissions(permissions, requestCode);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 读写权限
     */
    public static final String[] PERMESSION_WRITE = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android
            .Manifest.permission.READ_EXTERNAL_STORAGE};

    String fileName = Environment.getExternalStorageDirectory().getPath() + File.separator + "/Leto/leto.txt";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<LeToBean> leToBeanList = letoListAdapter.getLeToBeanList();
        String json = GsonUtils.toJson(leToBeanList);
        boolean b = FileIOUtils.writeFileFromString(fileName, json);

    }
}