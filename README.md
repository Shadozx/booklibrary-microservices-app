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
| `library-service`      | Робота з книгами, розділами, авторами, серіями книг                                    |
| `userprofile-service`  | Робота зі закладками книг та каталогами користувачів                                                        |
| `parser-service`       | Парсинг HTML-сторінок і текстів книг та збереження в сервісах `library-service` та `media-service`                                       |
| `media-service`        | Робота з зображеннями                                          |
| `feedback-service`     | Робота з оцінками та коментарями користувачів                                          |
| `auth-service`         | Аутентифікаці користувачів, видача JWT та робота з користувачами                                     |
| `jwt-security`         | Бібліотека для спільної безпеки на основі JWT                               |
| `api`                  | Спільні DTO, Feign-контракти                                                |
| `exception-handler`    | Глобальна обробка винятків                                                  |

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
- Docker Compose
