package com.demo.kotlin.model;

import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.demo.kotlin.bean.BlueBall;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.bean.RedBall;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LetoViewModel extends ViewModel {
    public static final String TAG = "LetoModel";
    private final String FILE_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LETO/";
    private final String FILE_NAME = "leto.txt";
    public MutableLiveData<List<LeToBean>> letoListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> saveHistoryData = new MutableLiveData<>();
    public MutableLiveData<List<String>> ballProbabilityLiveData = new MutableLiveData<>();
    public MutableLiveData<LeToBean> addLetoLiveData = new MutableLiveData<>();
    public MutableLiveData<LeToBean> updateLetoLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> removeLetoLiveData = new MutableLiveData<>();

    public void getHistoryLetoList() {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<List<LeToBean>>() {
            @Override
            public List<LeToBean> doInBackground() throws Throwable {
                String json = FileIOUtils.readFile2String(FILE_SAVE_PATH + FILE_NAME);
                if (json == null) {
                    return new ArrayList<>();
                }
                Log.d(TAG, "[getHistoryLetoList]:" + json);
                Type fooType = new TypeToken<ArrayList<LeToBean>>() {
                }.getType();
                return GsonUtils.fromJson(json, fooType);
            }

            @Override
            public void onSuccess(List<LeToBean> result) {
                letoListLiveData.setValue(result);
                updateProbability();
            }
        });

    }

    public void saveHistory() {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Boolean>() {
            @Override
            public Boolean doInBackground() {
                if (letoListLiveData.getValue() == null) {
                    return false;
                }
                String json = GsonUtils.toJson(letoListLiveData.getValue());
                return FileIOUtils.writeFileFromString(FILE_SAVE_PATH + FILE_NAME, json);
            }

            @Override
            public void onSuccess(Boolean success) {
                saveHistoryData.setValue(success);
            }
        });
    }

    public void updateProbability() {
        ThreadUtils.executeByCpu(new ThreadUtils.SimpleTask<ArrayList<String>>() {
            @Override
            public ArrayList<String> doInBackground() throws Throwable {
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 1; i <= 7; i++) {
                    String ballProbability = getBallProbability(i, letoListLiveData.getValue());
                    strings.add(ballProbability);
                    Log.d(TAG, "ballProbability:" + ballProbability);
                }
                return strings;
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                ballProbabilityLiveData.setValue(result);
            }
        });

    }

    private String getBallProbability(int numBall, List<LeToBean> leToBeanList) {
        if (leToBeanList == null) {
            return "";
        }

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
                        String keyValue = String.valueOf(key);
                        if (key < 10) {
                            keyValue = "0" + key;
                        }
                        Log.d(TAG, "key+count:" + (key + ":" + count));
                        double probability = ((double) count / leToBeanList.size()) * 100;
                        stringBuffer.append("[").append(keyValue).append("]").append(" : ").append(count).append("次").append("\t\t").append(probability).append("%").append("\n");
                    }
                });
        return stringBuffer.toString();
    }

    public void addLetoItem(LeToBean le) {
        List<LeToBean> value = letoListLiveData.getValue();
        if (value == null) {
            value = new ArrayList<>();
        }
        value.add(0, le);
        addLetoLiveData.setValue(le);
        updateProbability();
    }

    public void updateLetoItem(LeToBean le) {
        List<LeToBean> value = letoListLiveData.getValue();
        if (value == null) {
            return;
        }
        for (LeToBean leToBean : value) {
            if (leToBean.id == null) {
                continue;
            }
            if (leToBean.id.equals(le.id)) {
                leToBean = le;
                updateLetoLiveData.setValue(leToBean);
                updateProbability();
                break;
            }
        }
    }


    public void removeItem(int le) {
        List<LeToBean> value = letoListLiveData.getValue();
        if (value == null) {
            return;
        }
        value.remove(le - 1);
        removeLetoLiveData.setValue(le);
        updateProbability();
    }

}
