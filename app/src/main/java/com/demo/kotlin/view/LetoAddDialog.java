package com.demo.kotlin.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.kotlin.R;
import com.demo.kotlin.bean.BlueBall;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.bean.RedBall;
import com.demo.kotlin.bean.SelectBallBean;
import com.demo.kotlin.utils.LottoNumberGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LetoAddDialog extends Dialog {

    private final AddLetoAdapter redLetoAdapter;
    private final AddLetoAdapter blueLetoAdapter;
    private final Button confirmBtn;

    public LetoAddDialog(@NonNull Context context) {
        super(context, R.style.Theme_MaterialComponents_DayNight_DialogWhenLarge);
        setContentView(R.layout.add_leto_dialog);
        confirmBtn = findViewById(R.id.add_btn_confirm_btn);
        Button randomBtn = findViewById(R.id.random_num_btn);
        randomBtn.setOnClickListener(v -> randomNum());
        RecyclerView redBallRecyclerView = findViewById(R.id.red_ball_recycler_view);
        redBallRecyclerView.setLayoutManager(new GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false));
        redLetoAdapter = new AddLetoAdapter(getRedBallList(), true);
        redLetoAdapter.setOnItemClickListener(selectBallBean -> updateConfirmBtn());
        redBallRecyclerView.setAdapter(redLetoAdapter);

        RecyclerView blueBallRecyclerView = findViewById(R.id.blue_ball_recycler_view);
        blueBallRecyclerView.setLayoutManager(new GridLayoutManager(context, 7, LinearLayoutManager.VERTICAL, false));
        blueLetoAdapter = new AddLetoAdapter(getBlueBallList(), false);
        blueLetoAdapter.setOnItemClickListener(selectBallBean -> updateConfirmBtn());
        blueBallRecyclerView.setAdapter(blueLetoAdapter);

        confirmBtn.setOnClickListener(v -> {
            if (onConfirmClickListener != null) {
                onConfirmClickListener.onConfirm(getLetoBean());
            }
            dismiss();
        });
        confirmBtn.setClickable(false);
    }

    private void randomNum() {
        List<Integer> mainNumbers = LottoNumberGenerator.generateMainNumbers();
        List<SelectBallBean> red = redLetoAdapter.getBallList();
        for (int i = 0; i < red.size(); i++) {
            SelectBallBean selectBallBean = red.get(i);
            selectBallBean.isSelected = mainNumbers.contains(selectBallBean.ballNum);
            redLetoAdapter.notifyItemChanged(i);
        }
        List<Integer> specialNumbers = LottoNumberGenerator.generateSpecialNumbers();
        List<SelectBallBean> blue = blueLetoAdapter.getBallList();
        for (int i = 0; i < blue.size(); i++) {
            SelectBallBean ballNum = blue.get(i);
            ballNum.isSelected = specialNumbers.contains(ballNum.ballNum);
            blueLetoAdapter.notifyItemChanged(i);
        }
        updateConfirmBtn();
    }

    private void updateConfirmBtn() {
        confirmBtn.setAlpha(checkConfirmBtn() ? 1f : 0.5f);
        confirmBtn.setClickable(checkConfirmBtn());
    }

    private LeToBean getLetoBean() {
        if (!checkConfirmBtn()) {
            return null;
        }
        List<SelectBallBean> redList = redLetoAdapter.getSelectedBallList();
        redList.sort(new Comparator<SelectBallBean>() {
            @Override
            public int compare(SelectBallBean o1, SelectBallBean o2) {
                return o1.ballNum - o2.ballNum;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        List<SelectBallBean> blueList = blueLetoAdapter.getSelectedBallList();
        blueList.sort(new Comparator<SelectBallBean>() {
            @Override
            public int compare(SelectBallBean o1, SelectBallBean o2) {
                return o1.ballNum - o2.ballNum;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        RedBall redBall = new RedBall(redList.get(0).ballNum, redList.get(1).ballNum, redList.get(2).ballNum, redList.get(3).ballNum, redList.get(4).ballNum);
        BlueBall blueBall = new BlueBall(blueList.get(0).ballNum, blueList.get(1).ballNum);
        return new LeToBean(redBall, blueBall);
    }

    private boolean checkConfirmBtn() {
        return redLetoAdapter.getSelectedBallList().size() + blueLetoAdapter.getSelectedBallList().size() == 7;
    }

    private List<SelectBallBean> getRedBallList() {
        List<SelectBallBean> redBalls = new ArrayList<>();
        for (int i = 1; i <= 35; i++) {
            redBalls.add(new SelectBallBean(i, false));
        }
        return redBalls;
    }

    private List<SelectBallBean> getBlueBallList() {
        List<SelectBallBean> blueBalls = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            blueBalls.add(new SelectBallBean(i, false));
        }
        return blueBalls;
    }

    private OnConfirmClickListener onConfirmClickListener;

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public interface OnConfirmClickListener {
        void onConfirm(LeToBean letoBean);
    }
}
