package com.dicoding.asclepius.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author riezky maisyar
 */

@Parcelize
data class ResultModel(
    val result: String?,
    val imageUri: Uri?,
) : Parcelable