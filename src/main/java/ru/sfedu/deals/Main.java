package ru.sfedu.deals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import ru.sfedu.deals.dataProviders.*;
import ru.sfedu.deals.enums.*;
import ru.sfedu.deals.lab3.joinedTable.CreditAccount;
import ru.sfedu.deals.lab4.set.User;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.model.AddressT;
import ru.sfedu.deals.model.TestEntity;

import java.math.BigDecimal;
import java.util.*;

public class Main {
  private static final Logger log = (Logger) LogManager.getLogger(Main.class);

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    System.err.close();
    System.setErr(System.out);
    List<String> command = Arrays.asList(args);
    String providerType;
    String methodKey;
    String subMethodKey;
    String methodParams = "";
    try {
      providerType = command.get(0);

      if (CliHBKeys.valueOf(providerType.toUpperCase()) == CliHBKeys.HB) {
        methodKey = command.get(1);
        subMethodKey = command.get(2);
        log.debug(methodKey);
        if (CliHBKeys.valueOf(methodKey.toUpperCase()) != CliHBKeys.HB_LAB1) {
          methodParams = command.get(3);
        }
        log.info("RESULT: " + executeHBCommand(methodKey, subMethodKey, methodParams));
      } else {
        methodKey = command.get(1);
        if (command.size() == 3) {
          methodParams = command.get(2);
        }
        log.info("RESULT: " + executeCommand(getDataSource(providerType), methodKey, methodParams));
      }
    } catch (Exception e) {
      log.error(Constants.WRONG_COMMAND);
      log.error(e);
    }
  }

  /**
   * Получить датапровайдер по переданному ключу
   *
   * @param key ключ датапровайдера
   * @return датапровайдер
   */
  private static IDataProvider getDataSource(String key) {
    switch (CliKeys.valueOf(key.toUpperCase())) {
      case CSV:
        return new DataProviderCSV();
      case XML:
        return new DataProviderXML();
      case JDBC:
        return new DataProviderJdbc(true);
      default:
        log.error(Constants.PROVIDER_ERROR);
    }
    return null;
  }

  /**
   * Выполнить команду в соответсвии с запросом из командной строки
   *
   * @param dp     датапровайдер
   * @param cKey   ключ вызываемой функции
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String executeCommand(IDataProvider dp, String cKey, String params) {
    try {
      switch (CliKeys.valueOf(cKey.toUpperCase())) {
        case GET_USER:
          return dp.getUser(params).toString();
        case GET_USERS:
          return dp.getUsers().toString();
        case EDIT_USER:
          return userUpdate(dp, params);
        case CREATE_USER:
          return userCreate(dp, params);
        case DELETE_USER:
          return dp.deleteUser(params).toString();
        case CREATE_DEAL:
          return dealCreate(dp, params, false);
        case CREATE_PUBLIC_DEAL:
          return dealCreate(dp, params, true);
        case UPDATE_DEAL:
          return dealUpdate(dp, params);
        case MANAGE_DEAL:
          return dp.manageDeal(params).toString();
        case REMOVE_DEAL:
          return dp.removeDeal(params).toString();
        case SET_STATUS:
          return setStatus(dp, params);
        case ADD_ADDRESS:
          return addressCreate(dp, params);
        case GET_ADDRESS_BY_ID:
          return dp.getAddress(Long.parseLong(params)).toString();
        case GET_ADDRESS_BY_NAME:
          return dp.getAddress(params).toString();
        case GET_ADDRESSES:
          return dp.getAddresses().toString();
        case EDIT_ADDRESS:
          return addressUpdate(dp, params);
        case REMOVE_ADDRESS:
          return dp.removeAddress(Long.parseLong(params)).toString();
        case GET_MY_DEALS:
          return dp.getMyDeals(params).toString();
        case GET_GLOBAL_DEALS:
          return dp.getGlobalDeals(params).toString();
        case GET_MY_QUEUE:
          return dp.getMyQueue(params).toString();
        case ADD_DEAL_REQUEST:
          return dealRequest(dp, params, QueueEvent.ADD);
        case MANAGE_DEAL_REQUEST:
          return dealRequest(dp, params, QueueEvent.MANAGE);
        case ACCEPT_DEAL_REQUEST:
          return dealRequest(dp, params, QueueEvent.ACCEPT);
        case REFUSE_DEAL_REQUEST:
          return dealRequest(dp, params, QueueEvent.REFUSE);
        case GET_DEAL_QUEUE:
          return dp.getDealQueue(params).toString();
        case ADD_DEAL_PERFORMER:
          return dealPerform(dp, params, QueueEvent.ADD);
        case MANAGE_DEAL_PERFORM:
          return dealPerform(dp, params, QueueEvent.MANAGE);
        case ACCEPT_DEAL_PERFORM:
          return dealPerform(dp, params, QueueEvent.ACCEPT);
        case REFUSE_DEAL_PERFORM:
          return dealPerform(dp, params, QueueEvent.REFUSE);
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  private static String executeHBCommand(String cKey, String sMKey, String params) {
    try {
      switch (CliHBKeys.valueOf(cKey.toUpperCase())) {
        case HB_LAB1:
          return hb_Lab1(sMKey);
        case HB_LAB2:
          return hb_Lab2(sMKey, params);
        case HB_LAB3:
          return hb_Lab3(sMKey, params);
        case HB_LAB4:
          return hb_Lab4(sMKey, params);
        case HB_LAB5:
          return hb_Lab4(sMKey, params);
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  /**
   * Добавить новый адрес
   *
   * @param dp     датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String addressCreate(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 3);
    if (parsedParams == null) return "";
    return dp.addAddress(
            parsedParams[0],
            parsedParams[1],
            parsedParams[2]
    ).toString();
  }

  /**
   * Редактировать существующий адрес
   * @param dp датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String addressUpdate(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 4);
    if (parsedParams == null) return "";
    return dp.updateAddress(
            Long.parseLong(parsedParams[0]),
            parsedParams[1],
            parsedParams[2],
            parsedParams[3]
    ).toString();
  }

  /**
   * Создать пользователя
   *
   * @param dp     датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String userCreate(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 3);
    if (parsedParams == null) return "";
    return dp.createUser(
            parsedParams[0],
            parsedParams[1],
            Long.parseLong(parsedParams[2])
    ).toString();
  }

  /**
   * Редактировать данные пользователя
   *
   * @param dp     датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String userUpdate(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 4);
    if (parsedParams == null) return "";
    return dp.editUser(
            parsedParams[0],
            parsedParams[1],
            parsedParams[2],
            Long.parseLong(parsedParams[3])
    ).toString();
  }

  /**
   * Создать сделку
   *
   * @param dp       датапровайдер
   * @param params   строка с аргументами
   * @param isPublic флаг глобальной ли сделки. Если true, то будет создана глобальная сделка
   * @return строка ответа
   */
  private static String dealCreate(IDataProvider dp, String params, boolean isPublic) {
    String[] parsedParams = checkParamsCount(params, isPublic ? 8 : 7);
    if (parsedParams == null) return "";
    if (isPublic) {
      return dp.createPublicDeal(
              parsedParams[0],
              parsedParams[1],
              parsedParams[2],
              Long.parseLong(parsedParams[3]),
              DealStatus.valueOf(parsedParams[4]),
              DealTypes.valueOf(parsedParams[5]),
              ObjectTypes.valueOf(parsedParams[6]),
              parsedParams[6]
      ).toString();
    } else {
      return dp.createDeal(
              parsedParams[0],
              parsedParams[1],
              parsedParams[2],
              Long.parseLong(parsedParams[3]),
              DealTypes.valueOf(parsedParams[4]),
              ObjectTypes.valueOf(parsedParams[5]),
              parsedParams[6]
      ).toString();
    }
  }

  /**
   * Редактировать сделку
   *
   * @param dp     датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String dealUpdate(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 7);
    if (parsedParams == null) return "";
    return dp.updateDeal(
            parsedParams[0],
            parsedParams[1],
            Long.parseLong(parsedParams[2]),
            parsedParams[3],
            DealTypes.valueOf(parsedParams[4]),
            ObjectTypes.valueOf(parsedParams[5]),
            parsedParams[6]
    ).toString();
  }

  /**
   * Изменить статус глобальной сделки
   *
   * @param dp     датапровайдер
   * @param params строка с аргументами
   * @return строка ответа
   */
  private static String setStatus(IDataProvider dp, String params) {
    String[] parsedParams = checkParamsCount(params, 2);
    if (parsedParams == null) return "";
    return dp.setStatus(
            parsedParams[0],
            DealStatus.valueOf(parsedParams[4])
    ).toString();
  }

  /**
   * Произвести действие с запросом к сделке
   *
   * @param dp           датапровайдер
   * @param params       строка с аргументами
   * @param requestEvent тип запроса к очереди
   * @return строка ответа
   */
  private static String dealRequest(IDataProvider dp, String params, QueueEvent requestEvent) {
    switch (requestEvent) {
      case ADD: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.addDealRequest(parsedParams[0], parsedParams[1]).toString();
      }
      case MANAGE: {
        String[] parsedParams = checkParamsCount(params, 3);
        if (parsedParams == null) return "";
        return dp.manageDealRequest(parsedParams[0], parsedParams[1], Boolean.parseBoolean(parsedParams[2])).toString();
      }
      case ACCEPT: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.acceptDealRequest(parsedParams[0], parsedParams[1]).toString();
      }
      case REFUSE: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.refuseDealRequest(parsedParams[0], parsedParams[1]).toString();
      }
      default:
        return "";
    }
  }

  /**
   * Произвести действие с запросом сделки к пользователю
   *
   * @param dp           датапровайдер
   * @param params       строка с аргументами
   * @param requestEvent тип запроса к очереди
   * @return строка ответа
   */
  private static String dealPerform(IDataProvider dp, String params, QueueEvent requestEvent) {
    switch (requestEvent) {
      case ADD: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.addDealPerformer(parsedParams[0], parsedParams[1]).toString();
      }
      case MANAGE: {
        String[] parsedParams = checkParamsCount(params, 3);
        if (parsedParams == null) return "";
        return dp.manageDealPerform(parsedParams[0], parsedParams[1], Boolean.parseBoolean(parsedParams[2])).toString();
      }
      case ACCEPT: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.acceptDealPerform(parsedParams[0], parsedParams[1]).toString();
      }
      case REFUSE: {
        String[] parsedParams = checkParamsCount(params, 2);
        if (parsedParams == null) return "";
        return dp.refuseDealPerform(parsedParams[0], parsedParams[1]).toString();
      }
      default:
        return "";
    }
  }

  /**
   * Разделить переданные аргументы по разделителю и проверяить,
   * что их количество верное для вызова конкретной функции
   *
   * @param methodParams строка с аргументами
   * @param count        количество аргументов, которое необходимо
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String[] checkParamsCount(String methodParams, int count) {
    String[] result = methodParams.split(Constants.ARRAY_DELIMITER);
    if (result.length != count) {
      log.error(Constants.PARAMS_NUMBER_ERROR);
      return null;
    }
    return result;
  }

  /**
   * Блок задач первой л/р
   *
   * @param method название метода в блоке
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String hb_Lab1(String method) {
    final HibernateDataProvider provider = new HibernateDataProvider();
    try {
      switch (CliHBKeys.valueOf(method.toUpperCase())) {
        case GET_ALL_SCHEMAS:
          return provider.getAllSchemas().toString();
        case GET_SQL_HELP:
          return provider.getSqlHelp().toString();
        case GET_ALL_TABLES:
          return provider.getAllTables().toString();
        case GET_ALL_USERS:
          return provider.getAllUsers().toString();
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  /* ------------------------------------------------------- */

  /**
   * Блок задач второй л/р
   *
   * @param method название метода в блоке
   * @param params параметры для выполнения метода
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String hb_Lab2(String method, String params) {
    final HibernateDataProvider provider = new HibernateDataProvider();

    try {
      switch (CliHBKeys.valueOf(method.toUpperCase())) {
        case INSERT_TEST_ENTITY: {
          TestEntity entity = parseTestEntity(params, true);
          return provider.insertTestEntity(entity).toString();
        }
        case GET_ALL_TEST_ENTITY: {
          provider.insertTestEntity(getTemplateTestEntity());
          return provider.selectAllTestEntity().toString();
        }
        case DELETE_TEST_ENTITY: {
            String[] parsedParams = checkParamsCount(params, 1);
            if (parsedParams == null) return "";
            return provider.deleteTestEntity(Long.getLong(parsedParams[0])).toString();
          }
        case UPDATE_TEST_ENTITY: {
          TestEntity entity = parseTestEntity(params, false);
          return provider.updateTestEntity(entity).toString();
        }
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  private static TestEntity parseTestEntity(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 6 : 7);
    if (parsedParams == null) return null;
    TestEntity entity = new TestEntity();
    entity.setName(parsedParams[0]);
    entity.setDescription(parsedParams[1]);
    entity.setCheck(Boolean.getBoolean(parsedParams[2]));
    AddressT addressT = new AddressT();
    addressT.setCity(parsedParams[3]);
    addressT.setRegion(parsedParams[4]);
    addressT.setDistrict(parsedParams[5]);
    entity.setAddress(addressT);


    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[6]));
    } else {
      entity.setDateCreated(new Date());
    }
    log.debug("------------------------------------------------------------------------------------------------");
    log.debug(entity);
    return entity;
  }

  /* ------------------------------------------------------- */

  /**
   * Блок задач третьей л/р
   *
   * @param method название метода в блоке
   * @param params параметры для выполнения метода
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String hb_Lab3(String method, String params) {
    final ru.sfedu.deals.lab3.joinedTable.HibernateDataProvider providerJT
            = new ru.sfedu.deals.lab3.joinedTable.HibernateDataProvider();
    final ru.sfedu.deals.lab3.mapSupClass.HibernateDataProvider providerMSC
            = new ru.sfedu.deals.lab3.mapSupClass.HibernateDataProvider();
    final ru.sfedu.deals.lab3.singleTable.HibernateDataProvider providerST
            = new ru.sfedu.deals.lab3.singleTable.HibernateDataProvider();
    final ru.sfedu.deals.lab3.tablePerClass.HibernateDataProvider providerTPC
            = new ru.sfedu.deals.lab3.tablePerClass.HibernateDataProvider();

    try {
      switch (CliHBKeys.valueOf(method.toUpperCase())) {
        case JT_INSERT_CREDIT_ACCOUNT: {
          ru.sfedu.deals.lab3.joinedTable.CreditAccount account
                  = parseJTCreditAccount(params, true);
          assert account != null;
          return providerJT.insertAccount(account).toString();
        }
        case JT_SELECT_CREDIT_ACCOUNT: {
          providerJT.insertAccount(getJTCreditAccountTemplate());
          return providerJT.selectAllCreditAccount().toString();
        }
        case JT_DELETE_ACCOUNT: {
          providerJT.insertAccount(getJTCreditAccountTemplate());
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerJT.deleteAccount(Long.parseLong(parsedParams[0])).toString();
        }
        case JT_UPDATE_ACCOUNT: {
          ru.sfedu.deals.lab3.joinedTable.CreditAccount account
                  = parseJTCreditAccount(params, false);
          assert account != null;
          return providerJT.insertAccount(account).toString();
        }
        case MSC_INSERT_CREDIT_ACCOUNT: {
          ru.sfedu.deals.lab3.mapSupClass.CreditAccount account
                  = parseMSCCreditAccount(params, true);
          assert account != null;
          return providerMSC.insertAccount(account).toString();
        }
        case MSC_SELECT_CREDIT_ACCOUNT: {
          providerMSC.insertAccount(getMSCCreditAccountTemplate());
          return providerMSC.selectAllCreditAccount().toString();
        }
        case MSC_DELETE_ACCOUNT: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerMSC.deleteAccount(Long.getLong(parsedParams[0])).toString();
        }
        case MSC_UPDATE_ACCOUNT: {
          ru.sfedu.deals.lab3.mapSupClass.CreditAccount account
                  = parseMSCCreditAccount(params, false);
          assert account != null;
          return providerMSC.insertAccount(account).toString();
        }
        case ST_INSERT_CREDIT_ACCOUNT: {
          ru.sfedu.deals.lab3.singleTable.CreditAccount account
                  = parseSTCreditAccount(params, true);
          assert account != null;
          return providerST.insertAccount(account).toString();
        }
        case ST_SELECT_CREDIT_ACCOUNT: {
          providerST.insertAccount(getSTCreditAccountTemplate());
          return providerST.selectAllCreditAccount().toString();
        }
        case ST_DELETE_ACCOUNT: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerST.deleteAccount(Long.getLong(parsedParams[0])).toString();
        }
        case ST_UPDATE_ACCOUNT: {
          ru.sfedu.deals.lab3.singleTable.CreditAccount account
                  = parseSTCreditAccount(params, false);
          assert account != null;
          return providerST.insertAccount(account).toString();
        }
        case TPC_INSERT_CREDIT_ACCOUNT: {
          ru.sfedu.deals.lab3.tablePerClass.CreditAccount account
                  = parseTPCCreditAccount(params, true);
          assert account != null;
          return providerTPC.insertAccount(account).toString();
        }
        case TPC_SELECT_CREDIT_ACCOUNT: {
          providerTPC.insertAccount(getTPCCreditAccountTemplate());
          return providerTPC.selectAllCreditAccount().toString();
        }
        case TPC_DELETE_ACCOUNT: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerTPC.deleteAccount(Long.getLong(parsedParams[0])).toString();
        }
        case TPC_UPDATE_ACCOUNT: {
          ru.sfedu.deals.lab3.tablePerClass.CreditAccount account
                  = parseTPCCreditAccount(params, false);
          assert account != null;
          return providerTPC.insertAccount(account).toString();
        }
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  private static ru.sfedu.deals.lab3.joinedTable.CreditAccount parseJTCreditAccount(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 4 : 5);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab3.joinedTable.CreditAccount entity = new ru.sfedu.deals.lab3.joinedTable.CreditAccount();
    entity.setOwner(parsedParams[0]);
    entity.setBalance(BigDecimal.valueOf(Long.parseLong(parsedParams[1])));
    entity.setInterestRate(BigDecimal.valueOf(Long.parseLong(parsedParams[2])));
    entity.setCreditLimit(BigDecimal.valueOf(Long.parseLong(parsedParams[3])));

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[4]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab3.mapSupClass.CreditAccount parseMSCCreditAccount(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 4 : 5);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab3.mapSupClass.CreditAccount entity = new ru.sfedu.deals.lab3.mapSupClass.CreditAccount();
    entity.setOwner(parsedParams[0]);
    entity.setBalance(BigDecimal.valueOf(Long.parseLong(parsedParams[1])));
    entity.setInterestRate(BigDecimal.valueOf(Long.parseLong(parsedParams[2])));
    entity.setCreditLimit(BigDecimal.valueOf(Long.parseLong(parsedParams[3])));

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[4]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab3.singleTable.CreditAccount parseSTCreditAccount(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 4 : 5);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab3.singleTable.CreditAccount entity = new ru.sfedu.deals.lab3.singleTable.CreditAccount();
    entity.setOwner(parsedParams[0]);
    entity.setBalance(BigDecimal.valueOf(Long.parseLong(parsedParams[1])));
    entity.setInterestRate(BigDecimal.valueOf(Long.parseLong(parsedParams[2])));
    entity.setCreditLimit(BigDecimal.valueOf(Long.parseLong(parsedParams[3])));

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[4]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab3.tablePerClass.CreditAccount parseTPCCreditAccount(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 4 : 5);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab3.tablePerClass.CreditAccount entity = new ru.sfedu.deals.lab3.tablePerClass.CreditAccount();
    entity.setOwner(parsedParams[0]);
    entity.setBalance(BigDecimal.valueOf(Long.parseLong(parsedParams[1])));
    entity.setInterestRate(BigDecimal.valueOf(Long.parseLong(parsedParams[2])));
    entity.setCreditLimit(BigDecimal.valueOf(Long.parseLong(parsedParams[3])));

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[4]));
    }
    return entity;
  }

  /* ------------------------------------------------------- */

  /**
   * Блок задач четвертой л/р
   *
   * @param method название метода в блоке
   * @param params параметры для выполнения метода
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String hb_Lab4(String method, String params) {
    final ru.sfedu.deals.lab4.set.HibernateDataProvider providerSet
            = new ru.sfedu.deals.lab4.set.HibernateDataProvider();
    final ru.sfedu.deals.lab4.list.HibernateDataProvider providerList
            = new ru.sfedu.deals.lab4.list.HibernateDataProvider();
    final ru.sfedu.deals.lab4.map.HibernateDataProvider providerMap
            = new ru.sfedu.deals.lab4.map.HibernateDataProvider();
    final ru.sfedu.deals.lab4.entityMap.HibernateDataProvider providerEMap
            = new ru.sfedu.deals.lab4.entityMap.HibernateDataProvider();
    final ru.sfedu.deals.lab4.entitySet.HibernateDataProvider providerESet
            = new ru.sfedu.deals.lab4.entitySet.HibernateDataProvider();

    try {
      switch (CliHBKeys.valueOf(method.toUpperCase())) {
        case SET_INSERT_USER: {
          ru.sfedu.deals.lab4.set.User entity = parseSetUser(params, true);
          return providerSet.insertUser(entity).toString();
        }
        case SET_SELECT_USER: {
          providerSet.insertUser(getSetUserTemplate());
          return providerSet.selectAllUsers().toString();
        }
        case SET_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerSet.deleteUser(parsedParams[0]).toString();
        }
        case SET_UPDATE_USER: {
          ru.sfedu.deals.lab4.set.User entity = parseSetUser(params, false);
          return providerSet.updateUser(entity).toString();
        }
        case LIST_INSERT_USER: {
          ru.sfedu.deals.lab4.list.User entity = parseListUser(params, true);
          return providerList.insertUser(entity).toString();
        }
        case LIST_SELECT_USER: {
          providerList.insertUser(getListUserTemplate());
          return providerList.selectAllUsers().toString();
        }
        case LIST_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerList.deleteUser(parsedParams[0]).toString();
        }
        case LIST_UPDATE_USER: {
          providerList.insertUser(getListUserTemplate());
          ru.sfedu.deals.lab4.list.User entity = parseListUser(params, false);
          return providerList.updateUser(entity).toString();
        }
        case MAP_INSERT_USER: {
          ru.sfedu.deals.lab4.map.User entity = parseMapUser(params, true);
          return providerMap.insertUser(entity).toString();
        }
        case MAP_SELECT_USER: {
          providerMap.insertUser(getMapUserTemplate());
          return providerMap.selectAllUsers().toString();
        }
        case MAP_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerMap.deleteUser(parsedParams[0]).toString();
        }
        case MAP_UPDATE_USER: {
          ru.sfedu.deals.lab4.map.User entity = parseMapUser(params, false);
          return providerMap.updateUser(entity).toString();
        }
        case ESET_INSERT_USER: {
          ru.sfedu.deals.lab4.entityMap.User entity = parseEMapUser(params, true);
          return providerEMap.insertUser(entity).toString();
        }
        case ESET_SELECT_USER: {
          providerESet.insertUser(getESetUserTemplate());
          return providerEMap.selectAllUsers().toString();
        }
        case ESET_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerEMap.deleteUser(parsedParams[0]).toString();
        }
        case ESET_UPDATE_USER: {
          ru.sfedu.deals.lab4.entityMap.User entity = parseEMapUser(params, false);
          return providerEMap.updateUser(entity).toString();
        }
        case EMAP_INSERT_USER: {
          ru.sfedu.deals.lab4.entitySet.User entity = parseESetUser(params, true);
          return providerESet.insertUser(entity).toString();
        }
        case EMAP_SELECT_USER: {
          providerEMap.insertUser(getEMapUserTemplate());
          return providerESet.selectAllUsers().toString();
        }
        case EMAP_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return providerESet.deleteUser(parsedParams[0]).toString();
        }
        case EMAP_UPDATE_USER: {
          ru.sfedu.deals.lab4.entitySet.User entity = parseESetUser(params, false);
          return providerESet.updateUser(entity).toString();
        }
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  private static ru.sfedu.deals.lab4.set.User parseSetUser(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 2 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab4.set.User entity = new ru.sfedu.deals.lab4.set.User();
    entity.setName(parsedParams[0]);
    entity.setLast_name(parsedParams[1]);

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[2]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab4.list.User parseListUser(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 2 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab4.list.User entity = new ru.sfedu.deals.lab4.list.User();
    entity.setName(parsedParams[0]);
    entity.setLast_name(parsedParams[1]);
    entity.setPhones(new ArrayList<>());

    if (!isNew) {
      entity.setId(Long.parseLong(parsedParams[2]));
    }
    log.debug(entity);
    return entity;
  }

  private static ru.sfedu.deals.lab4.map.User parseMapUser(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 2 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab4.map.User entity = new ru.sfedu.deals.lab4.map.User();
    entity.setName(parsedParams[0]);
    entity.setLast_name(parsedParams[1]);
    entity.setPhones(new HashMap<>());

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[2]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab4.entityMap.User parseEMapUser(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 2 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab4.entityMap.User entity = new ru.sfedu.deals.lab4.entityMap.User();
    entity.setName(parsedParams[0]);
    entity.setLast_name(parsedParams[1]);
    entity.setAddresses(new HashMap<>());

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[2]));
    }
    return entity;
  }

  private static ru.sfedu.deals.lab4.entitySet.User parseESetUser(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 2 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab4.entitySet.User entity = new ru.sfedu.deals.lab4.entitySet.User();
    entity.setName(parsedParams[0]);
    entity.setLast_name(parsedParams[1]);
    entity.setAddresses(new HashSet<>());

    if (!isNew) {
      entity.setId(Long.getLong(parsedParams[2]));
    }
    return entity;
  }


  /* ------------------------------------------------------- */

  /**
   * Блок задач пятой л/р
   *
   * @param method название метода в блоке
   * @param params параметры для выполнения метода
   * @return массив с аргументами или null, в случае неверного количества
   */
  private static String hb_Lab5(String method, String params) {
    final ru.sfedu.deals.lab5.provider.HQLProvider provider
            = new ru.sfedu.deals.lab5.provider.HQLProvider();
    final ru.sfedu.deals.lab5.provider.NativeSQLProvider providerNQL
            = new ru.sfedu.deals.lab5.provider.NativeSQLProvider();
    final ru.sfedu.deals.lab5.provider.DataProvider simpleProvider
            = new ru.sfedu.deals.lab5.provider.DataProvider();

    try {
      switch (CliHBKeys.valueOf(method.toUpperCase())) {
        case HQL_INSERT_USER: {
          ru.sfedu.deals.lab5.User entity = parseLab5User(params, true);
          return simpleProvider.insertUser(entity).toString();
        }
        case HQL_SELECT_USER: {
          simpleProvider.insertUser(getLab5UserTemplate());
          return providerNQL.selectAllUsers().toString();
        }
        case HQL_DELETE_USER: {
          String[] parsedParams = checkParamsCount(params, 1);
          if (parsedParams == null) return "";
          return simpleProvider.deleteUser(parsedParams[0]).toString();
        }
        case HQL_UPDATE_USER: {
          simpleProvider.insertUser(getLab5UserTemplate());
          ru.sfedu.deals.lab5.User entity = parseLab5User(params, false);
          return simpleProvider.updateUser(entity).toString();
        }
      }
    } catch (IllegalArgumentException e) {
      log.error(Constants.PARAMS_ERROR);
      return Constants.PARAMS_ERROR;
    }
    return Constants.COMMAND_ERROR;
  }

  private static ru.sfedu.deals.lab5.User parseLab5User(String params, Boolean isNew) {
    String[] parsedParams = checkParamsCount(params, isNew ? 5 : 3);
    if (parsedParams == null) return null;
    ru.sfedu.deals.lab5.User entity = new ru.sfedu.deals.lab5.User();
    entity.setName(parsedParams[0]);
    entity.setPhone(parsedParams[1]);

    if (isNew) {
      Address address = new Address();
      address.setCity(parsedParams[2]);
      address.setRegion(parsedParams[3]);
      address.setDistrict(parsedParams[4]);
      entity.setAddress(address);
    } else {
      entity.setId(parsedParams[2]);
    }
    return entity;
  }

  private static TestEntity getTemplateTestEntity() {
    AddressT address = new AddressT();
    address.setCity("Москва");
    address.setDistrict("Московская");
    address.setRegion("Ммм");

    TestEntity testEntity = new TestEntity();
    testEntity.setName("Name");
    testEntity.setDescription("Description");
    testEntity.setDateCreated(new Date());
    testEntity.setCheck(false);
    testEntity.setAddress(address);
    return testEntity;
  }

  private static ru.sfedu.deals.lab3.joinedTable.CreditAccount getJTCreditAccountTemplate() {
    ru.sfedu.deals.lab3.joinedTable.CreditAccount account = new ru.sfedu.deals.lab3.joinedTable.CreditAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setCreditLimit(BigDecimal.valueOf(5600.0));
    return account;
  }
  private static ru.sfedu.deals.lab3.mapSupClass.CreditAccount getMSCCreditAccountTemplate() {
    ru.sfedu.deals.lab3.mapSupClass.CreditAccount account = new ru.sfedu.deals.lab3.mapSupClass.CreditAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setCreditLimit(BigDecimal.valueOf(5600.0));
    return account;
  }
  private static ru.sfedu.deals.lab3.tablePerClass.CreditAccount getTPCCreditAccountTemplate() {
    ru.sfedu.deals.lab3.tablePerClass.CreditAccount account = new ru.sfedu.deals.lab3.tablePerClass.CreditAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setCreditLimit(BigDecimal.valueOf(5600.0));
    return account;
  }
  private static ru.sfedu.deals.lab3.singleTable.CreditAccount getSTCreditAccountTemplate() {
    ru.sfedu.deals.lab3.singleTable.CreditAccount account = new ru.sfedu.deals.lab3.singleTable.CreditAccount();
    account.setBalance(BigDecimal.valueOf(123.0));
    account.setInterestRate(BigDecimal.valueOf(3.0));
    account.setOwner("Owner");
    account.setCreditLimit(BigDecimal.valueOf(5600.0));
    return account;
  }

  private static ru.sfedu.deals.lab4.set.User getSetUserTemplate() {
    ru.sfedu.deals.lab4.set.User user = new ru.sfedu.deals.lab4.set.User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    Set<String> set = new HashSet();
    set.add("14123123");
    user.setPhones(set);
    return user;
  }
  private static ru.sfedu.deals.lab4.list.User getListUserTemplate() {
    ru.sfedu.deals.lab4.list.User user = new ru.sfedu.deals.lab4.list.User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setPhones(new ArrayList<>());
    return user;
  }
  private static ru.sfedu.deals.lab4.map.User getMapUserTemplate() {
    ru.sfedu.deals.lab4.map.User user = new ru.sfedu.deals.lab4.map.User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setPhones(new HashMap<>());
    return user;
  }
  private static ru.sfedu.deals.lab4.entitySet.User getESetUserTemplate() {
    ru.sfedu.deals.lab4.entitySet.User user = new ru.sfedu.deals.lab4.entitySet.User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setAddresses(new HashSet<>());
    return user;
  }
  private static ru.sfedu.deals.lab4.entityMap.User getEMapUserTemplate() {
    ru.sfedu.deals.lab4.entityMap.User user = new ru.sfedu.deals.lab4.entityMap.User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setAddresses(new HashMap<>());
    return user;
  }
  private static ru.sfedu.deals.lab5.User getLab5UserTemplate() {
    ru.sfedu.deals.lab5.User user = new ru.sfedu.deals.lab5.User();
    Address address = new Address();
    address.setCity("Ростов-на-Дону");
    address.setDistrict("Ростовсая");
    address.setRegion("Южный");
    user.setAddress(address);
    user.setName("Иван");
    user.setPhone("895615156413");
    log.debug("---------------------------------------------------------");
    log.debug(user);
    return user;
  }
}

