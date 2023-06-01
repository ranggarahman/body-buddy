package com.example.bodybuddy.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.bodybuddy.ml.Output
import com.example.bodybuddy.ml.Output1
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp

//fun mlTransform(bitmap: Bitmap, context: Context): String {
//    val filename = "prediction.txt"
//
//    val inputString = context.resources.assets.open(filename).bufferedReader().use { it.readText() }
//    val foodList = inputString.split("\n")
//
//    val model = Model.newInstance(context)
//    var resized = Bitmap.createScaledBitmap(bitmap,224, 224, true)
//    var tbuffer = TensorImage.fromBitmap(resized)
//    var byteBuffer = tbuffer.buffer
//
//    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
//
//    Log.d("hasilinputFeature0",inputFeature0.toString())
//    inputFeature0.loadBuffer(byteBuffer)
//
//    val outputs = model.process(inputFeature0)
//    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
//
//    Log.d("hasilcek",outputFeature0.toString())
//
//    var max = getMax(outputFeature0)
//    // binding.resultPred.text = townList[max]
//
//    model.close()
//
//    return foodList[max]
//}

fun mlTransform(bitmap: Bitmap, context: Context): PredictResult {
    val filename = "prediction.txt"

    val inputString = context.resources.assets.open(filename).bufferedReader().use { it.readText() }
    val foodList = inputString.split("\n")

//    val model = Model.newInstance(context)
    val model = Output1.newInstance(context)

    var resized = Bitmap.createScaledBitmap(bitmap, 1, 1, true)

    // Initialize a TensorImage
    val tensorImage = TensorImage(DataType.FLOAT32)

    // Load Bitmap into TensorImage
    tensorImage.load(resized)

    // Create an ImageProcessor
    val imageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(1, 1, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0f, 255f)) // Normalize pixel values to [0,1]
        .build()

    // Process the TensorImage
    val processedImage = imageProcessor.process(tensorImage)

    Log.d("hasilinputFeature0", processedImage.toString())

    val outputs = model.process(processedImage.tensorBuffer)

    val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
    val outputList = outputFeature0.toList()
    Log.d("hasilcek", "$outputList")

    var max = getMax(outputFeature0)
    Log.d("HasilPrediksi", max.toString())
    // binding.resultPred.text = townList[max]

    model.close()

    return PredictResult(name = foodList[max], confidence = outputList[max]*100)
}

data class PredictResult(
    val name: String,
    val confidence: Float
)

private fun getMax(arr:FloatArray):Int{
    var ind = 0
    var min = 0.0f
    for (i in 0..104){
        if (arr[i]>min){
            ind = i
            min = arr[i]
        }
    }
    return ind
}