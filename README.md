# directory-subscriber
Запуск проекта осуществляелся в два этапа

1.Запуск интерфейса пользователя (frontend).
Из папки с frontend подтягиваем зависимости - npm i
Из папки с frontend - npm start

2. Запуск модуля обработки данных (backend)

Для того, что бы подгрузилась схема валидации необходимо набрать mvn jsonschema2pojo:generate в папке backend.

Перед тестированием обогощения данных полем work_address необходимо заполнить бд h2 - id, firstName, lastName, work_address. Запись можно осуществить через h2_console, которая находится по адресу http://localhost:9111/h2_console

Запуск модуля происходит из папки backend - запуск BackendApplication

В resources/application.yml лежат настройки проекта:

file:
  name:
  
    filePathPrepare: /home/hightmike/testFiles/ - корневая папка в которой будут создаваться ключевые папки
    
настройки логирования:

logging:

  level:
    root: INFO
    ru.borisov: DEBUG
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
  
    name: /home/hightmike/testFiles/log/userInfo.log - папка куда пишутся логи по маршруту
    
Планировщик:
 scheduler:
  file:
  
    cron: '0/10+*+*+?+*+*+*' - частота с которой опрашивается ключевая папка на ниличие в ней файлов
