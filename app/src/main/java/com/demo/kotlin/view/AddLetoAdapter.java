package com.demo.kotlin.view;

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
    private final List<SelectBallBean> redBalls;
    private final List<SelectBallBean> selectedBallList = new ArrayList<>();

    private final boolean isRed;

    public AddLetoAdapter(List<SelectBallBean> redBalls, boolean isRed) {
        this.redBalls = redBalls;
        this.isRed = isRed;
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
        SelectBallBean selectBallBean = redBalls.get(position);
        holder.textView.setBackgroundResource(isRed ? R.drawable.red_ball_bg_shape : R.drawable.blue_ball_bg_shape);
        holder.textView.setText(String.valueOf(selectBallBean.ballNum));
        holder.textView.setAlpha(selectBallBean.isSelected ? 1 : 0.5f);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRed) {
                    if (selectedBallList.size() == 5 && !selectBallBean.isSelected) {
                        return;
                    }
                } else {
                    if (selectedBallList.size() == 2 && !selectBallBean.isSelected) {
                        return;
                    }
                }
                selectBallBean.isSelected = !selectBallBean.isSelected;
                if (selectBallBean.isSelected) {
                    selectedBallList.add(selectBallBean);
                }else {
                    selectedBallList.remove(selectBallBean);
                }
                notifyItemChanged(position);
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(selectBallBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return redBalls == null ? 0 : redBalls.size();
    }

    public static class RedBallHolder extends RecyclerView.ViewHolder {
        private TextView textView;

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
        return selectedBallList;
    }
}
