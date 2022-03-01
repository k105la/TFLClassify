package com.example.tflclassifier.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.tflclassifier.databinding.FragmentPredictBinding
import androidx.activity.result.ActivityResult
import androidx.fragment.app.viewModels
import com.example.tflclassifier.model.BirdRepository
import com.example.tflclassifier.viewmodel.RecognitionViewModel

const val GALLERY_REQUEST_CODE = 123

class PredictFragment : Fragment() {
    private var _predictBinding: FragmentPredictBinding? = null
    private val predictBinding get() = _predictBinding
    private val birdRepo by lazy { BirdRepository }
    private val recognitionViewModel: RecognitionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _predictBinding = FragmentPredictBinding.inflate(layoutInflater, container, false)
        runInference()
        return predictBinding?.root
    }

    private fun runInference() {
        predictBinding?.loadButton?.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            onResult.launch(intent)
        }
    }

    // to get image from gallery
    private val onResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
            onResultReceived(GALLERY_REQUEST_CODE, result)
        }

    private fun onResultReceived(requestCode: Int, result: ActivityResult) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (result.resultCode == Activity.RESULT_OK) {

                    result.data?.data?.let { uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap =
                            BitmapFactory.decodeStream(activity?.contentResolver?.openInputStream(
                                uri))

                        predictBinding?.birdIv?.setImageBitmap(bitmap)
                        birdRepo.birdClassifier.predict(bitmap,
                            predictBinding!!,
                            this.requireContext(),
                            recognitionViewModel,
                            viewLifecycleOwner)
                    }
                } else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }
}

