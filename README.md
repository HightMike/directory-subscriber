# directory-subscriber
Запуск проекта осуществляелся в два этапа

1.Запуск интерфейса пользователя (frontend).
Из папки с frontend - npm start

2. Запуск модуля обработки данных (backend)
Для того, что бы подгрузилась схема валидации необходимо набрать mvn jsonschema2pojo:generate в папке с backend
Из папки с backend - запуск BackendApplication

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
