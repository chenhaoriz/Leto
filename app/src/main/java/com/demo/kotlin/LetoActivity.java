package com.demo.kotlin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.demo.kotlin.bean.LeToBean;
import com.demo.kotlin.view.LetoAddDialog;
import com.demo.kotlin.view.LetoListAdapter;

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
    }
}