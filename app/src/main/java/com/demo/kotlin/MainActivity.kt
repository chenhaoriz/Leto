package com.demo.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.kotlin.databinding.ActivityMainBinding
import com.demo.kotlin.model.MainDataModel
import com.demo.kotlin.view.TestRecyclerAdapter

class MainActivity : AppCompatActivity() {
    private var testRecyclerAdapter: TestRecyclerAdapter? = null;
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var mainDataModel: MainDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()

        dataBinding.recycleView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        testRecyclerAdapter = TestRecyclerAdapter(mutableListOf())
        dataBinding.recycleView.adapter = testRecyclerAdapter

        startActivity(Intent(this, LetoActivity::class.java));
    }


    private fun initData() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainDataModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainDataModel::class.java)
        mainDataModel.listModel?.observe(this, { list ->
            testRecyclerAdapter!!.notifyDataSetChanged()
        })
        dataBinding.mainModel = mainDataModel
    }


}