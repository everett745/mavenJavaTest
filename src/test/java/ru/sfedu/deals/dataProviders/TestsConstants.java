package ru.sfedu.deals.dataProviders;

import ru.sfedu.deals.enums.DealStatus;
import ru.sfedu.deals.enums.DealTypes;
import ru.sfedu.deals.enums.ObjectTypes;

import java.util.ArrayList;
import java.util.List;

public class TestsConstants {
  /* ADDRESS */
  public static final String TEST_ADDRESS_CITY = "Rostov-on-Don";
  public static final String TEST_ADDRESS_REGION = "Rostov region";
  public static final String TEST_ADDRESS_DISTRICT = "Southern";

  public static final String TEST_ADDRESS_CITY1 = "Moscow";
  public static final String TEST_ADDRESS_REGION1 = "Moscow region";
  public static final String TEST_ADDRESS_DISTRICT1 = "Central";

  /* USER */
  public static final String TEST_USER_1_ID = "5fe6d630-61fd-42a0-ac07-82b9ad68ced5";
  public static final String TEST_USER_1_NAME = "Ivan Test";
  public static final String TEST_USER_1_PHONE = "8922222222";

  public static final String TEST_USER_2_ID = "79356e63-da22-4048-978f-aae08265063a";
  public static final String TEST_USER_2_NAME = "Semen Test";
  public static final String TEST_USER_2_PHONE = "0000000";

  /* USER_QUEUE */
  public static final String TEST_USER_QUEUE_ID = "5fe6d630-61fd-42a0-ac07-82b9ad68ced5";
  public static final List<String> TEST_USER_QUEUE_ITEMS = new ArrayList<String>();

  /*DEAL*/
  public static final String TEST_DEAL_NAME = "New simple deal";
  public static final DealTypes TEST_DEAL_TYPE = DealTypes.PURCHASE;
  public static final String TEST_DEAL_PRICE = "150 000";

  public static final String TEST_PUBLIC_DEAL_NAME = "New public deal";
  public static final String TEST_PUBLIC_DEAL_DESCRIPTION = "public deal description";
  public static final DealTypes TEST_PUBLIC_DEAL_TYPE = DealTypes.SALE;
  public static final ObjectTypes TEST_PUBLIC_DEAL_OBJECT = ObjectTypes.LAND;
  public static final DealStatus TEST_PUBLIC_DEAL_STATUS = DealStatus.NEW_DEAL;
  public static final String TEST_PUBLIC_DEAL_PRICE = "123 000";
}
