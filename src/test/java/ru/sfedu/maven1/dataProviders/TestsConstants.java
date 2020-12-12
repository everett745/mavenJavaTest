package ru.sfedu.maven1.dataProviders;

import ru.sfedu.maven1.enums.DealModel;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestsConstants {
  /* ADDRESS */
  public static final long TEST_ADDRESS_ID = 744;
  public static final String TEST_ADDRESS_CITY = "Ростов-на-Дону";
  public static final String TEST_ADDRESS_REGION = "Ростовская область";
  public static final String TEST_ADDRESS_DISTRICT = "Южный";

  /* USER */
  public static final UUID TEST_USER_1_ID = UUID.fromString("5fe6d630-61fd-42a0-ac07-82b9ad68ced5");
  public static final String TEST_USER_1_NAME = "Иван Тест";
  public static final String TEST_USER_1_PHONE = "8922222222";

  public static final UUID TEST_USER_2_ID = UUID.fromString("79356e63-da22-4048-978f-aae08265063a");
  public static final String TEST_USER_2_NAME = "Петр Тест";
  public static final String TEST_USER_2_PHONE = "0000000";

  /* USER_QUEUE */
  public static final UUID TEST_USER_QUEUE_ID = UUID.fromString("5fe6d630-61fd-42a0-ac07-82b9ad68ced5");
  public static final List<UUID> TEST_USER_QUEUE_ITEMS = new ArrayList<UUID>();


  /*DEAL*/
  public static final String TEST_DEAL_NAME = "Новая обычная сделка";
  public static final String TEST_DEAL_DESCRIPTION = "тестовое описание сделки";
  public static final DealTypes TEST_DEAL_TYPE = DealTypes.PURCHASE;
  public static final ObjectTypes TEST_DEAL_OBJECT = ObjectTypes.BUILDING;
  public static final String TEST_DEAL_PRICE = "150 000";

  public static final String TEST_PUBLIC_DEAL_NAME = "Новая публичная сделка";
  public static final String TEST_PUBLIC_DEAL_DESCRIPTION = "тестовое описание публичной сделки";
  public static final DealTypes TEST_PUBLIC_DEAL_TYPE = DealTypes.SALE;
  public static final ObjectTypes TEST_PUBLIC_DEAL_OBJECT = ObjectTypes.LAND;
  public static final DealStatus TEST_PUBLIC_DEAL_STATUS = DealStatus.NEW_DEAL;
  public static final String TEST_PUBLIC_DEAL_PRICE = "123 000";

  /* DEAL_QUEUE */
  public static final UUID TEST_DEAL_QUEUE_ID = UUID.fromString("5fe6d630-61fd-42a0-ac07-82b9ad68ced5");
  public static final UUID[] TEST_DEAL_QUEUE_ITEMS = new UUID[] {};



  /*ERRORS MESSAGES*/
  public static final String ERROR_GET_USER_BY_ID = "Fail test -- getUserByIdCorrect";
  public static final String ERROR_USERS_LIST_EMPTY = "Users list is empty";
  public static final String ERROR_GET_USER_FROM_LIST = "Fail test -- getUserFromList";

}
