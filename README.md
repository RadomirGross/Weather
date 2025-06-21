# Weather App 🌦️

Веб-приложение для просмотра текущей погоды в выбранных локациях. Пользователь может зарегистрироваться, добавлять города и отслеживать актуальные погодные данные с использованием OpenWeather API.

## 🚀 Функциональность

- Регистрация и вход в систему
- Добавление и удаление локаций (городов, населённых пунктов)
- Просмотр текущей погоды в добавленных локациях
- Интеграция с OpenWeather API
- Валидация форм и обработка ошибок
- Аутентификация сессий
- Интеграционные тесты с Testcontainers

## 🛠️ Технологии

- **Java 17**
- **Spring MVC**
- **Hibernate (JPA)**
- **Thymeleaf**
- **PostgreSQL**
- **Liquibase** (миграции базы данных)
- **JUnit 5 + Testcontainers**
- **Gradle**
- **Docker & Docker Compose**
- **Bootstrap 5**

## Запуск

### 1. Клонируем репозиторий
- git clone https://github.com/RadomirGross/Weather.git

### 2. Получаем ключ на https://openweathermap.org/
- Ключ копируем в docker-compose в 21 строку или передаем как переменную файлом .env.

### 3. Делаем WAR файл для запуска
- В терминале запускаем сборку проекта
```./gradlew clean build```
### 4. Запускаем приложение и БД в Docker
- docker-compose up --build

### 5. Приложение будет доступно по адресу:
-  http://localhost (порт 80 проброшен на 8080 приложения)

📄 [Техническое задание](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)  
(Автор курса: zhukovsd)

