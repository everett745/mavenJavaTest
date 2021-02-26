package ru.sfedu.deals;

import javax.management.Query;

public class SqlQueriesConstants {
  public static final String SQL_ALL_SCHEMA = "SELECT schema_name from information_schema.SCHEMATA";
  public static final String SQL_HELP = "SELECT topic from information_schema.HELP";
  public static final String SQL_ALL_TABLES = "SELECT table_name from information_schema.TABLES";
  public static final String SQL_ALL_USERS = "SELECT name from information_schema.USERS";

  public static final String SQL_SELECT_TEST_ENTITY = "from TestEntity";
  public static final String SQL_DELETE_TEST_ENTITY = "delete from TestEntity where id='%s'";
  public static final String SQL_UPDATE_TEST_ENTITY = "UPDATE test_entity set name='%s', description='%s', datecreate='%s', check_='%s' where id='%s'";

  public static final String SQL_SELECT_ADDRESS_BY_ID = "from Address where id=%d";


  public static final String SQL_SELECT_CREDIT_ACCOUNT = "from CreditAccount";
  public static final String SQL_SELECT_DEBIT_ACCOUNT = "from DebitAccount";

  public static final String SQL_SELECT_LAB4_ENTITY = "from Lab4Entity";
}
