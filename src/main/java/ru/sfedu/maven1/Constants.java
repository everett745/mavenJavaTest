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
    public static final String UNDEFINED_ADDRESS = "Address not found";

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

    public static final String DELETE_FILE = "Delete file: ";
    public static final String CREATE_FILE = "Create file: ";

    // tables fields
    public static final String USER_ID = "id";
    public static final String USER_ADDRESS = "address";
    public static final String USER_QUEUE = "queue";
    public static final String USER_NAME = "name";
    public static final String USER_PHONE = "phone";

    public static final String ADDRESS_ID = "id";
    public static final String ADDRESS_CITY = "city";
    public static final String ADDRESS_REGION = "region";
    public static final String ADDRESS_DISTRICT = "district";

    public static final String QUEUE_ID = "id";
    public static final String QUEUE_ITEMS = "items";


    // queries
    public static final String INSERT_USER = "INSERT INTO USER VALUES ('%s', '%s', '%s', '%s', '%s');";
    public static final String SELECT_USERS = "SELECT * FROM USER";
    public static final String SELECT_USER = "SELECT * FROM USER WHERE id LIKE '%s';";
    public static final String UPDATE_USER = "UPDATE USER SET address='%s', queue='%s', name='%s', phone='%s' WHERE id LIKE '%s';";
    public static final String DELETE_USER = "DELETE FROM USER WHERE id LIKE '%s';";

    public static final String INSERT_ADDRESS = "INSERT INTO ADDRESS VALUES (%d, '%s', '%s', '%s');";
    public static final String SELECT_ADDRESSES = "SELECT * FROM ADDRESS;";
    public static final String SELECT_ADDRESS = "SELECT * FROM ADDRESS WHERE id=%d;";

    public static final String INSERT_QUEUE = "INSERT INTO QUEUE VALUES ('%s', '%s');";
    public static final String SELECT_QUEUE = "SELECT * FROM QUEUE WHERE id LIKE '%s';";


    public static final String CLEAR_BD = "drop table if exists company cascade; drop table if exists deal cascade; drop table if exists dealHistory cascade; drop table if exists publicDeal cascade; drop table if exists queue cascade; drop table if exists user cascade;";
}
