package com.demo.kotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.kotlin.databinding.ActivityMainBinding
import com.demo.kotlin.model.MainDataModel
import com.demo.kotlin.utils.LottoNumberGenerator
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


        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:" + this.packageName)
        startActivity(intent)

        dataBinding.clickBtn.setOnLongClickListener {
            val mainNumbers = LottoNumberGenerator.generateMainNumbers()
            val specialNumbers = LottoNumberGenerator.generateSpecialNumbers()
            Log.d(LetoActivity.TAG, "mainNumbers: ${mainNumbers}")
            Log.d(LetoActivity.TAG, "specialNumbers: ${specialNumbers}")
            dataBinding.clickBtn.setText("mainNumbers: ${mainNumbers} \n specialNumbers: ${specialNumbers}")
            return@setOnLongClickListener true
        }

    }


    private fun initData() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainDataModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainDataModel::class.java)
        mainDataModel.listModel?.observe(this, { list ->
            startActivity(Intent(this, LetoActivity::class.java))
            testRecyclerAdapter!!.notifyDataSetChanged()
        })
        dataBinding.mainModel = mainDataModel
    }


}