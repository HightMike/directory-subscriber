# directory-subscriber
Запуск проекта осуществляелся в два этапа

# 1.Запуск интерфейса пользователя (frontend).
Из папки с frontend подтягиваем зависимости - **npm i**

Из папки с frontend - **npm start**

# 2. Запуск модуля обработки данных (backend)
Необходимо собрать проект использовав команду - **mvn clean install -Dmaven.test.skip=true**

После запускаем jar файл по пути - **java -jar pathToFile**, например **java -jar /home/userName/IdeaProjects/directory-subscriber/backend/target/backend-0.0.1-SNAPSHOT.jar**

Перед тестированием обогощения данных полем work_address необходимо заполнить бд h2 - id, firstName, lastName, work_address. Запись можно осуществить через h2_console, которая находится по адресу http://localhost:9111/h2_console

В resources/application.yml лежат настройки проекта:

file:
  name:
  
    **filePathPrepare: /home/userName/testFiles/ - корневая папка в которой будут создаваться ключевые папки**
    
настройки логирования:

logging:

  level:
    root: INFO
    ru.borisov: DEBUG
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
  
    name: /home/userName/testFiles/log/userInfo.log - папка куда пишутся логи по маршруту
    
Планировщик:
 scheduler:
  file:
  
    **cron: '0/10+*+*+?+*+*+*' - частота с которой опрашивается ключевая папка на ниличие в ней файлов**
