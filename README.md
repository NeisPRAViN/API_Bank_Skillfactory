# Java Bank API

Это RESTful API, разработанный для симуляции операций в интернет-банке. Проект создан в рамках итоговой работы по курсу "Разработчик - Java".

## Используемые технологии
* **Java 17**
* **Spring Boot 3.2.1**: Фреймворк для создания приложения.
* **Spring Data JPA**: Для удобной работы с базой данных.
* **PostgreSQL**: Реляционная база данных для хранения данных.
* **Maven**: Система сборки и управления зависимостями.

## Функциональность API

API предоставляет следующие операции, доступные через HTTP-запросы:

| Операция | HTTP-метод | Эндпоинт | Описание |
| :--- | :---: | :--- | :--- |
| **Пополнение счета** | `POST` | `/api/account/deposit` | Пополняет баланс счета на указанную сумму. |
| **Снятие средств** | `POST` | `/api/account/withdraw` | Снимает средства со счета, если баланс достаточен. |
| **Перевод между счетами** | `POST` | `/api/transactions/transfer` | Переводит деньги от одного пользователя другому с проверкой баланса. |
| **Получение баланса** | `GET` | `/api/account/balance` | Возвращает текущий баланс по ID счета. |
| **История операций** | `GET` | `/api/operations` | Отображает список всех операций по ID счета, с возможностью фильтрации по временному диапазону. |

## Как запустить проект

1.  **Клонируйте репозиторий:**
    ```bash
    git clone [https://www.nic.ru/info/blog/repository/](https://www.nic.ru/info/blog/repository/)
    ```
2.  **Настройка базы данных:**
    * Установите **PostgreSQL**.
    * Создайте базу данных и настройте параметры подключения в файле `src/main/resources/application.properties`.
    * Приложение автоматически создаст необходимые таблицы (`accounts` и `operations`) при первом запуске, если настроен `spring.jpa.hibernate.ddl-auto=update`.
3.  **Запуск приложения:**
    * Откройте проект в IDE (например, IntelliJ IDEA).
    * Запустите главный класс `BankApplication.java`.

## Примеры использования с Postman

* **Пополнение:** `POST http://localhost:8080/api/account/deposit?accountId=1&amount=500.00`
* **Снятие:** `POST http://localhost:8080/api/account/withdraw?accountId=1&amount=100.00`
* **Перевод:** `POST http://localhost:8080/api/transactions/transfer?senderId=1&recipientId=2&amount=50.00`
* **Получение баланса:** `GET http://localhost:8080/api/account/balance?accountId=1`
* **История операций:** `GET http://localhost:8080/api/operations?accountId=1&startDate=2025-01-01T00:00:00&endDate=2025-09-26T00:00:00`

---
