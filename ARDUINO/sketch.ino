#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <stdlib.h>

// Инициализация дисплея (адрес 0x27, 16 символов, 4 строки)
LiquidCrystal_I2C lcd(0x27, 16, 4);

const int buttonPin = 2;
unsigned long startTime, endTime;

void setup() {
  lcd.init();
  lcd.backlight();
  pinMode(buttonPin, INPUT_PULLUP);
  randomSeed(analogRead(0));
}

// Рекурсивная функция Фибоначчи
unsigned long long fibonacci(int n) {
  if (n <= 1) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}

void loop() {
  if (digitalRead(buttonPin) == LOW) {
    delay(50); // Антидребезг

    // Генерация случайного числа от 5 до 30
    int randNum = random(5, 31);
    lcd.clear();
    // Вывод на Строку №1
    lcd.setCursor(0, 0);
    lcd.print("random: " + String(randNum));

    // Замер времени
    startTime = micros();
    unsigned long long fibNum = fibonacci(randNum);
    endTime = micros();

    // Вывод времени на Строку №2
    lcd.setCursor(0, 1);
    float seconds = (endTime - startTime) / 1000000.0;
    lcd.print("time: " + String(seconds, 5) + " sec.");

    // Вывод числа Фибоначчи на Строку №3
    lcd.setCursor(0, 2);
    lcd.print("number: " + String((unsigned long)fibNum));

    // Ожидание отпускания кнопки
    while (digitalRead(buttonPin) == LOW);
  }
}