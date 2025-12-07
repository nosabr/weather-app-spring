# 🌤️ Weather App - Spring MVC

Веб-приложение для отслеживания погоды в избранных городах с использованием Spring MVC, Hibernate и OpenWeather API.

## 📋 Описание

Weather App позволяет пользователям:
- Регистрироваться и авторизоваться в системе
- Искать города по названию
- Добавлять города в список избранных
- Просматривать актуальную погоду для избранных локаций
- Управлять своим списком отслеживаемых городов

## 🛠️ Технологии

### Backend
- **Java 17**
- **Spring Framework 6.2.11** (Core, MVC, ORM, JDBC)
- **Hibernate 6.6.4** - ORM для работы с БД
- **PostgreSQL 42.7.8** - основная БД
- **Flyway** - миграции БД
- **Apache Commons DBCP2** - connection pool
- **BCrypt** - хеширование паролей

### Frontend
- **Thymeleaf 3.1.2** - шаблонизатор
- **HTML/CSS/JavaScript**

### Testing
- **JUnit 5**
- **Mockito**
- **H2 Database** - in-memory БД для тестов
- **Spring Test**
- **AssertJ**

### Tools
- **Maven** - сборка проекта
- **Lombok** - уменьшение boilerplate кода
- **Jackson** - работа с JSON
- **SLF4J + Logback** - логирование

## 📁 Структура проекта

```
weather-app-spring/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── config/          # Конфигурация Spring
│   │   │   ├── controller/      # Spring MVC контроллеры
│   │   │   ├── dao/             # Data Access Objects
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Кастомные исключения
│   │   │   ├── filter/          # Servlet фильтры
│   │   │   ├── model/           # Entity модели
│   │   │   ├── service/         # Бизнес-логика
│   │   │   └── util/            # Утилиты
│   │   ├── resources/
│   │   │   ├── db/migration/    # Flyway миграции
│   │   │   ├── application.properties
│   │   │   ├── database.properties
│   │   │   └── logback.xml
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── views/       # Thymeleaf шаблоны
│   │       └── static/          # CSS, JS, изображения
│   └── test/                    # Тесты
├── pom.xml
└── README.md
```

## 🚀 Установка и запуск

### Предварительные требования

