package com.demo.kotlin.model

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainDataModel : ViewModel() {
    var listModel: MutableLiveData<MutableList<String>>? = MutableLiveData()

    init {
        listModel?.value = generateData();
    }

    fun loadMoreDate(view: View) {
        listModel?.postValue(generateData())
    }

    fun generateData(): MutableList<String> {
        val list = mutableListOf("------------------------------")
        for (i in 0..5) {
            val randomNum = Random.nextInt(1, 35)
            val indexVal = " index: $i random: $randomNum"
            list.add(indexVal)
            Log.d("generateListData", indexVal)
        }
        return list
    }
}