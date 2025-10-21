# Система управления банковскими картами
Проект в рамках тестового задания на позицию Java-разработчика в компании Effective Mobile.

## Общая цель
Разработать backend-приложение на Java (Spring Boot) для управления банковскими картами (создание и управление, просмотр, переводы), с поддержкой ролей доступа (типы пользователей ADMIN и USER).

Требуемые технологии в соответствии с условиями задания: Java 17+, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL/MySQL, Liquibase, Docker, JWT, Swagger (OpenAPI)

Полные условия задания: [https://gitlab.com/PaatoM/bank_rest/-/blob/6224547ce919f0e28fe7c76871d27aa6c74fad0f/README.md](https://github.com/m-kulygin/bank_rest/blob/f3e4f4a45a0b1e50210ed77207279099a7b0270d/README-origin.md)

## Технологии
- Язык программирования: Java 17 (Microsoft OpenJDK 17.0.16)
- Среда разработки: IntelliJ IDEA 2024.3 (Ultimate Edition)
- Сборка: Maven 3.9.9
- Spring Boot 3.5.6 (Web, Data JPA, Security, Test)
- OpenAPI: springdoc-openapi-starter-webmvc-ui 2.5.0
- Токены аутентификации: JWT 0.12.3
- Тестирование: JUnit Jupiter 5.7.0, Mockito 5.18.0, ByteBuddy 1.17.7 
- Контейнеризация: Docker 28.4.0
- БД: PostgreSQL 42.7.7 + миграции Liquibase 4.29.2
- Образы в контейнерах: openjdk:17-jdk-slim для приложения, postgres latest для БД
- Другое: lombok 1.18.30

## Инструкция по запуску
1. Убедиться, что на устройстве:
   - установлена Java 17+
   - установлен сборщик Maven
   - установлен Docker
2. Скачать папку проекта или склонировать репозиторий на устройство.
3. Открыть проект в IDE или перейти по пути проекта в консоли.
4. Запустить сборку через Maven (`mvn package`). Дождаться выполнения. Автотесты прогоняются на этом этапе.
5. Собрать контейнеры командой `docker compose up --build`.
6. В браузере перейти по адресу http://localhost:8080/swagger-ui/index.html . Должна открыться интерактивная swagger-страница с возможностью дёргать эндпоинты сервиса.  
   <img width="700" alt="image" src="https://github.com/user-attachments/assets/2eeaff7b-b074-4b52-a5e2-c4f6d213aa3e" />
7. Для дальнейшего использования бизнес-ручек необходимо авторизоваться, используя эндпоинт `/auth/sign-in` у контроллера авторизации, нажав "Try it out" у соответствующей ручки и указав логин и пароль в request body.  
   <img width="700" alt="image" src="https://github.com/user-attachments/assets/f77c5b84-b20e-400f-8231-e57567275401" />  
   Логин указывается любой из имеющихся в базе:  
   <img width="300" alt="image" src="https://github.com/user-attachments/assets/67b730a8-b794-401d-9824-03da770883eb" />  
   Пароль для всех имеющихся пользователей: 12345678  
   После заполнения аутентификационных данных нажать "Execute". Запрос должен вернуть ответ с JWT-токеном авторизации.  
   <img width="700" alt="image" src="https://github.com/user-attachments/assets/e7b19cb4-5608-48a0-88e3-57bd791fb16d" />  
   Полученное значение токена скопировать.  
   <img width="700" alt="image" src="https://github.com/user-attachments/assets/57fadc82-0faa-4279-9ae5-ac05f079d258" />  
8. В верхней части swagger-страницы нажать кнопку Authorize, ввести в поле скопированный токен, нажать Authorize. В дальнейшем через эту же кнопку можно делать Logout.
   <img width="700" alt="image" src="https://github.com/user-attachments/assets/64081295-7b3a-4222-ac92-3c7c39c00205" />  
9. По необходимости осуществлять эндпоинт-запросы для взаимодействия с системой. Необходимые для выполнения каждого запроса права доступа (USER/ADMIN) указаны в описании запросов.
10. По завершении остановить контейнеры в докере.
