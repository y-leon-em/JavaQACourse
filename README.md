# План тестирования WishList API

**Базовый URL:** `https://api.wishlist.otus.kartushin.su`
**Спецификация:** Swagger UI (`/swagger-ui/index.html`)
**Инструменты:** Postman / SOAP UI
**Версия документа:** 1.0

---
Дисклеймер: Проверка документации выявила отсутствие описания негативных статусов, поэтому ожидаемые результаты негативных сценариев основываются на предположениях о поведении системы
---

# Чек-лист проверок WishList API

## 1. `POST /api/auth/register`

**Метод:** POST | **Путь:** `/api/auth/register` | **Авторизация:** ❌

| Сценарий                       | Входные данные                                                 | Ожидаемый результат                                               | Фактический результат |
|--------------------------------|----------------------------------------------------------------|-------------------------------------------------------------------|-----------------------|
| Успешная регистрация           | `{username:"user01", email:"u1@mail.com", password:"Pass123"}` | `200 OK`, тело содержит `id`, `username`, `email`, без `password` | ✅PASSED               |
| Отсутствует `username`         | `{email:"u1@mail.com", password:"Pass123"}`                    | `400 Bad Request`                                                 | ❌ STATUS 403          |
| Отсутствует `email`            | `{username:"user01", password:"Pass123"}`                      | `400 Bad Request`                                                 | ❌ STATUS 403          |
| Отсутствует `password`         | `{username:"user01", email:"u1@mail.com"}`                     | `400 Bad Request`                                                 | ❌ STATUS 403          |
| `username` короче 3 символов   | `{username:"ab", ...}`                                         | `400 Bad Request` (minLength: 3)                                  | ❌ STATUS 403          |
| `username` длиннее 50 символов | `username` из 51 символа                                       | `400 Bad Request` (maxLength: 50)                                 | ❌ STATUS 200          |
| `password` короче 6 символов   | `{password:"12345", ...}`                                      | `400 Bad Request` (minLength: 6)                                  | ❌ STATUS 200          |
| Невалидный email               | `{email:"not-an-email", ...}`                                  | `400 Bad Request`                                                 | ❌ STATUS 200          |
| Дубликат `username`            | Повторная регистрация с тем же `username`                      | `409 Conflict` / `400`                                            | ❌ STATUS 200          |
| Дубликат `email`               | Повторная регистрация с тем же `email`                         | `409 Conflict` / `400`                                            | ❌ STATUS 200          |

---

## 2. `POST /api/auth/login`

**Метод:** POST | **Путь:** `/api/auth/login` | **Авторизация:** ❌

| Сценарий                    | Входные данные                            | Ожидаемый результат                   | Фактический результат |
|-----------------------------|-------------------------------------------|---------------------------------------|-----------------------|
| Успешный вход               | `{username:"user01", password:"Pass123"}` | `200 OK`, тело содержит `token` (JWT) | ✅PASSED               |
| Отсутствует `username`      | `{password:"Pass123"}`                    | `400 Bad Request`                     | ✅PASSED               |
| Отсутствует `password`      | `{username:"user01"}`                     | `400 Bad Request`                     | ✅PASSED               |
| Неверный пароль             | `{username:"user01", password:"wrong"}`   | `401 Unauthorized / 403`              | ✅PASSED               |
| Несуществующий пользователь | `{username:"ghost", password:"Pass123"}`  | `401 Unauthorized / 403`              | ✅PASSED               |

---

## 3. `GET /api/wishlists`

**Метод:** GET | **Путь:** `/api/wishlists` | **Авторизация:** ✅

