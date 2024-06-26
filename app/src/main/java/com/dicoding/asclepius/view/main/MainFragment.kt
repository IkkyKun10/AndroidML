package com.dicoding.asclepius.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.databinding.FragmentMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.model.ResultModel
import com.dicoding.asclepius.view.MainActivity.Companion.RESULT_KEY
import com.yalantis.ucrop.UCrop
import io.realm.kotlin.internal.RealmInitializer.Companion.filesDir
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import kotlin.time.Duration.Companion.seconds

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

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
            intent?.let {
                return UCrop.getOutput(it) ?: Uri.EMPTY
            }
            return Uri.EMPTY
        }

    }

    private val cropImage = registerForActivityResult(uCropContract) { cropUri ->
        currentImageUri = cropUri
        showImage(currentImageUri)
    }

    private val requestGaleriPermission = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val outputUri = File(filesDir, "${System.currentTimeMillis()}_cropped_image.jpg").toUri()
            val list = listOf(uri, outputUri)
            cropImage.launch(list)
        }
    }


    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGallery()
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }

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
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread {
                        showToast(error)
                    }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    requireActivity().runOnUiThread {
                        results?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                showResult(it)
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

    private fun showResult(results: List<Classifications>) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        showToast("$label ${score.formatToString()}")

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1.seconds)
            val result = ResultModel(
                resultLabel = label,
                resultScore = score.formatToString(),
                imageUri = currentImageUri
            )
            moveToResult(result)
        }
    }

    private fun moveToResult(result: ResultModel) {
        val intent = Intent(context, ResultActivity::class.java).also {
            it.putExtra(RESULT_KEY, result)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}