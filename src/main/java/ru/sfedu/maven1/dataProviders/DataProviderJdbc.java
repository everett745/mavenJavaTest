package ru.sfedu.maven1.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.dataConvertors.ListConvertor;
import ru.sfedu.maven1.enums.*;
import ru.sfedu.maven1.model.*;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.utils.PropertyProvider;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DataProviderJdbc implements DataProvider {
  private static DataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(DataProviderJdbc.class);
  private static final ListConvertor listConvertor = new ListConvertor();

  public static DataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  @Override
  public RequestStatuses createUser(@NotNull String name, @NotNull String phone, @NotNull Address address) {
    try {
      String newId = UUID.randomUUID().toString();
      String queueId = createQueue(newId);
      if (queueId == null) {
        return RequestStatuses.FAILED;
      }
      return execute(
              String.format(Constants.INSERT_USER,
                      newId,
                      address.getId(),
                      queueId,
                      name,
                      phone
              )
      );
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<User>> getUsers() {
    ResultSet set = this.getResultSet(Constants.SELECT_USERS);
    try {
      if (set != null && set.next()) {
        List<User> list = new ArrayList<>();
        do {
          Optional<Queue> queue = getQueue(set.getString(Constants.COLUMN_USER_QUEUE));
          Optional<Address> address = getAddress(set.getLong(Constants.COLUMN_USER_ADDRESS));
          if (queue.isPresent() && address.isPresent()) {
            User user = new User();
            user.setId(set.getString(Constants.COLUMN_USER_ID));
            user.setAddress(address.get());
            user.setQueue(queue.get());
            user.setName(set.getString(Constants.COLUMN_USER_NAME));
            user.setPhone(set.getString(Constants.COLUMN_USER_PHONE));
            list.add(user);
          } else {
            log.error(Constants.UNDEFINED_USER_DATA);
          }
        } while (set.next());
        return Optional.of(list);
      } else {
        log.error(Constants.UNDEFINED_USERS_LIST);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> getUser(@NotNull String userId) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_USER, userId));
    try {
      if (set != null && set.next()) {
        Optional<Queue> queue = getQueue(set.getString(Constants.COLUMN_USER_QUEUE));
        Optional<Address> address = getAddress(set.getLong(Constants.COLUMN_USER_ADDRESS));

        if (queue.isPresent() && address.isPresent()) {
          User user = new User();
          user.setId(set.getString(Constants.COLUMN_USER_ID));
          user.setAddress(address.get());
          user.setQueue(queue.get());
          user.setName(set.getString(Constants.COLUMN_USER_NAME));
          user.setPhone(set.getString(Constants.COLUMN_USER_PHONE));
          return Optional.of(user);
        }
        log.error(Constants.UNDEFINED_USER_DATA);
        return Optional.empty();
      } else {
        log.error(Constants.UNDEFINED_USER);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses editUser(@NotNull User user) {
    try {
      if (getUser(user.getId()).isPresent()) {
        return execute(
                String.format(Constants.UPDATE_USER,
                        user.getAddress().getId(),
                        user.getName(),
                        user.getPhone(),
                        user.getId()
                )
        );
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses deleteUser(@NotNull String userId) {
    try {
      Optional<User> user = getUser(userId);
      if (user.isPresent()) {
        return execute(String.format(Constants.DELETE_USER, userId, userId));
      }
      return RequestStatuses.FAILED;
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<Address>> getAddresses() {
    ResultSet set = this.getResultSet(Constants.SELECT_ADDRESSES);
    try {
      if (set != null && set.next()) {
        List<Address> list = new ArrayList<>();
        do {
          Address address = new Address();
          address.setId(set.getLong(Constants.COLUMN_ADDRESS_ID));
          address.setCity(set.getString(Constants.COLUMN_ADDRESS_CITY));
          address.setRegion(set.getString(Constants.COLUMN_ADDRESS_REGION));
          address.setDistrict(set.getString(Constants.COLUMN_ADDRESS_DISTRICT));
          list.add(address);
        } while (set.next());
        return Optional.of(list);
      } else {
        log.error(Constants.UNDEFINED_ADDRESSES);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(long id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_ADDRESS_BY_ID, id));
    try {
      if (set != null && set.next()) {
        Address address = new Address();
        address.setId(set.getLong(Constants.COLUMN_ADDRESS_ID));
        address.setCity(set.getString(Constants.COLUMN_ADDRESS_CITY));
        address.setDistrict(set.getString(Constants.COLUMN_ADDRESS_DISTRICT));
        address.setRegion(set.getString(Constants.COLUMN_ADDRESS_REGION));
        return Optional.of(address);
      } else {
        log.error(Constants.UNDEFINED_ADDRESS);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(@NotNull String city) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_ADDRESS_BY_NAME, city.toLowerCase()));
    try {
      if (set != null && set.next()) {
        Address address = new Address();
        address.setId(set.getLong(Constants.COLUMN_ADDRESS_ID));
        address.setCity(set.getString(Constants.COLUMN_ADDRESS_CITY));
        address.setDistrict(set.getString(Constants.COLUMN_ADDRESS_DISTRICT));
        address.setRegion(set.getString(Constants.COLUMN_ADDRESS_REGION));
        return Optional.of(address);
      } else {
        log.error(Constants.UNDEFINED_ADDRESS);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses createDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          @NotNull Address address,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price
  ) {
    try {
      String newId = UUID.randomUUID().toString();
      String requestsId = createQueue(newId);
      if (requestsId == null) {
        return RequestStatuses.FAILED;
      }
      return execute(
              String.format(Constants.INSERT_DEAL,
                      newId,
                      name,
                      description,
                      address.getId(),
                      requestsId,
                      userId,
                      Constants.EMPTY_STRING,
                      dealType,
                      objectType,
                      DealModel.PRIVATE,
                      new Date().getTime(),
                      price,
                      Constants.EMPTY_STRING
              )
      );
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses createDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          @NotNull Address address,
          @NotNull DealStatus currentStatus,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price
  ) {
    try {
      String newId = UUID.randomUUID().toString();
      String requestsId = createQueue(newId);
      if (requestsId == null) {
        return RequestStatuses.FAILED;
      }
      this.execute(
              String.format(Constants.INSERT_DEAL,
                      newId,
                      name,
                      description,
                      address.getId(),
                      requestsId,
                      userId,
                      Constants.EMPTY_STRING,
                      dealType,
                      objectType,
                      DealModel.PUBLIC,
                      new Date().getTime(),
                      price,
                      currentStatus
              )
      );
      return addDealHistory(newId, currentStatus);
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<Deal>> getMyDeals(@NotNull String userId) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_MY_DEALS, userId));
    try {
      if (set != null && set.next()) {
        List<Deal> list = new ArrayList<>();
        do {
          Deal deal = new Deal();
          deal.setId(set.getString(Constants.COLUMN_DEAL_ID));
          deal.setDealModel(DealModel.valueOf(set.getString(Constants.COLUMN_DEAL_MODEL)));
          deal.setName(set.getString(Constants.COLUMN_DEAL_NAME));
          deal.setDescription(set.getString(Constants.COLUMN_DEAL_DESCRIPTION));
          deal.setAddress(getAddress(set.getLong(Constants.COLUMN_DEAL_ADDRESS)).get());
          deal.setRequests(getQueue(set.getString(Constants.COLUMN_DEAL_REQUESTS)).get());
          deal.setOwner(set.getString(Constants.COLUMN_DEAL_OWNER));
          deal.setPerformer(set.getString(Constants.COLUMN_DEAL_PERFORMER));
          deal.setDealType(DealTypes.valueOf(set.getString(Constants.COLUMN_DEAL_TYPE)));
          deal.setObject(ObjectTypes.valueOf(set.getString(Constants.COLUMN_DEAL_OBJECT)));
          deal.setCreated_at(parseDate(set.getLong(Constants.COLUMN_DEAL_CREATED_AT)));
          deal.setPrice(set.getString(Constants.COLUMN_DEAL_PRICE));

          switch (deal.getDealModel()) {
            case PRIVATE: {
              list.add(deal);
            } break;
            case PUBLIC: {
              Optional<List<DealHistory>> dealHistory = getDealHistory(deal.getId());
              PublicDeal publicDeal = new PublicDeal();
              publicDeal.setId(deal.getId());
              publicDeal.setName(deal.getName());
              publicDeal.setDescription(deal.getDescription());
              publicDeal.setAddress(deal.getAddress());
              publicDeal.setRequests(deal.getRequests());
              publicDeal.setOwner(deal.getOwner());
              publicDeal.setPerformer(deal.getPerformer());
              publicDeal.setDealType(deal.getDealType());
              publicDeal.setObject(deal.getObject());
              publicDeal.setDealModel(deal.getDealModel());
              publicDeal.setCreated_at(deal.getCreated_at());
              publicDeal.setPrice(deal.getPrice());
              publicDeal.setCurrentStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_DEAL_STATUS)));
              publicDeal.setHistory(dealHistory.get());
              list.add(publicDeal);
            }
          }

        } while (set.next());
        return Optional.of(list);
      } else {
        log.info(Constants.UNDEFINED_DEAL);
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<PublicDeal>> getGlobalDeals(@NotNull String userId) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_GLOBAL_DEALS, userId));
    try {
      if (set != null && set.next()) {
        List<PublicDeal> list = new ArrayList<>();
        do {
          PublicDeal deal = new PublicDeal();
          deal.setId(set.getString(Constants.COLUMN_DEAL_ID));
          deal.setDealModel(DealModel.valueOf(set.getString(Constants.COLUMN_DEAL_MODEL)));
          deal.setName(set.getString(Constants.COLUMN_DEAL_NAME));
          deal.setDescription(set.getString(Constants.COLUMN_DEAL_DESCRIPTION));
          deal.setAddress(getAddress(set.getLong(Constants.COLUMN_DEAL_ADDRESS)).get());
          deal.setRequests(getQueue(set.getString(Constants.COLUMN_DEAL_REQUESTS)).get());
          deal.setOwner(set.getString(Constants.COLUMN_DEAL_OWNER));
          deal.setPerformer(set.getString(Constants.COLUMN_DEAL_PERFORMER));
          deal.setDealType(DealTypes.valueOf(set.getString(Constants.COLUMN_DEAL_TYPE)));
          deal.setObject(ObjectTypes.valueOf(set.getString(Constants.COLUMN_DEAL_OBJECT)));
          deal.setCreated_at(parseDate(set.getLong(Constants.COLUMN_DEAL_CREATED_AT)));
          deal.setPrice(set.getString(Constants.COLUMN_DEAL_PRICE));
          deal.setCurrentStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_DEAL_STATUS)));
          deal.setHistory(getDealHistory(deal.getId()).get());
          list.add(deal);
        } while (set.next());

        return Optional.of(list);
      } else {
        log.info(Constants.UNDEFINED_DEAL);
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Deal> manageDeal(@NotNull String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_DEAL, id));
    try {
      if (set != null && set.next()) {
        Deal deal = new Deal();
        deal.setId(set.getString(Constants.COLUMN_DEAL_ID));
        deal.setDealModel(DealModel.valueOf(set.getString(Constants.COLUMN_DEAL_MODEL)));
        deal.setName(set.getString(Constants.COLUMN_DEAL_NAME));
        deal.setDescription(set.getString(Constants.COLUMN_DEAL_DESCRIPTION));
        deal.setAddress(getAddress(set.getLong(Constants.COLUMN_DEAL_ADDRESS)).get());
        deal.setRequests(getQueue(set.getString(Constants.COLUMN_DEAL_REQUESTS)).get());
        deal.setOwner(set.getString(Constants.COLUMN_DEAL_OWNER));
        deal.setPerformer(set.getString(Constants.COLUMN_DEAL_PERFORMER));
        deal.setDealType(DealTypes.valueOf(set.getString(Constants.COLUMN_DEAL_TYPE)));
        deal.setObject(ObjectTypes.valueOf(set.getString(Constants.COLUMN_DEAL_OBJECT)));
        deal.setCreated_at(parseDate(set.getLong(Constants.COLUMN_DEAL_CREATED_AT)));
        deal.setPrice(set.getString(Constants.COLUMN_DEAL_PRICE));

        if (deal.getDealModel() == DealModel.PRIVATE) {
          return Optional.of(deal);
        } else {
          Optional<List<DealHistory>> dealHistory = getDealHistory(deal.getId());
          PublicDeal publicDeal = new PublicDeal();
          publicDeal.setId(deal.getId());
          publicDeal.setName(deal.getName());
          publicDeal.setDescription(deal.getDescription());
          publicDeal.setAddress(deal.getAddress());
          publicDeal.setRequests(deal.getRequests());
          publicDeal.setOwner(deal.getOwner());
          publicDeal.setPerformer(deal.getPerformer());
          publicDeal.setDealType(deal.getDealType());
          publicDeal.setObject(deal.getObject());
          publicDeal.setDealModel(deal.getDealModel());
          publicDeal.setCreated_at(deal.getCreated_at());
          publicDeal.setPrice(deal.getPrice());
          publicDeal.setCurrentStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_DEAL_STATUS)));
          publicDeal.setHistory(dealHistory.get());
          return Optional.of(publicDeal);
        }
      } else {
        log.info(Constants.UNDEFINED_DEAL);
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses removeDeal(@NotNull String id) {
    try {
      if (manageDeal(id).isPresent()) {
        return execute(String.format(Constants.DELETE_DEAL, id, id, id));
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses updateDeal(@NotNull Deal deal) {
    if (manageDeal(deal.getId()).isPresent()) {
      return execute(String.format(
              Constants.UPDATE_DEAL,
              deal.getName(),
              deal.getDescription(),
              deal.getDealType(),
              deal.getObject(),
              deal.getPrice(),
              deal.getId())
      );
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses setStatus(@NotNull String id, @NotNull DealStatus newStatus) {
    Optional<Deal> deal = manageDeal(id);
    if (deal.isPresent() && (deal.get().getDealModel() == DealModel.PUBLIC)) {
      addDealHistory(id, newStatus);
      return execute(String.format(Constants.UPDATE_DEAL_STATUS, newStatus, id));
    } else {
      log.error(Constants.DEAL_NOT_PUBLIC);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses addDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Deal> dealOptional = manageDeal(id);
    Optional<User> userOptional = getUser(userId);
    if (dealOptional.isPresent() && userOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue queue = deal.getRequests();
      List<String> items = new ArrayList<>(queue.getItems());
      if (!deal.getPerformer().equals(Constants.EMPTY_STRING)) {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      } else if (items.contains(userId)) {
        log.error(Constants.ALREADY_IN_QUEUE + userId);
        return RequestStatuses.FAILED;
      }
      items.add(userId);
      queue.setItems(items);
      return updateQueue(queue);
    } else {
      log.error(Constants.UNDEFINED_USER_OR_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getDealQueue(@NotNull String id) {
    return getQueue(id);
  }

  @Override
  public RequestStatuses manageDealRequest(@NotNull String userId, @NotNull String id, boolean accept) {
    Optional<Deal> dealOptional = manageDeal(id);
    if (dealOptional.isPresent()) {
      if (accept) {
        return acceptDealRequest(userId, id);
      } else {
        return refuseDealRequest(userId, id);
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses acceptDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Queue> queueOptional = getQueue(id);
    if (queueOptional.isPresent()) {
      Queue requests = queueOptional.get();
      if (checkIdInQueue(requests, userId)) {
        requests.setItems(new ArrayList<>());
        updateQueue(requests);
        return execute(String.format(Constants.UPDATE_DEAL_PERFORMER, userId, id));
      } else {
        log.error(Constants.UNDEFINED_USER_IN_DEAL_REQUESTS);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses refuseDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Queue> queueOptional = getQueue(id);
    if (queueOptional.isPresent()) {
      Queue requests = queueOptional.get();
      if (checkIdInQueue(requests, userId)) {
        List<String> items = requests.getItems();
        requests.setItems(items.stream()
                .filter(item -> !item.equals(userId))
                .collect(Collectors.toList())
        );
        return updateQueue(requests);
      } else {
        log.error(Constants.UNDEFINED_USER_IN_DEAL_REQUESTS);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses addDealPerformer(@NotNull String userId, @NotNull String id) {
    Optional<Deal> dealOptional = manageDeal(id);
    Optional<User> userOptional = getUser(userId);
    if (dealOptional.isPresent() && userOptional.isPresent()) {
      User user = userOptional.get();
      Queue queue = user.getQueue();
      List<String> items = new ArrayList<>(queue.getItems());
      if (!dealOptional.get().getPerformer().equals(Constants.EMPTY_STRING)) {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      } else if (items.contains(id)) {
        log.error(Constants.ALREADY_IN_QUEUE + userId);
        return RequestStatuses.FAILED;
      }
      items.add(id);
      queue.setItems(items);
      return updateQueue(queue);
    } else {
      log.error(Constants.UNDEFINED_USER_OR_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getMyQueue(@NotNull String userId) {
    return getQueue(userId);
  }

  @Override
  public RequestStatuses manageDealPerform(@NotNull String userId, @NotNull String id, boolean accept) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      if (userOptional.get().getQueue().getItems().contains(id)) {
        if (accept) {
          return acceptDealPerform(userId, id);
        } else {
          return refuseDealPerform(userId, id);
        }
      } else {
        log.error(Constants.UNDEFINED_PERFORM);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses acceptDealPerform(@NotNull String userId, @NotNull String id) {
    Optional<Queue> queueOptional = getQueue(userId);
    if (queueOptional.isPresent()) {
      Queue requests = queueOptional.get();
      if (checkIdInQueue(requests, id)) {
        List<String> items = requests.getItems();
        requests.setItems(items.stream()
                .filter(item -> !item.equals(id))
                .collect(Collectors.toList())
        );
        updateQueue(requests);
        return execute(String.format(Constants.UPDATE_DEAL_PERFORMER, userId, id));
      } else {
        log.error(Constants.UNDEFINED_PERFORM);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses refuseDealPerform(@NotNull String userId, @NotNull String id) {
    Optional<Queue> queueOptional = getQueue(userId);
    if (queueOptional.isPresent()) {
      Queue requests = queueOptional.get();
      if (checkIdInQueue(requests, id)) {
        List<String> items = requests.getItems();
        requests.setItems(items.stream()
                .filter(item -> !item.equals(id))
                .collect(Collectors.toList())
        );
        return updateQueue(requests);
      } else {
        log.error(Constants.UNDEFINED_PERFORM);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public void clearDB() {
    execute(Constants.CLEAR_BD);
  }

  @Override
  public void initDB() {
    try {
      String s;
      FileReader fr = new FileReader(new File(PropertyProvider.getProperty(Constants.DB_INIT_PATH)));
      BufferedReader br = new BufferedReader(fr);

      while((s = br.readLine()) != null){
        execute(s);
      }
      br.close();
    } catch (Exception e) {
      log.error(e);
    }
  }

  private Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
    Class.forName(PropertyProvider.getProperty(Constants.DB_DRIVER));
    return DriverManager.getConnection(
            PropertyProvider.getProperty(Constants.DB_URL),
            PropertyProvider.getProperty(Constants.DB_USER),
            PropertyProvider.getProperty(Constants.DB_PASSWORD));
  }

  private RequestStatuses execute(String sql) {
    log.info(sql);
    try {
      PreparedStatement statement = getConnection().prepareStatement(sql);
      statement.executeUpdate();
      statement.close();
      return RequestStatuses.SUCCESS;
    } catch (SQLException | IOException | ClassNotFoundException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private ResultSet getResultSet(String sql) {
    log.info(sql);
    try {
      PreparedStatement statement = getConnection().prepareStatement(sql);
      return statement.executeQuery();
    } catch (SQLException | ClassNotFoundException | IOException e) {
      log.error(e);
      return null;
    }
  }

  private String createQueue(String id) {
    try {
      Queue queue = new Queue();
      queue.setId(id);
      this.execute(
              String.format(Constants.INSERT_QUEUE,
                      queue.getId(),
                      queue.getItems().toString()
              )
      );
      return queue.getId();
    } catch (Exception e) {
      return null;
    }
  }

  private Optional<Queue> getQueue(String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_QUEUE, id));
    try {
      if (set != null && set.next()) {
        Queue queue = new Queue();
        queue.setId(set.getString(Constants.COLUMN_QUEUE_ID));
        String str = set.getString(Constants.COLUMN_QUEUE_ITEMS);
        queue.setItems(listConvertor.convert(str)
                .stream().filter(item -> !item.equals(Constants.EMPTY_STRING))
                .collect(Collectors.toList())
        );
        return Optional.of(queue);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  private RequestStatuses updateQueue(Queue queue) {
    try {
      return execute(String.format(Constants.UPDATE_QUEUE, queue.getItems(), queue.getId()));
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses addDealHistory(String id, DealStatus status) {
    try {
      return execute(String.format(Constants.INSERT_DEAL_HISTORY, id, status.getMessage(), status, new Date().getTime()));
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<DealHistory>> getDealHistory(String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_DEAL_HISTORY, id));
    try {
      if (set != null && set.next()) {
        List<DealHistory> list = new ArrayList<>();
        do {
          DealHistory dealHistory = new DealHistory();
          dealHistory.setId(set.getString(Constants.COLUMN_HISTORY_ID));
          dealHistory.setStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_HISTORY_STATUS)));
          dealHistory.setText(set.getString(Constants.COLUMN_HISTORY_TEXT));
          dealHistory.setCreated_at(parseDate(set.getLong(Constants.COLUMN_HISTORY_CREATED_AT)));
          list.add(dealHistory);
        } while (set.next());
        return Optional.of(list);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }

  private boolean checkIdInQueue(Queue queue, String id) {
    return queue.getItems().contains(id);
  }

  private Date parseDate(long s) {
    try {
      return new Date(s);
    } catch (Exception e) {
      log.error(e);
      return new Date();
    }

  }
}