| Сценарий                            | Входные данные                             | Ожидаемый результат                  | Фактический результат |
|-------------------------------------|--------------------------------------------|--------------------------------------|-----------------------|
| Пустой список у нового пользователя | Валидный `Bearer`-токен                    | `200 OK`, тело `[]`                  | ✅PASSED               |
| Есть несколько списков              | Валидный токен, 3 созданных списка         | `200 OK`, массив из 3 объектов       | ✅PASSED               |
| Структура элемента                  | Валидный токен                             | Каждый объект содержит `id`, `title` | ✅PASSED               |
| Отсутствующий токен                 |                                            | `401 Unauthorized / 403`             | ✅PASSED               |
| Невалидный JWT                      | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`             | ✅PASSED               |
| Истёкший токен                      | Старый JWT                                 | `401 Unauthorized / 403`             | ✅PASSED               |

---

## 4. `POST /api/wishlists`

**Метод:** POST | **Путь:** `/api/wishlists` | **Авторизация:** ✅

| Сценарий               | Входные данные                             | Ожидаемый результат                | Фактический результат |
|------------------------|--------------------------------------------|------------------------------------|-----------------------|
| Валидное создание      | `{title:"Birthday", description:"..."}`    | `200 OK`, объект с `id`, `title`   | ✅PASSED               |
| Только `title`         | `{title:"Birthday"}`                       | `200 OK`, `description: null`/`""` | ✅PASSED               |
| Отсутствует `title`    | `{description:"..."}`                      | `400 Bad Request`                  | ✅PASSED               |
| `title` > 255 символов | Строка из 300 символов                     | `400 Bad Request`                  | ✅PASSED               |
| Отсутствующий токен    |                                            | `401 Unauthorized / 403`           | ✅PASSED               |
| Невалидный JWT         | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`           | ✅PASSED               |
| Истёкший токен         | Старый JWT                                 | `401 Unauthorized / 403`           | ✅PASSED               |

---

## 5. `GET /api/wishlists/{id}`

**Метод:** GET | **Путь:** `/api/wishlists/{id}` | **Авторизация:** ✅

| Сценарий               | Входные данные                             | Ожидаемый результат              | Фактический результат |
|------------------------|--------------------------------------------|----------------------------------|-----------------------|
| Существующий `id`      | Валидный UUID своего списка                | `200 OK`, объект с `id`, `title` | ✅PASSED               |
| Несуществующий UUID    | `00000000-0000-0000-0000-000000000000`     | `404 Not Found`                  | ❌ STATUS 401          |
| Невалидный формат `id` | `id: "abc"`                                | `400 Bad Request`                | ❌ STATUS 403          |
| Отсутствующий токен    |                                            | `401 Unauthorized / 403`         | ✅PASSED               |
| Невалидный JWT         | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`         | ✅PASSED               |
| Истёкший токен         | Старый JWT                                 | `401 Unauthorized / 403`         | ✅PASSED               |

---

## 6. `PUT /api/wishlists/{id}`

**Метод:** PUT | **Путь:** `/api/wishlists/{id}` | **Авторизация:** ✅

| Сценарий                 | Входные данные                             | Ожидаемый результат             | Фактический результат |
|--------------------------|--------------------------------------------|---------------------------------|-----------------------|
| Обновление `title`       | `{title:"New title"}`                      | `200 OK`, `title` изменён       | ✅PASSED               |
| Обновление `description` | `{description:"new"}`                      | `200 OK`, `description` изменён | ✅PASSED               |
| Несуществующий `id`      | Валидный UUID, но не существует            | `404 Not Found`                 | ❌ STATUS 401          |
| Отсутствует `title`      | `{description:"new"}`                      | `400 Bad Request`               | ❌ STATUS 403          |
| Невалидный формат `id`   | `id: "abc"`                                | `400 Bad Request`               | ❌ STATUS 403          |
| Отсутствующий токен      |                                            | `401 Unauthorized / 403`        | ✅PASSED               |
| Невалидный JWT           | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`        | ✅PASSED               |
| Истёкший токен           | Старый JWT                                 | `401 Unauthorized / 403`        | ✅PASSED               |

---

## 7. `DELETE /api/wishlists/{id}`

