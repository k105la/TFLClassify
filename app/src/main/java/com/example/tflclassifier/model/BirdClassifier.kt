package com.example.tflclassifier.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.tflclassifier.databinding.FragmentPredictBinding
import com.example.tflclassifier.ml.ClassifierBirds
import com.example.tflclassifier.viewmodel.Recognition
import com.example.tflclassifier.viewmodel.RecognitionViewModel
import org.tensorflow.lite.support.image.TensorImage

class BirdClassifier {
    fun predict(bitmap: Bitmap, predictBinding: FragmentPredictBinding, context: Context, recognitionViewModel: RecognitionViewModel, viewLifecycleOwner: LifecycleOwner ) {
        // Declaring tensor flow lite model variable
        val birdsModel = ClassifierBirds.newInstance(context)

        // Converting bitmap into tensor flow image
        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val tfImage = TensorImage.fromBitmap(newBitmap)

        // Process the image using trained model and sort it in descending order
        val outputs = birdsModel.process(tfImage)
            .probabilityAsCategoryList.apply { sortByDescending { it.score } }

        // Getting result having high probability
        val highProbabilityOutput = outputs[0]

        recognitionViewModel.updateData(Recognition(highProbabilityOutput.label))
        // Setting output text
        recognitionViewModel.recognition.observe(viewLifecycleOwner) {
            predictBinding.predictionText.text = it
        }
        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
    }
}