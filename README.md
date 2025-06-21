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

# 1. Клонируем репозиторий
git clone https://github.com/cakeslayer00/weather-app.git

# 2. Переходим в директорию проекта
cd weather-app

# 3. Запускаем приложение в Docker (не забудьте указать API-ключ в .env или в docker-compose.yml)
docker-compose up --build

# 4. Приложение будет доступно по адресу:
# http://localhost (порт 80 проброшен на 8080 приложения)

# 🛠 Для разработки:
# - Убедитесь, что настроен application.properties с корректной конфигурацией
# - Соберите и запустите приложение вручную:
./gradlew clean build
# После чего разверните скомпилированный WAR-файл в вашем локальном Tomcat
📄 [Техническое задание](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)  
(Автор курса: zhukovsd)

