package com.demo.kotlin.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.kotlin.R;
import com.demo.kotlin.bean.BlueBall;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.bean.RedBall;
import com.demo.kotlin.bean.SelectBallBean;

import java.util.ArrayList;
import java.util.List;

public class LetoAddDialog extends Dialog {


    private final AddLetoAdapter redLetoAdapter;
    private final AddLetoAdapter blueLetoAdapter;

    public LetoAddDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.add_leto_dialog);
        View confirmBtn = findViewById(R.id.add_btn_confirm_btn);

        RecyclerView redBallRecyclerView = findViewById(R.id.red_ball_recycler_view);
        redBallRecyclerView.setLayoutManager(new GridLayoutManager(context, 5, LinearLayoutManager.VERTICAL, false));
        redLetoAdapter = new AddLetoAdapter(getRedBallList(), true);
        redLetoAdapter.setOnItemClickListener(selectBallBean -> confirmBtn.setAlpha(checkConfirmBtn() ? 1f : 0.2f));
        redBallRecyclerView.setAdapter(redLetoAdapter);

        RecyclerView blueBallRecyclerView = findViewById(R.id.blue_ball_recycler_view);
        blueBallRecyclerView.setLayoutManager(new GridLayoutManager(context, 5, LinearLayoutManager.VERTICAL, false));
        blueLetoAdapter = new AddLetoAdapter(getBlueBallList(), false);
        blueLetoAdapter.setOnItemClickListener(selectBallBean -> confirmBtn.setAlpha(checkConfirmBtn() ? 1f : 0.2f));
        blueBallRecyclerView.setAdapter(blueLetoAdapter);

        confirmBtn.setOnClickListener(v -> {
            if (onConfirmClickListener != null) {
                onConfirmClickListener.onConfirm(getLetoBean());
            }
        });
    }

    private LeToBean getLetoBean() {
        if (!checkConfirmBtn()) {
            return null;
        }
        List<SelectBallBean> redList = redLetoAdapter.getSelectedBallList();
        List<SelectBallBean> blueList = blueLetoAdapter.getSelectedBallList();

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
