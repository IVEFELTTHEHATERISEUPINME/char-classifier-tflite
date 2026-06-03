# Рукописный классификатор символов (Digit & Letter Classifier)

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![TensorFlow Lite](https://img.shields.io/badge/TensorFlow_Lite-FF6F00?style=for-the-badge&logo=tensorflow&logoColor=white)](https://www.tensorflow.org/lite)

**Лабораторная работа №4** по дисциплине «Разработка мобильных приложений».

Android-приложение для распознавания рукописных цифр и букв с использованием TensorFlow Lite и модели EMNIST Balanced (47 классов).

---

## 📱 Скриншоты

### Главное меню
<img width="484" height="995" alt="main menu" src="https://github.com/user-attachments/assets/c5807bd5-0234-442d-8cf9-7f8ee897bf13" />

### Результат распознавания
<img width="487" height="995" alt="result" src="https://github.com/user-attachments/assets/4914ab16-2c29-40b3-bfc9-aa7eca450189" />

---

## 🎯 Возможности

- ✏️ Рисование символов пальцем на экране
- 🤖 Распознавание **47 рукописных символов**:
  - Цифры: 0–9 (10 классов)
  - Заглавные буквы: A–Z (26 классов)
  - Строчные буквы: a, b, d, e, f, g, h, n, q, r, t (11 классов)
- 📊 Отображение процента уверенности (confidence)
- ⚡ Полностью офлайн — модель работает на устройстве
- 🚀 Асинхронная обработка — интерфейс не зависает

---

## 🛠 Технологии

| Технология | Версия | Назначение |
|------------|--------|------------|
| Android SDK | API 24+ | Минимальная версия Android 7.0 |
| Kotlin | 1.9.20 | Язык разработки |
| TensorFlow Lite | 2.17.0 | Запуск модели машинного обучения |
| Gradle | 8.13 | Система сборки |
| AGP | 8.13.2 | Android Gradle Plugin |
| EMNIST Balanced | — | Модель распознавания (47 классов) |

---

## 🚀 Установка и запуск

### Требования

- Android Studio Ladybug (2024.2.1) или новее
- Android устройство с **Android 7.0 (API 24)+** или эмулятор
- Java **JDK 17**

### Инструкция

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/IVEFELTTHEHATERISEUPINME/char-classifier-tflite.git
2. Открыть проект в Android Studio
- File → Open → выбрать папку char-classifier-tflite
- Дождаться синхронизации Gradle
3. Подключить устройство или запустить эмулятор
- Физическое устройство: включите режим разработчика и USB-отладку
- Эмулятор: создайте виртуальное устройство с API 24+
4. Запустить приложение
- Нажмите зелёный треугольник ▶️
- Или выполните: ./gradlew installDebug
5. Использование
- Нарисуйте символ пальцем на белом поле
- Нажмите «Распознать»
- Результат появится в нижней части экрана

---

## 📸 Примеры работы
| Ввод (рисунок) | Выход |
|------------|--------|
| Цифра «5» | Распознано: 5, Уверенность: 94% |
| Заглавная «A» | Распознано: A, Уверенность: 87% |
| Строчная «a» | Распознано: a, Уверенность: 76% |
| Смазанная "8" | Распознано: 3, Уверенность: 30% |

---

## 🔧 Возможные проблемы и решения
| Проблема | Решение |
|------------|--------|
| model.tflite not found | Убедитесь, что файл модели лежит в app/src/main/assets/ |
| Cannot copy from a TensorFlowLite tensor with shape [1, 47] | В модели 47 классов, массив ALL_SYMBOLS должен содержать 47 элементов |
| Inconsistent JVM-target compatibility | Установите Java 17 и настройте toolchain |
| Unresolved reference 'DrawingView' | Проверьте полный путь в XML: org.tensorflow...DrawingView |

---

## 📚 Документация
- Руководство по TensorFlow Lite для Android
- Официальный Codelab: Digit Classifier

## 📄 Лицензия
Проект распространяется под лицензией MIT. Подробнее в файле LICENSE.

## 👨‍💻 Авторы
- Студенты группы ИБ-206Б
- Валишин М.М., Хабиров Э.И.

## Преподаватель:
- Чернышев Е.С.

## 🗓️ Дата выполнения
- май 2026 года