- Java 17+
- Maven 3.6+
- PostgreSQL 12+ или Docker
- Tomcat 10+
- OpenWeather API ключ ([получить бесплатно](https://openweathermap.org/api))

### 1. Клонирование репозитория

```bash
git clone https://github.com/yourusername/weather-app-spring.git
cd weather-app-spring
```

### 2. Настройка базы данных

#### Вариант A: PostgreSQL через Docker

```bash
docker run -d \
  --name weather-postgres \
  -e POSTGRES_DB=weather_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:16-alpine
```

#### Вариант B: Локальный PostgreSQL

```sql
CREATE DATABASE weather_db;
CREATE USER postgres WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE weather_db TO postgres;
```

### 3. Настройка переменных окружения

#### Для разработки в IntelliJ IDEA:

1. **Run → Edit Configurations...**
2. Выберите конфигурацию Tomcat
3. **Startup/Connection → Environment Variables**
4. Добавьте переменные:

```
DB_URL=jdbc:postgresql://localhost:5432/weather_db
DB_USERNAME=postgres
DB_PASSWORD=password
OPENWEATHER_API_KEY=your_api_key_here
```

#### Для Tomcat на сервере:

Создайте файл `$TOMCAT_HOME/bin/setenv.sh`:

```bash
#!/bin/bash

export DB_URL="jdbc:postgresql://localhost:5432/weather_db"
export DB_USERNAME="postgres"
export DB_PASSWORD="password"
export OPENWEATHER_API_KEY="your_api_key_here"
export SPRING_PROFILES_ACTIVE="prod"
```

Дайте права на выполнение:
```bash
chmod +x $TOMCAT_HOME/bin/setenv.sh
```

### 4. Сборка проекта

```bash
mvn clean package
```

WAR файл будет создан в `target/weather-app-spring.war`

### 5. Деплой

#### Локально в IntelliJ IDEA:
- Настройте Tomcat Server в Run Configurations
- Запустите приложение (Shift + F10)

#### На сервер:
```bash
# Скопируйте WAR в Tomcat
cp target/weather-app-spring.war $TOMCAT_HOME/webapps/

# Перезапустите Tomcat
$TOMCAT_HOME/bin/shutdown.sh
$TOMCAT_HOME/bin/startup.sh
```

### 6. Доступ к приложению

Откройте в браузере:
```
http://localhost:8080/weather-app-spring
```

## 🔧 Конфигурация

### Профили Spring

Приложение поддерживает три профиля:

- **dev** (по умолчанию) - для разработки с PostgreSQL
- **test** - для тестов с H2 in-memory БД
- **prod** - для production окружения

Активация профиля:
```bash
export SPRING_PROFILES_ACTIVE=prod
```

### Настройки БД (database.properties)

```properties
db.url=${DB_URL}
db.username=${DB_USERNAME}
db.password=${DB_PASSWORD}
db.driver=org.postgresql.Driver

# Connection Pool
db.initialSize=5
db.maxTotal=10
db.maxIdle=5
db.minIdle=2
```

### Настройки приложения (application.properties)

```properties
app.name=Weather App
app.version=1.0
session.duration.seconds=3600
openweather.api.key=${OPENWEATHER_API_KEY}
```

## 🧪 Тестирование

Запуск всех тестов:
```bash
mvn test
```

Запуск с отчетом о покрытии:
```bash
mvn test jacoco:report
```

## 📦 Деплой на production

### Ubuntu 22.04 + Docker + Tomcat 10

Полная инструкция по деплою доступна в [DEPLOYMENT.md](DEPLOYMENT.md)

Краткая версия:

1. **Подготовка сервера:**
```bash
# Обновление системы
sudo apt update && sudo apt upgrade -y

# Установка Java 17
sudo apt install openjdk-17-jdk -y

# Установка Docker
curl -fsSL https://get.docker.com | sh
```

2. **PostgreSQL через Docker:**
```bash
cd /opt/weather-app
docker-compose up -d
```

3. **Установка Tomcat 10:**
```bash
cd /opt
wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.49/bin/apache-tomcat-10.1.49.tar.gz
tar -xzf apache-tomcat-10.1.49.tar.gz
mv apache-tomcat-10.1.49 tomcat10
```

4. **Настройка переменных окружения** (см. выше)

5. **Деплой приложения:**
```bash
cp weather-app-spring.war /opt/tomcat10/webapps/
systemctl restart tomcat
```

## 🔐 Безопасность

- Пароли хранятся в виде BCrypt хешей
- Сессии с настраиваемым временем жизни
- Фильтры аутентификации для защищенных эндпоинтов
- Переменные окружения для конфиденциальных данных
- CSRF защита (рекомендуется добавить Spring Security)

## 📝 API Endpoints

### Публичные
- `GET /` - главная страница
- `GET /auth/sign-up` - регистрация
- `POST /auth/sign-up` - обработка регистрации
- `GET /auth/sign-in` - авторизация
- `POST /auth/sign-in` - обработка авторизации

### Защищенные (требуют авторизации)
- `GET /main` - список избранных городов с погодой
- `GET /search` - поиск городов
- `POST /locations` - добавление города в избранное
- `DELETE /locations/{id}` - удаление города из избранного
- `POST /auth/logout` - выход из системы

## 🌍 Интеграция с OpenWeather API

Приложение использует [OpenWeather API](https://openweathermap.org/api) для получения данных о погоде:

- Current Weather Data API
- Geocoding API для поиска городов

Получите бесплатный API ключ на https://openweathermap.org/api

## 🐛 Известные проблемы и TODO

- [ ] Добавить Spring Security для улучшенной безопасности
- [ ] Реализовать прогноз погоды на несколько дней
- [ ] Добавить уведомления о критических погодных условиях
- [ ] Кэширование запросов к OpenWeather API
- [ ] Internationalization (i18n) для поддержки нескольких языков
- [ ] Responsive дизайн для мобильных устройств

## 📄 Лицензия

MIT License

## 👤 Автор

Ваше имя - [GitHub Profile](https://github.com/yourusername)

## 🤝 Вклад в проект

Pull requests приветствуются! Для серьезных изменений сначала откройте issue для обсуждения.

---

⭐ Если проект оказался полезным, поставьте звезду на GitHub!
