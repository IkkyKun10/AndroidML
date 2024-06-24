package com.dicoding.asclepius.view.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.entity.ResultEntity
import com.dicoding.asclepius.model.ResultModel
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResultViewModel(private val realm: Realm) : ViewModel() {

    val histories = realm
        .query<ResultEntity>()
        .asFlow()
        .map { results ->
            results.list.map {
                ResultModel(
                    resultLabel = it.resultLabel,
                    resultScore = it.resultScore,
                    imageUri = Uri.parse(it.imageUri)
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

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