**Метод:** DELETE | **Путь:** `/api/wishlists/{id}` | **Авторизация:** ✅

| Сценарий               | Входные данные                             | Ожидаемый результат         | Фактический результат |
|------------------------|--------------------------------------------|-----------------------------|-----------------------|
| Удаление существующего | Валидный UUID своего списка                | `200 OK` / `204 No Content` | ✅PASSED               |
| Несуществующий `id`    | Валидный UUID, не существует               | `404 Not Found`             | ❌ STATUS 401          |
| Невалидный формат `id` | `id: "abc"`                                | `400 Bad Request`           | ❌ STATUS 403          |
| Отсутствующий токен    |                                            | `401 Unauthorized / 403`    | ✅PASSED               |
| Невалидный JWT         | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`    | ✅PASSED               |
| Истёкший токен         | Старый JWT                                 | `401 Unauthorized / 403`    | ✅PASSED               |

---

## 8. `GET /api/wishlists/user/{userId}`

**Метод:** GET | **Путь:** `/api/wishlists/user/{userId}` | **Авторизация:** ✅

| Сценарий                   | Входные данные                             | Ожидаемый результат                  | Фактический результат |
|----------------------------|--------------------------------------------|--------------------------------------|-----------------------|
| Существующий `userId`      | UUID пользователя со списками              | `200 OK`, массив `WishList`          | ✅PASSED               |
| Пользователь без списков   | UUID без списков                           | `200 OK`, `[]`                       | ✅PASSED               |
| Несуществующий `userId`    | Валидный UUID, не существует               | `404` / `200 []` (задокументировать) | ✅PASSED               |
| Невалидный формат `userId` | `userId: "abc"`                            | `400 Bad Request`                    | ❌ STATUS 403          |
| Отсутствующий токен        |                                            | `401 Unauthorized / 403`             | ✅PASSED               |
| Невалидный JWT             | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`             | ✅PASSED               |
| Истёкший токен             | Старый JWT                                 | `401 Unauthorized / 403`             | ✅PASSED               |

---

## 9. `GET /api/gifts/wishlist/{wishListId}`

**Метод:** GET | **Путь:** `/api/gifts/wishlist/{wishListId}` | **Авторизация:** ✅

