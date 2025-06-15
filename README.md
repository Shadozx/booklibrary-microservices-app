# BookLibrary — Microservices App

**BookLibrary Microservices App** — мікросервісна система для онлайн-читання книг. Проєкт реалізує масштабовану архітектуру з використанням Spring Boot, Spring Cloud, Eureka, Feign, JWT тощо.

## Архітектура

### Інфраструктура

- `config-server` — централізоване зберігання конфігурацій
- `discovery-server` — сервіс-реєстратор (Eureka)
- `gateway-server` — шлюз для взаємодії з мікросервісами

### Мікросервіси

| Сервіс                | Призначення                                                                 |
|------------------------|------------------------------------------------------------------------------|
| `library-service`      | Управління книгами, розділами, авторами та серіями книг                    |
| `userprofile-service`  | Закладки книг та каталоги користувачів                                     |
| `parser-service`       | Парсинг HTML-сторінок і текстів книг. Збереження контенту у `library-service` та `media-service` |
| `media-service`        | Робота із зображеннями (наприклад, обкладинки, ілюстрацій книг)                        |
| `feedback-service`     | Оцінки та коментарі користувачів                                            |
| `auth-service`         | Аутентифікація, реєстрація, видача JWT та управління користувачами         |
| `jwt-security`         | Бібліотека для спільної JWT-безпеки між сервісами                          |
| `api`                  | Спільні DTO, Feign-контракти                                                |
| `exception-handler`    | Глобальна обробка винятків                                                  |

---

## Технології

- Java 17
- Spring Boot
- Spring Cloud (Eureka, Config, OpenFeign)
- Spring Security + JWT
- Jsoup (HTML парсинг)
- Jackson (серіалізація JSON)
- Lombok (скорочення шаблонного коду)
- Spring Retry, AOP
- Spring Boot Actuator
- Gradle
- Docker, Docker Compose
  
---

## Запуск проєкту

### Вимоги

- Java 17
- Gradle 8+
- Docker + Docker Compose
- (Рекомендовано) IntelliJ IDEA

### Кроки

1. **Клонування репозиторію:**

```bash
git clone https://github.com/Shadozx/booklibrary-microservices-app.git
cd booklibrary-microservices-app
docker-compose up -d
