# Overview

## Программа реализует набор сервисов для WEB приложения

- Регистрация пользователя, с указанием имени и пароля. Имя - уникально у каждого пользователя. Пароль хранится в базе данных в зашифрованном виде (BCryptPasswordEncoder);
- Незарегистрированный пользователь может проверить доступность имени через сервис валидации: /validation ;
- Созданный пользователь может авторизоваться, введя имя и пароль по адресу: /login . Количество неудачных попыток ограничено (по умолчанию = 3), после этого доступ к аккаунту блокируется (по умолчанию на 5 минут). После удачной авторизации, счетчик обнуляется;
- Неавторизированный пользователь имеет доступ только к /login, /registration и /validation ;
- Авторизированный пользователь не имеет доступ к /registration и /validation, но ему становятся доступны /animals, /pets, /pets/nickname, /pets/type/ и он может осуществлять различные запросы по этим адресам;
- Пользователь может получить список всех животных или каждого по отдельности (/animals/);
- Пользователю доступна информация о всех своих питомцах (/pets/), а также добавление / удаление / изменение их параметров (Тип, Кличка (уникальна), Пол, Дата рождения)
- Все взаимодействие происходит с использованием JSON формата данных

Перед запуском программы необходимо создать базу данных и настроить доступ к ней в файле resources/application.properties

    server.port=8083
    spring.datasource.url=jdbc:postgresql://localhost:5432/PetService
    spring.datasource.username="username"
    spring.datasource.password="12345"

Примеры запросов для тестиования программы:

1. GET -> http://localhost:8083/registration   - переход на /registration
2. POST -> http://localhost:8083/registration?username=user1&password=12345    - регистрация пользователя с именем "user1" и паролем "12345"
3. POST -> http://localhost:8083/validation?username=user1   - проверка доступности имени
4. POST -> http://localhost:8083/login?username=user1&password=12345   - попытка входа в систему с именем "user1" и паролем "12345"
5. GET -> http://localhost:8083/animals/   - получение списка всех животных
6. GET -> http://localhost:8083/animals/DOG   - получение информации о конкретном виде
7. GET -> http://localhost:8083/pets/   - получение списка всех своих питомцев
8. GET -> http://localhost:8083/pets/nickname/киса   - получение данных питомца по имени
9. GET -> http://localhost:8083/pets/type/CAT   - получение питомцев указанного типа
10. POST -> http://localhost:8083/pets/?nickName=sharik&gender=MALE&dateBirth=13.07.2021&type=DOG   - добавление питомца
11. PUT -> http://localhost:8083/pets/nickname/киса?nickName=дружок&dateBirth=01.01.2020   - изменение данных питомца
12. DELETE -> http://localhost:8083/pets/nickname/дружок   - удаление питомца по имени
13. GET -> http://localhost:8083/logout   - выход из системы


### Стек технологий: 
- Java
- Spring Boot
- Spring Data Jpa
- Spring Security
- PostgreSQL
- Log4j
- Jackson
