package com.dicoding.asclepius.view.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.entity.ResultEntity
import com.dicoding.asclepius.model.ResultModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.launch

class ResultViewModel(private val realm: Realm) : ViewModel() {

    fun insertToHistory(resultModel: ResultModel) {
        viewModelScope.launch {
            realm.write {
                val resultEntity = ResultEntity().apply {
                    resultLabel = resultModel.resultLabel
                    resultScore = resultModel.resultScore
                    imageUri = resultModel.imageUri.toString()
                }
                copyToRealm(resultEntity, updatePolicy = UpdatePolicy.ALL)
                Log.i("ResultViewModel", "insertToHistory: Success")
            }
        }
    }
}