package com.dicoding.asclepius.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.model.ResultModel
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val uCropContract = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return UCrop.getOutput(intent!!)!!
        }

    }

    private val cropImage = registerForActivityResult(uCropContract) { uri ->
        currentImageUri = uri
        showImage(uri)
    }

    private val requestGaleriPermission = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val outputUri = File(filesDir, "cropped_image.jpg").toUri()
            val list = listOf(uri, outputUri)
            cropImage.launch(list)
        }
    }


    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startGallery()
        analyzeImage()
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        binding.galleryButton.setOnClickListener {
            requestGaleriPermission.launch("image/*")
        }
    }

    private fun showImage(uri: Uri?) {
        binding.previewImageView.setImageURI(uri)
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val sortedCategories = it[0].categories.sortedByDescending { it.score }
                                val displayResult = sortedCategories.joinToString(""){
                                    "${it.label} "
                                }
                                showToast(displayResult)
                                binding.analyzeButton.setOnClickListener {
                                    moveToResult(displayResult)
                                }
                            }
                        }
                    }
                }

            }
        )

        currentImageUri?.let {
            imageClassifierHelper.classifyStaticImage(it)
        }
    }

    private fun moveToResult(resultFromClassifier: String) {
        val intent = Intent(this, ResultActivity::class.java).also {
            val model = ResultModel(
                result = resultFromClassifier,
                imageUri = currentImageUri
            )
            it.putExtra(RESULT_KEY, model)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESULT_KEY = "result"
    }
}