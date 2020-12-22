package ru.sfedu.maven1;

public class Constants {
    public static final String CONFIG_PATH = "config.path";

    public static final String CSV_PATH = "csv.path";
    public static final String CSV_EXTENSION = "csv.extension";

    public static final String XML_PATH = "xml.path";
    public static final String XML_EXTENSION = "xml.extension";

    public static final String DB_INIT_PATH = "db_init_path";
    public static final String DB_URL = "db_url";
    public static final String DB_USER = "db_user";
    public static final String DB_PASSWORD = "db_password";
    public static final String DB_DRIVER = "db_driver";

    public static final String EXCEPTION_CANNOT_CREATE_FILE = "unable to create file %s";

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    public static final String CONVERTER_REGEXP_LIST_WITHOUT_QUOTES = "^\\[";
    public static final String CONVERTER_REGEXP_LIST_WITH_QUOTES = "^\\\"\\[";
    public static final String ARRAY_DELIMITER = ",";
    public static final String EMPTY_STRING = "";


    /*MESSAGES*/
    public static final String UNDEFINED_USERS_LIST = "Users list not found";
    public static final String UNDEFINED_USERS_LIST_EMPTY = "Users list empty";
    public static final String UNDEFINED_USER = "User not found";
    public static final String UNDEFINED_USER_DATA = "Error while fetching user data";
    public static final String UNDEFINED_ADDRESS = "Address not found";
    public static final String UNDEFINED_ADDRESSES = "Addresses not found";

    public static final String UNDEFINED_DEALS_LIST = "Deals list not found";
    public static final String UNDEFINED_DEAL = "Deal not found";
    public static final String EMPTY_PUBLIC_DEALS = "Public deals list is empty";
    public static final String NOT_EMPTY_COMPANY = "Company must been empty";

    public static final String UNDEFINED_DEAL_HISTORY = "Deal history not found";
    public static final String EMPTY_DEAL_HISTORY = "Deal history is empty";
    public static final String DEAL_NOT_PUBLIC = "Deal not public";

    public static final String UNDEFINED_QUEUES_LIST = "Queues list not found";
    public static final String UNDEFINED_QUEUE_FOR_DELETE = "Error where delete queue";
    public static final String UNDEFINED_QUEUE = "Queue not found";
    public static final String UNDEFINED_USER_IN_DEAL_REQUESTS = "This user not send request on this deal";
    public static final String ALREADY_IN_QUEUE = "Queue already contain this uuid   ";
    public static final String ALREADY_PERFORMER = "This user already deal performer. You cant send request   ";
    public static final String ALREADY_DEAL_PERFORMER = "This deal already have performer. You cant been performer   ";
    public static final String UNDEFINED_PERFORM = "This deal not send by current user";
    public static final String UNDEFINED_USER_OR_DEAL = "User or deal not found";

    public static final String DELETE_FILE = "Delete file: ";
    public static final String CREATE_FILE = "Create file: ";

    // tables fields
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_ADDRESS = "address";
    public static final String COLUMN_USER_QUEUE = "queue";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_PHONE = "phone";

    public static final String COLUMN_DEAL_ID = "id";
    public static final String COLUMN_DEAL_NAME = "name";
    public static final String COLUMN_DEAL_DESCRIPTION = "description";
    public static final String COLUMN_DEAL_ADDRESS = "address";
    public static final String COLUMN_DEAL_REQUESTS = "requests";
    public static final String COLUMN_DEAL_OWNER = "owner";
    public static final String COLUMN_DEAL_PERFORMER = "performer";
    public static final String COLUMN_DEAL_TYPE = "dealType";
    public static final String COLUMN_DEAL_OBJECT = "object";
    public static final String COLUMN_DEAL_CREATED_AT = "created_at";
    public static final String COLUMN_DEAL_PRICE = "price";
    public static final String COLUMN_DEAL_MODEL = "dealModel";
    public static final String COLUMN_DEAL_STATUS = "current_status";

    public static final String COLUMN_ADDRESS_ID = "id";
    public static final String COLUMN_ADDRESS_CITY = "city";
    public static final String COLUMN_ADDRESS_REGION = "region";
    public static final String COLUMN_ADDRESS_DISTRICT = "district";

    public static final String COLUMN_QUEUE_ID = "id";
    public static final String COLUMN_QUEUE_ITEMS = "items";

    public static final String COLUMN_HISTORY_ID = "id";
    public static final String COLUMN_HISTORY_TEXT = "text";
    public static final String COLUMN_HISTORY_STATUS = "status";
    public static final String COLUMN_HISTORY_CREATED_AT = "created_at";


    // queries
    public static final String INSERT_USER = "INSERT INTO USER VALUES ('%s', '%s', '%s', '%s', '%s');";
    public static final String SELECT_USERS = "SELECT * FROM USER";
    public static final String SELECT_USER = "SELECT * FROM USER WHERE id='%s';";
    public static final String UPDATE_USER = "UPDATE USER SET address='%s', name='%s', phone='%s' WHERE id='%s';";
    public static final String DELETE_USER = "DELETE FROM USER WHERE id='%s'; DELETE FROM QUEUE WHERE id='%s';";

    public static final String INSERT_ADDRESS = "INSERT INTO ADDRESS VALUES (%d, '%s', '%s', '%s');";
    public static final String SELECT_ADDRESSES = "SELECT * FROM ADDRESS;";
    public static final String SELECT_ADDRESS_BY_ID = "SELECT * FROM ADDRESS WHERE id=%d;";
    public static final String SELECT_ADDRESS_BY_NAME = "SELECT * FROM ADDRESS WHERE LOWER(city) LIKE '%%%s%%';";

    public static final String INSERT_QUEUE = "INSERT INTO QUEUE VALUES ('%s', '%s');";
    public static final String SELECT_QUEUE = "SELECT * FROM QUEUE WHERE id='%s';";
    public static final String UPDATE_QUEUE = "UPDATE QUEUE SET items='%s' WHERE id='%s';";
    public static final String DELETE_QUEUE = "DELETE FROM QUEUE WHERE id='%s';";

    public static final String INSERT_DEAL = "INSERT INTO DEAL VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');";
    public static final String SELECT_DEAL = "SELECT * FROM DEAL WHERE id='%s';";
    public static final String DELETE_DEAL = "DELETE FROM DEAL WHERE id='%s'; DELETE FROM QUEUE WHERE id='%s'; DELETE DEAL_HISTORY DEAL WHERE id='%s'";
    public static final String UPDATE_DEAL = "UPDATE DEAL set name='%s', description='%s', dealType='%s', object='%s', price='%s' WHERE id='%s';";
    public static final String UPDATE_DEAL_PERFORMER = "UPDATE DEAL set performer='%s' WHERE id='%s';";

    public static final String UPDATE_DEAL_STATUS = "UPDATE DEAL set current_status='%s' WHERE id='%s';";

    public static final String SELECT_MY_DEALS = "SELECT * FROM DEAL WHERE owner='%s';";
    public static final String SELECT_GLOBAL_DEALS = "SELECT * FROM DEAL WHERE owner!='%s' AND dealModel='PUBLIC';";

    public static final String INSERT_DEAL_HISTORY = "INSERT INTO DEAL_HISTORY VALUES ('%s', '%s', '%s', '%s');";
    public static final String SELECT_DEAL_HISTORY = "SELECT * FROM DEAL_HISTORY WHERE id='%s';";

    public static final String CLEAR_BD = "drop table if exists company cascade; drop table if exists deal cascade; drop table if exists dealHistory cascade; drop table if exists publicDeal cascade; drop table if exists queue cascade; drop table if exists user cascade;";
}