| Сценарий                    | Входные данные                             | Ожидаемый результат                                | Фактический результат |
|-----------------------------|--------------------------------------------|----------------------------------------------------|-----------------------|
| Пустой список подарков      | Валидный `wishListId` без подарков         | `200 OK`, `[]`                                     | ✅PASSED               |
| Несколько подарков          | Валидный `wishListId` с 3 подарками        | `200 OK`, массив из 3 объектов                     | ✅PASSED               |
| Структура элемента          | Валидный `wishListId`                      | Каждый элемент: `id`, `name`, `reserved` (boolean) | ✅PASSED               |
| Несуществующий `wishListId` | Валидный UUID, не существует               | `404 Not Found`                                    | ❌ STATUS 200 []       |
| Невалидный формат           | `wishListId: "abc"`                        | `400 Bad Request`                                  | ❌ STATUS 403          |
| Отсутствующий токен         |                                            | `401 Unauthorized / 403`                           | ✅PASSED               |
| Невалидный JWT              | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`                           | ✅PASSED               |
| Истёкший токен              | Старый JWT                                 | `401 Unauthorized / 403`                           | ✅PASSED               |

---

## 10. `POST /api/gifts/wishlist/{wishListId}`

**Метод:** POST | **Путь:** `/api/gifts/wishlist/{wishListId}` | **Авторизация:** ✅

| Сценарий                      | Входные данные                                                                                 | Ожидаемый результат                        | Фактический результат |
|-------------------------------|------------------------------------------------------------------------------------------------|--------------------------------------------|-----------------------|
| Полные валидные данные        | `{name:"Book", description:"...", price:1500, imageUrl:"...", storeUrl:"...", reserved:false}` | `200 OK`, объект с `id`, `reserved: false` | ✅PASSED               |
| Только `name`                 | `{name:"Book"}`                                                                                | `200 OK`, остальные = null/дефолт          | ❌ STATUS 403          |
| Отсутствует `name`            | `{price:1500}`                                                                                 | `400 Bad Request`                          | ❌ STATUS 403          |
| `name` > 255 символов         | Строка из 300 символов                                                                         | `400 Bad Request`                          | ❌ STATUS 403          |
| `description` > 1000 символов | Строка из 1500 символов                                                                        | `400 Bad Request`                          | ❌ STATUS 403          |
| `price` отрицательный         | `{name:"X", price:-100}`                                                                       | `400 Bad Request`                          | ❌ STATUS 403          |
| `price` строкой               | `{name:"X", price:"abc"}`                                                                      | `400 Bad Request`                          | ❌ STATUS 403          |
| `reserved` не boolean         | `{name:"X", reserved:"yes"}`                                                                   | `400 Bad Request`                          | ❌ STATUS 200          |
| `storeUrl` не по pattern      | `storeUrl:"not-a-url"`                                                                         | `400 Bad Request` (pattern в спецификации) | ❌ STATUS 403          |
| Несуществующий `wishListId`   | Валидный UUID, не существует                                                                   | `404 Not Found`                            | ❌ STATUS 401          |
| Отсутствующий токен           |                                                                                                | `401 Unauthorized / 403`                   | ✅PASSED               |
| Невалидный JWT                | `Authorization: Bearer invalid.token.here`                                                     | `401 Unauthorized / 403`                   | ✅PASSED               |
| Истёкший токен                | Старый JWT                                                                                     | `401 Unauthorized / 403`                   | ✅PASSED               |

---

## 11. `GET /api/gifts/{id}`

**Метод:** GET | **Путь:** `/api/gifts/{id}` | **Авторизация:** ✅

| Сценарий               | Входные данные                             | Ожидаемый результат      | Фактический результат |
|------------------------|--------------------------------------------|--------------------------|-----------------------|
| Существующий `id`      | Валидный UUID своего подарка               | `200 OK`, объект `Gift`  | ✅PASSED               |
| Несуществующий `id`    | Валидный UUID, не существует               | `404 Not Found`          | ❌ STATUS 401          |
| Невалидный формат `id` | `id: "abc"`                                | `400 Bad Request`        | ❌ STATUS 403          |
| Отсутствующий токен    |                                            | `401 Unauthorized / 403` | ✅PASSED               |
| Невалидный JWT         | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403` | ✅PASSED               |
| Истёкший токен         | Старый JWT                                 | `401 Unauthorized / 403` | ✅PASSED               |

---

## 12. `PUT /api/gifts/{id}`

**Метод:** PUT | **Путь:** `/api/gifts/{id}` | **Авторизация:** ✅

