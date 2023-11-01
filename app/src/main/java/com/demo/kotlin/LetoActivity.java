package com.demo.kotlin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.demo.kotlin.bean.BlueBall;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.bean.RedBall;
import com.demo.kotlin.view.LetoAddDialog;
import com.demo.kotlin.view.LetoListAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LetoActivity extends AppCompatActivity {
    public static final String TAG = "LetoActivity";
    private LetoListAdapter letoListAdapter;
    private LinearLayout mProbabilityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leto);
        View addBallBtn = findViewById(R.id.add_ball_btn);
        LetoAddDialog addDialog = new LetoAddDialog(this);
        addDialog.setOnConfirmClickListener(letoBean -> {
            letoListAdapter.getLeToBeanList().add(0, letoBean);
            letoListAdapter.notifyDataSetChanged();
            updateProbability();
        });
        addBallBtn.setOnClickListener(v -> addDialog.show());

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        letoListAdapter = new LetoListAdapter();
        if (getHistoryList() != null) {
            letoListAdapter.getLeToBeanList().addAll(getHistoryList());
        }
        letoListAdapter.setOnItemRemovedListen(new LetoListAdapter.OnItemRemovedListener() {
            @Override
            public void onRemoved() {
                updateProbability();
            }
        });
        recyclerView.setAdapter(letoListAdapter);
        mProbabilityView = findViewById(R.id.count_container_view);
        updateProbability();
        checkPermession(this, PERMESSION_WRITE, 0);
    }

    private void updateProbability() {
        mProbabilityView.removeAllViews();
        for (int i = 1; i <= 7; i++) {
            updateProbabilityItem(i);
        }
    }

    private void updateProbabilityItem(int ball) {
        TextView title = new TextView(this);
        title.setText("-----------" + "ball:" + ball + "-----------");
        mProbabilityView.addView(title);
        TextView oneProbability = new TextView(this);
        oneProbability.setText(getBallProbability(ball));
        mProbabilityView.addView(oneProbability);
    }

    private String getBallProbability(int numBall) {
        List<LeToBean> leToBeanList = letoListAdapter.getLeToBeanList();
        Map<Integer, Integer> elementCountMap = new HashMap<>();
        for (int i = 0; i < leToBeanList.size(); i++) {
            LeToBean leToBean = leToBeanList.get(i);
            RedBall redBall = leToBean.getRedBall();
            BlueBall blueBall = leToBean.getBlueBall();
            int num = redBall.getOne();
            switch (numBall) {
                case 1:
                    num = redBall.getOne();
                    break;
                case 2:
                    num = redBall.getTwo();
                    break;
                case 3:
                    num = redBall.getThree();
                    break;
                case 4:
                    num = redBall.getFour();
                    break;
                case 5:
                    num = redBall.getFive();
                    break;
                case 6:
                    num = blueBall.getOne();
                    break;
                case 7:
                    num = blueBall.getTwo();
                    break;

            }
            if (elementCountMap.containsKey(num)) {
                elementCountMap.put(num, elementCountMap.get(num) + 1);
            } else {
                elementCountMap.put(num, 1);
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        // 使用Stream API和Comparator按照出现次数排序，然后打印
        elementCountMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(new Consumer<Map.Entry<Integer, Integer>>() {
                    @Override
                    public void accept(Map.Entry<Integer, Integer> entry) {
                        Integer count = entry.getValue();
                        Integer key = entry.getKey();
                        Log.d(TAG, "key+count:" + (key + ":" + count));
                        double probability = ((double) count / leToBeanList.size()) * 100;
                        stringBuffer.append("次数：").append(key).append("：").append(count).append("\t占比：").append(probability).append("%").append("\n");
                    }
                });
        return stringBuffer.toString();
    }

    private List<LeToBean> getHistoryList() {
        String json = FileIOUtils.readFile2String(FILE_SAVE_PATH + FILE_NAME);
        if (json == null) {
            return null;
        }
        Log.d("LetoActivity", "[getHistoryList]:" + json);
        Type fooType = new TypeToken<ArrayList<LeToBean>>() {
        }.getType();
        return GsonUtils.fromJson(json, fooType);
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

    private final String FILE_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LETO/";
    private final String FILE_NAME = "leto.txt";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<LeToBean> leToBeanList = letoListAdapter.getLeToBeanList();
        String json = GsonUtils.toJson(leToBeanList);
        boolean success = FileIOUtils.writeFileFromString(FILE_SAVE_PATH + FILE_NAME, json);
        Toast.makeText(this, "success:" + success, Toast.LENGTH_SHORT).show();
    }


}