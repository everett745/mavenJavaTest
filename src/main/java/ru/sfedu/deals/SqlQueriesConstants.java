package ru.sfedu.deals;

public class SqlQueriesConstants {
  public static final String SQL_ALL_SCHEMA = "SELECT schema_name from information_schema.SCHEMATA";
  public static final String SQL_HELP = "SELECT topic from information_schema.HELP";
  public static final String SQL_ALL_TABLES = "SELECT table_name from information_schema.TABLES";
  public static final String SQL_ALL_USERS = "SELECT name from information_schema.USERS";

  public static final String SQL_SELECT_TEST_ENTITY = "from TestEntity";
  public static final String SQL_SELECT_TEST_ENTITY_BY_ID = "from TestEntity where id=%s";

  public static final String SQL_SELECT_ADDRESS_BY_ID = "from Address where id=%d";


  public static final String SQL_SELECT_CREDIT_ACCOUNT = "from CreditAccount";
  public static final String SQL_SELECT_DEBIT_ACCOUNT = "from DebitAccount";

  public static final String SQL_SELECT_LAB4_USER = "from User";

  public static final String HQL_SELECT_ADDRESS = "from Address";
  public static final String HQL_SELECT_USER = "from User";
  public static final String HQL_SELECT_DEAL = "from Deal";
  public static final String HQL_SELECT_COMPANY = "from Company";

  public static final String NSQL_SELECT_ADDRESS = "SELECT * FROM ADDRESS";
  public static final String NSQL_SELECT_USER = "SELECT * FROM USER";
  public static final String NSQL_SELECT_DEAL = "SELECT * FROM DEAL";
  public static final String NSQL_SELECT_COMPANY = "SELECT * FROM COMPANY";
}
