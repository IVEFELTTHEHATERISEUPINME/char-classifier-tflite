package org.tensorflow.lite.codelabs.digitclassifier

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Главный экран приложения.
 *
 * Отвечает за:
 * - Отображение области для рисования (DrawingView)
 * - Координацию работы классификатора (CharClassifier)
 * - Отображение результата распознавания
 * - Обработку нажатий кнопок
 */
class MainActivity : AppCompatActivity() {

  // Компоненты интерфейса
  private lateinit var drawingView: DrawingView      // Область для рисования
  private lateinit var resultText: TextView         // Поле для вывода результата
  private lateinit var classifier: CharClassifier   // Классификатор символов

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Инициализация UI-компонентов по ID из разметки
    drawingView = findViewById(R.id.drawingView)
    resultText = findViewById(R.id.resultText)
    val clearButton = findViewById<Button>(R.id.clearButton)
    val classifyButton = findViewById<Button>(R.id.classifyButton)

    // ========== ИНИЦИАЛИЗАЦИЯ КЛАССИФИКАТОРА ==========

    // Создаём экземпляр классификатора
    classifier = CharClassifier(this)

    // Асинхронная загрузка модели
    classifier.initialize().addOnSuccessListener {
      // Модель загружена успешно
      resultText.text = "Готов к распознаванию букв и цифр"
      resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
    }.addOnFailureListener { e ->
      // Ошибка загрузки модели
      resultText.text = "Ошибка загрузки модели"
      Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
    }

    // ========== ОБРАБОТЧИКИ КНОПОК ==========

    // Кнопка "Очистить" — стирает рисунок
    clearButton.setOnClickListener {
      drawingView.clear()                 // Очищаем область рисования
      resultText.text = "Нарисуйте символ" // Сбрасываем текст результата
      resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark))
    }

    // Кнопка "Распознать" — запускает распознавание
    classifyButton.setOnClickListener {
      // Получаем рисунок из DrawingView
      val bitmap = drawingView.getBitmap()

      // Проверка 1: есть ли вообще рисунок?
      if (bitmap == null) {
        Toast.makeText(this, "Сначала нарисуйте символ", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      // Проверка 2: пустой ли рисунок? (ИСПРАВЛЕНИЕ — не даём распознавать пустое поле)
      if (!drawingView.isDrawingNotEmpty()) {
        resultText.text = "Нарисуйте символ\n(поле пустое)"
        resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        return@setOnClickListener
      }

      // Если рисунок не пустой — распознаём
      classifier.classifyAsync(bitmap).addOnSuccessListener { result ->
        resultText.text = result
        resultText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
      }.addOnFailureListener { e ->
        resultText.text = "Ошибка распознавания"
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
      }
    }
  }

  /**
   * Освобождение ресурсов при закрытии Activity.
   * Важно: interpreter.close() должен быть вызван обязательно!
   */
  override fun onDestroy() {
    super.onDestroy()
    classifier.close()   // Закрываем интерпретатор, освобождаем нативную память
  }
}