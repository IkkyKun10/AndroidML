package com.dicoding.asclepius.view.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.dicoding.asclepius.MyApp
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.model.ResultModel
import com.dicoding.asclepius.view.MainActivity
import com.dicoding.asclepius.viewModelFactory

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel by viewModels<ResultViewModel>(
        factoryProducer = { viewModelFactory { ResultViewModel(MyApp.realm) } }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreen(window)
        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val resultModel: ResultModel?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultModel = intent.extras?.getParcelable(
                MainActivity.RESULT_KEY,
                ResultModel::class.java
            )
        } else {
            resultModel = intent.getParcelableExtra(MainActivity.RESULT_KEY)
        }

        binding.resultImage.setImageURI(resultModel?.imageUri)
        binding.resultText.text = buildString {
            append(resultModel?.resultLabel)
            append(" ")
            append(resultModel?.resultScore)
        }

        viewModel.insertToHistory(resultModel!!)
    }
}

fun setFullScreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}