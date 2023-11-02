package com.demo.kotlin.view;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.kotlin.R;
import com.demo.kotlin.bean.RedBall;
import com.demo.kotlin.bean.SelectBallBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddLetoAdapter extends RecyclerView.Adapter<AddLetoAdapter.RedBallHolder> {
    private final List<SelectBallBean> list = new ArrayList<>();

    private final boolean isRed;

    public AddLetoAdapter(List<SelectBallBean> redBalls, boolean isRed) {
        this.isRed = isRed;
        this.list.addAll(redBalls);
    }

    @NonNull
    @NotNull
    @Override
    public RedBallHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ball_item_layout, parent, false);
        return new RedBallHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RedBallHolder holder, int position) {
        SelectBallBean bean = list.get(position);
        holder.textView.setBackgroundResource(getBallBgResid(bean.isSelected));
        holder.textView.setTextColor(bean.isSelected ? Color.WHITE : isRed ? Color.RED : Color.BLUE);
        holder.textView.setText(String.valueOf(bean.ballNum));
        holder.textView.setAlpha(bean.isSelected ? 1 : 0.5f);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SelectBallBean> selectedBallList = getSelectedBallList();
                if (isRed) {
                    if (selectedBallList.size() == 5 && !bean.isSelected) {
                        return;
                    }
                } else {
                    if (selectedBallList.size() == 2 && !bean.isSelected) {
                        return;
                    }
                }
                bean.isSelected = !bean.isSelected;
                notifyItemChanged(position);
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(bean);
                }
            }
        });
    }

    private int getBallBgResid(boolean isSelected) {
        return isSelected ? (isRed ? R.drawable.red_ball_bg_shape : R.drawable.blue_ball_bg_shape) : R.drawable.default_ball_bg_shape;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RedBallHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public RedBallHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(SelectBallBean selectBallBean);
    }

    public List<SelectBallBean> getSelectedBallList() {
        List<SelectBallBean> selectBallBeans = new ArrayList<>();
        List<SelectBallBean> ballList = getBallList();
        for (SelectBallBean selectBallBean : ballList) {
            if (selectBallBean.isSelected) {
                selectBallBeans.add(selectBallBean);
            }
        }
        return selectBallBeans;
    }

    public List<SelectBallBean> getBallList() {
        return list;
    }
}
