package ru.sfedu.deals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import ru.sfedu.deals.dataProviders.DataProviderCSV;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.DataProviderXML;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.enums.CliKeys;
import ru.sfedu.deals.enums.DealStatus;
import ru.sfedu.deals.enums.DealTypes;
import ru.sfedu.deals.enums.ObjectTypes;
import ru.sfedu.deals.enums.QueueEvent;

import java.util.Arrays;
import java.util.List;

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
    String methodParams = "";
    try {
      providerType = command.get(0);
      methodKey = command.get(1);
      if (command.size() == 3) {
        methodParams = command.get(2);
      }

      log.info("RESULT: " + executeCommand(getDataSource(providerType), methodKey, methodParams));
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
}

