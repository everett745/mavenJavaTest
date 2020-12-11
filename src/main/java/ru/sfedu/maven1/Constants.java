package ru.sfedu.maven1;

import java.util.UUID;

public class Constants {
    public static final String CONFIG_PATH = "config.path";
    public static final String CSV_PATH = "csv.path";
    public static final String CSV_EXTENSION = "csv.extension";
    public static final String XML_PATH = "xml.path";
    public static final String XML_EXTENSION = "xml.extension";
    public static final String EXCEPTION_CANNOT_CREATE_FILE = "unable to create file %s";


    public static final String CONVERTER_REGEXP_LIST_WITHOUT_QUOTES = "^\\[((([0-9]+,)*[0-9]+))?\\]$";
    public static final String CONVERTER_REGEXP_LIST_WITH_QUOTES = "^\\{(([0-9]+:.+,)*([0-9]+:[^,]+]*))?\\}$";
    public static final String ARRAY_DELIMITER = ",";


    /*MESSAGES*/
    public static final String UNDEFINED_USERS_LIST = "Users list not found";
    public static final String UNDEFINED_USER = "User not found";

    public static final String UNDEFINED_DEALS_LIST = "Deals list not found";
    public static final String UNDEFINED_DEAL = "Deal not found";

    public static final String UNDEFINED_QUEUES_LIST = "Queues list not found";
    public static final String UNDEFINED_QUEUE_FOR_DELETE = "Error where delete queue";
    public static final String UNDEFINED_QUEUE = "Queue not found";
    public static final String ALREADY_IN_QUEUE = "Queue already contain this uuid   ";
    public static final String ALREADY_PERFORMER = "This user already deal performer. You cant send request   ";
    public static final String ALREADY_DEAL_PERFORMER = "This deal already have performer. You cant been performer   ";
    public static final String UNDEFINED_PERFORM = "Perform not found";
}
