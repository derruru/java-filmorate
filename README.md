# java-filmorate
# Техническое задание 1

## Модели данных
Создайте пакет model. Добавьте в него два класса — Film и User. Это классы — модели данных приложения.
- У model.Film должны быть следующие свойства:
1) целочисленный идентификатор — id;
2) название — name;
3) описание — description;
4) дата релиза — releaseDate;
5) продолжительность фильма — duration.
- Свойства model.User:
1) целочисленный идентификатор — id;
2) электронная почта — email;
3) логин пользователя — login;
4) имя для отображения — name;
5) дата рождения — birthday.

## Хранение данных
Сейчас данные можно хранить в памяти приложения — так же, как вы поступили в случае с менеджером задач. Для этого используйте контроллер.
В следующих спринтах мы расскажем, как правильно хранить данные в долговременном хранилище, чтобы они не зависели от перезапуска приложения.

## REST-контроллеры
Создайте два класса-контроллера. FilmController будет обслуживать фильмы, а UserController — пользователей. Убедитесь, что созданные контроллеры соответствуют правилам REST.
Добавьте в классы-контроллеры эндпоинты с подходящим типом запроса для каждого из случаев.
- Для FilmController:
1) добавление фильма;
2) обновление фильма;
3) получение всех фильмов.
- Для UserController:
1) создание пользователя;
2) обновление пользователя;
3) получение списка всех пользователей.
4) Эндпоинты для создания и обновления данных должны также вернуть созданную или изменённую сущность.

## Валидация
Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя. Эти данные должны соответствовать определённым критериям.
- Для Film:
1) название не может быть пустым;
2) максимальная длина описания — 200 символов;
3) дата релиза — не раньше 28 декабря 1895 года;
4) продолжительность фильма должна быть положительной.
- Для User:
1) электронная почта не может быть пустой и должна содержать символ @;
2) логин не может быть пустым и содержать пробелы;
3) имя для отображения может быть пустым — в таком случае будет использован логин;
4) дата рождения не может быть в будущем.