| Сценарий                 | Входные данные                             | Ожидаемый результат           | Фактический результат    |
|--------------------------|--------------------------------------------|-------------------------------|--------------------------|
| Обновление `name`        | `{name:"New name"}`                        | `200 OK`, `name` изменён      | ✅PASSED                  |
| Обновление `price`       | `{price:2000}`                             | `200 OK`, `price: 2000`       | ✅PASSED                  |
| Обновление `reserved`    | `{reserved:true}`                          | `200 OK`, `reserved: true`    | ❌ не обновилось значение |
| Обновление `description` | `{description:null}`                       | `200 OK`, `description: null` | ✅PASSED                  |
| Обновление `imageUrl`    | `{description:null}`                       | `200 OK`, `description: null` | ✅PASSED                  |
| Обновление `storeUrl`    | `{description:null}`                       | `200 OK`, `description: null` | ✅PASSED                  |
| Отсутствует `name`       | `{price:2000}`                             | `400 Bad Request`             | ❌ STATUS 403             |
| Отсутствует `price`      | `{name:"X"}`                               | `400 Bad Request`             | ❌ STATUS 403             |
| Несуществующий `id`      | Валидный UUID, не существует               | `404 Not Found`               | ❌ STATUS 401             |
| Отсутствующий токен      |                                            | `401 Unauthorized / 403`      | ✅PASSED                  |
| Невалидный JWT           | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`      | ✅PASSED                  |
| Истёкший токен           | Старый JWT                                 | `401 Unauthorized / 403`      | ✅PASSED                  |

---

## 13. `DELETE /api/gifts/{id}`

**Метод:** DELETE | **Путь:** `/api/gifts/{id}` | **Авторизация:** ✅

| Сценарий               | Входные данные                             | Ожидаемый результат         | Фактический результат |
|------------------------|--------------------------------------------|-----------------------------|-----------------------|
| Удаление существующего | Валидный UUID своего подарка               | `200 OK` / `204 No Content` | ✅PASSED               |
| Несуществующий `id`    | Валидный UUID, не существует               | `404 Not Found`             | ❌ STATUS 401          |
| Невалидный формат `id` | `id: "abc"`                                | `400 Bad Request`           | ❌ STATUS 403          |
| Отсутствующий токен    |                                            | `401 Unauthorized / 403`    | ✅PASSED               |
| Невалидный JWT         | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`    | ✅PASSED               |
| Истёкший токен         | Старый JWT                                 | `401 Unauthorized / 403`    | ✅PASSED               |

---

## 14. `PUT /api/gifts/{id}/reserve`

**Метод:** PUT | **Путь:** `/api/gifts/{id}/reserve` | **Авторизация:** ✅

| Сценарий                  | Входные данные                             | Ожидаемый результат        | Фактический результат |
|---------------------------|--------------------------------------------|----------------------------|-----------------------|
| Резерв свободного подарка | Валидный UUID, `reserved: false`           | `200 OK`, `reserved: true` | ✅PASSED               |
| Несуществующий `id`       | Валидный UUID, не существует               | `404 Not Found`            | ❌ STATUS 401          |
| Невалидный формат `id`    | `id: "abc"`                                | `400 Bad Request`          | ❌ STATUS 403          |
| Отсутствующий токен       |                                            | `401 Unauthorized / 403`   | ✅PASSED               |
| Невалидный JWT            | `Authorization: Bearer invalid.token.here` | `401 Unauthorized / 403`   | ✅PASSED               |
| Истёкший токен            | Старый JWT                                 | `401 Unauthorized / 403`   | ✅PASSED               |

---

## 15. `GET /api/users`

**Метод:** GET | **Путь:** `/api/users` | **Авторизация:** ❌ (по спецификации)

| Сценарий             | Входные данные | Ожидаемый результат                     | Фактический результат        |
|----------------------|----------------|-----------------------------------------|------------------------------|
| Список пользователей | —              | `200 OK`, массив `User`                 | ❌ STATUS 403 (200 с токеном) |
| Структура элемента   | —              | Каждый объект содержит `id`, `username` | ✅PASSED                      |

---

## 16. `GET /api/users/{userId}/wishlists`

**Метод:** GET | **Путь:** `/api/users/{userId}/wishlists` | **Авторизация:** ❌ (по спецификации)

| Сценарий                          | Входные данные               | Ожидаемый результат                  | Фактический результат        |
|-----------------------------------|------------------------------|--------------------------------------|------------------------------|
| Существующий `userId` со списками | Валидный UUID                | `200 OK`, массив `WishList`          | ❌ STATUS 403 (200 с токеном) |
| Пользователь без списков          | Валидный UUID                | `200 OK`, `[]`                       | ✅PASSED                      |
| Несуществующий `userId`           | Валидный UUID, не существует | `404` / `200 []` (задокументировать) | ✅PASSED 200 []               |
| Невалидный формат `userId`        | `userId: "abc"`              | `400 Bad Request`                    | ❌ STATUS 403                 |

---