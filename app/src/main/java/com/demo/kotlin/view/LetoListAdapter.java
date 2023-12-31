package com.demo.kotlin.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.kotlin.R;
import com.demo.kotlin.bean.BlueBall;
import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.bean.RedBall;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LetoListAdapter extends RecyclerView.Adapter<LetoListAdapter.LetoListHolder> {
    private final List<LeToBean> leToBeanList;

    public LetoListAdapter(List<LeToBean> leToBeanList) {
        this.leToBeanList = leToBeanList;
    }

    private OnItemRemovedListener onItemRemovedListen;

    public void setOnItemEditListener(OnItemEditListener onItemEditListener) {
        this.onItemEditListener = onItemEditListener;
    }

    private OnItemEditListener onItemEditListener;

    public void setOnItemRemovedListen(OnItemRemovedListener onItemRemovedListen) {
        this.onItemRemovedListen = onItemRemovedListen;
    }

    public interface OnItemRemovedListener {
        void onRemoved(int position);
    }

    public interface OnItemEditListener {
        void onEdited(LeToBean leToBean);
    }

    @NonNull
    @NotNull
    @Override
    public LetoListHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leto_list_item, parent, false);
        return new LetoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LetoListHolder holder, int position) {
        LeToBean leToBean = leToBeanList.get(position);
        holder.deleteBtn.setOnClickListener(v -> {
            if (onItemRemovedListen != null) {
                onItemRemovedListen.onRemoved(holder.getAdapterPosition());
            }
        });
        holder.editBtn.setOnClickListener(v -> {
            if (onItemEditListener != null) {
                onItemEditListener.onEdited(leToBean);
            }
        });
        if (leToBean == null) {
            return;
        }
        RedBall redBall = leToBean.getRedBall();
        holder.redTV1.setText(String.valueOf(redBall.getOne()));
        holder.redTV2.setText(String.valueOf(redBall.getTwo()));
        holder.redTV3.setText(String.valueOf(redBall.getThree()));
        holder.redTV4.setText(String.valueOf(redBall.getFour()));
        holder.redTV5.setText(String.valueOf(redBall.getFive()));
        BlueBall blueBall = leToBean.getBlueBall();
        holder.blueTV1.setText(String.valueOf(blueBall.getOne()));
        holder.blueTV2.setText(String.valueOf(blueBall.getTwo()));
    }

    @Override
    public int getItemCount() {
        return leToBeanList.size();
    }

    public static class LetoListHolder extends RecyclerView.ViewHolder {

        private final TextView redTV1;
        private final TextView redTV2;
        private final TextView redTV3;
        private final TextView redTV4;
        private final TextView redTV5;
        private final TextView blueTV1;
        private final TextView blueTV2;
        private final View deleteBtn;
        private final View editBtn;

        public LetoListHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            redTV1 = itemView.findViewById(R.id.red_tv_1);
            redTV2 = itemView.findViewById(R.id.red_tv_2);
            redTV3 = itemView.findViewById(R.id.red_tv_3);
            redTV4 = itemView.findViewById(R.id.red_tv_4);
            redTV5 = itemView.findViewById(R.id.red_tv_5);

            blueTV1 = itemView.findViewById(R.id.blue_tv_1);
            blueTV2 = itemView.findViewById(R.id.blue_tv_2);
            deleteBtn = itemView.findViewById(R.id.delete_ball_btn);
            editBtn = itemView.findViewById(R.id.edit_ball_btn);
        }
    }
}
