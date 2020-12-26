Пример установки проперти файлов для .bat файла (windows):

@echo off
set log4j=properties/log4j2.properties
set properties=properties/environment.properties
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar <PROVIDER> <COMMAND_KEY> <PARAMS>

Пример установки проперти файлов для .sh файла (linux):

#!/bin/sh
LOG4J2="properties/log4j2.properties"
PROPERTIES="properties/config.properties"
java -Dlog4j2.configurationFile=$LOG4J2 -Dconfig=$PROPERTIES -jar deals.jar <PROVIDER> <COMMAND_KEY> <PARAMS>


<PROVIDER> - тип провайдера:
CSV  - DataProviderCSV
XML - DataProviderXML
JDBC - DataProviderJdbc

<COMMAND_KEY> - имя операции:

<PARAMS> - данные необходимые для выполнения операции. Смотреть в таблице детализации курсовой работы
Параметры необходимо передавать без пробелов, через ","
Если же есть желание использовать пробелы в названиях, то все параметры НЕОБХОДИМО ОБЕРНУТЬ в двойные ковычки. Например:
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar jdbc add_address "Moscow,Moscow region,Central"


Примеры команд для .bat (windows):

добавить новые адрес с использованием jdbc датапровайдера
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar jdbc add_address "Moscow,Moscow region,Central"

добавить нового пользователя с использованием xml датапровайдера
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar xml create_user Ivan,8921312342,0

удалить пользователя, не передавая параметры
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar jdbc DELETE_USER 2fccca71-34e3-4b16-9d28-3cb2d5a0577c

редактировать пользователя
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar xml edit_user 2fccca71-34e3-4b16-9d28-3cb2d5a0577c,IvanNew,81111111,0

получить адрес с id=1 с помощью датапровайдера xml (из хранилища xml)
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar xml GET_ADDRESS_BY_ID 1

получить все адреса (в рамках переданного датапровайдера)
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar jdbc GET_ADDRESSES

получить всех пользователей (в рамках переданного датапровайдера)
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar xml get_users

получить информацию о сделке
java -Dlog4j2.configurationFile=%log4j% -Dconfig=%properties% -jar deals.jar csv manage_deal 2fccca71-34e3-4b16-9d28-3cb2d5a0577c



Весь перечень команд <COMMAND_KEY>:
CREATE_USER - создать пользователя,
GET_USER - получить объект пользователя,
EDIT_USER - редактировать пользователя,
DELETE_USER - удалить пользователя,
GET_USERS - получить всех пользователей,
GET_ADDRESSES - полчить все адреса,
GET_ADDRESS_BY_ID - получить адрес по id,
GET_ADDRESS_BY_NAME - получить адрес по ключевому слову (название города),
ADD_ADDRESS - создать адрес,
REMOVE_ADDRESS - удалить адрес,
UPDATE_ADDRESS - редактировать адрес,
CREATE_DEAL - создать сделку,
CREATE_PUBLIC_DEAL - создать глобальную сделку,
GET_GLOBAL_DEALS - получить глобальные сделки,
GET_MY_DEALS - получить мои сделки,
MANAGE_DEAL - получить объект сделки,
REMOVE_DEAL - удалить сделку,
UPDATE_DEAL - обновить сделку,
SET_STATUS - обновить статус сделки (только для глобальной сделки),
ADD_DEAL_REQUEST - отправть запрос сделке на передачу,
GET_DEAL_QUEUE - получить очередь запросов на сделку,
MANAGE_DEAL_REQUEST - управление запросом к сделке,
ACCEPT_DEAL_REQUEST - подвердить запрос,
REFUSE_DEAL_REQUEST - отклонить запрос,
ADD_DEAL_PERFORMER - отправить запрос пользователю на передачу сделки,
GET_MY_QUEUE - получить очередь запросов к пользователю,
MANAGE_DEAL_PERFORM - управление запросом к сделке,
ACCEPT_DEAL_PERFORM - подвердить запрос,
REFUSE_DEAL_PERFORM - отклонить запрос,