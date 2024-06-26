package com.dicoding.asclepius.view.history

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.entity.ResultEntity
import com.dicoding.asclepius.model.ResultModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val realm: Realm) : ViewModel() {

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
}