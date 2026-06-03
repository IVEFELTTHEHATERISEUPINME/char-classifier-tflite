package org.tensorflow.lite.codelabs.digitclassifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CharClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null
    var isInitialized = false
        private set

    private val executorService: ExecutorService = Executors.newCachedThreadPool()
    private var inputImageWidth: Int = 0
    private var inputImageHeight: Int = 0
    private var modelInputSize: Int = 0

    private val MODEL_FILENAME = "model.tflite"

    private val ALL_SYMBOLS = arrayOf(
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "a", "b", "d", "e", "f", "g", "h", "n", "q", "r", "t"
    )

    fun initialize(): Task<Void?> {
        val task = TaskCompletionSource<Void?>()
        executorService.execute {
            try {
                initializeInterpreter()
                task.setResult(null)
            } catch (e: IOException) {
                task.setException(e)
            }
        }
        return task.task
    }

    @Throws(IOException::class)
    private fun initializeInterpreter() {
        val assetManager = context.assets
        val model = loadModelFile(assetManager, MODEL_FILENAME)
        val interpreter = Interpreter(model)

        val inputShape = interpreter.getInputTensor(0).shape()
        inputImageWidth = inputShape[1]
        inputImageHeight = inputShape[2]

        // ========== ИСПРАВЛЕНИЕ: используем константы ==========
        modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE

        this.interpreter = interpreter
        isInitialized = true
        Log.d("CharClassifier", "Модель загружена. Вход: ${inputImageWidth}x${inputImageHeight}, классов: ${ALL_SYMBOLS.size}")
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: android.content.res.AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyAsync(bitmap: Bitmap): Task<String> {
        val task = TaskCompletionSource<String>()
        executorService.execute {
            val result = classify(bitmap)
            task.setResult(result)
        }
        return task.task
    }

    private fun classify(bitmap: Bitmap): String {
        check(isInitialized) { "Модель не загружена" }

        val resizedImage = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
        val byteBuffer = convertBitmapToByteBuffer(resizedImage)

        val output = Array(1) { FloatArray(ALL_SYMBOLS.size) }
        interpreter?.run(byteBuffer, output)

        val probabilities = output[0]
        var maxIndex = 0
        for (i in probabilities.indices) {
            if (probabilities[i] > probabilities[maxIndex]) {
                maxIndex = i
            }
        }
        val confidence = probabilities[maxIndex]
        val recognizedSymbol = ALL_SYMBOLS.getOrElse(maxIndex) { "?" }

        return String.format("Распознано: %s\nУверенность: %.1f%%", recognizedSymbol, confidence * 100)
    }

    fun close() {
        executorService.execute {
            interpreter?.close()
            Log.d("CharClassifier", "Интерпретатор закрыт")
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF)
            val g = (pixel shr 8 and 0xFF)
            val b = (pixel and 0xFF)
            val gray = (r + g + b) / 3.0f
            val normalized = gray / 255.0f
            val inverted = 1 - normalized
            byteBuffer.putFloat(inverted)
        }
        return byteBuffer
    }

    companion object {
        // ========== КОНСТАНТЫ ОПРЕДЕЛЕНЫ ЗДЕСЬ ==========
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1
    }
}