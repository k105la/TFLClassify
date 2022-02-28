package com.example.tflclassifier.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.tflclassifier.databinding.FragmentPredictBinding
import com.example.tflclassifier.ml.ClassifierBirds
import org.tensorflow.lite.support.image.TensorImage

class BirdClassifier {
    fun predict(bitmap: Bitmap, predictBinding: FragmentPredictBinding, context: Context) {
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

        // Setting output text
        predictBinding.predictionText.text = highProbabilityOutput.label
        Log.i("TAG", "outputGenerator: $highProbabilityOutput")
    }
}