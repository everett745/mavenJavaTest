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


    /*TEST*/
    public static final UUID TEST_USER_ID = UUID.fromString("5fe6d630-61fd-42a0-ac07-82b9ad68ced5");
    public static final String TEST_USER_NAME = "Иван Тест";
    public static final String TEST_USER_PHONE = "8922222222";
}
