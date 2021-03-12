package ru.sfedu.deals.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.Constants;
import ru.sfedu.deals.dataConvertors.ListConvertor;
import ru.sfedu.deals.enums.*;
import ru.sfedu.deals.model.*;
import ru.sfedu.deals.model.Queue;
import ru.sfedu.deals.utils.PropertyProvider;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DataProviderJdbc implements IDataProvider {
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(DataProviderJdbc.class);
  private static final ListConvertor listConvertor = new ListConvertor();

  public DataProviderJdbc() {
  }

  public DataProviderJdbc(boolean withInit) {
    initDB();
  }

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
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

  @Override
  public RequestStatuses createUser(
          @NotNull String name,
          @NotNull String phone,
          long addressId) {
    try {
      log.info(name);
      log.info(phone);
      log.info(addressId);
      String newId = UUID.randomUUID().toString();
      Queue queue = new Queue();
      queue.setId(newId);
      User user = new User();
      user.setId(newId);
      Optional<Address> addressOptional = getAddress(addressId);
      if (createQueue(queue).equals(RequestStatuses.SUCCESS)
              && createCompany(user).equals(RequestStatuses.SUCCESS)
              && addressOptional.isPresent()
      ) {
        return execute(
                String.format(Constants.INSERT_USER,
                        newId,
                        addressOptional.get().getId(),
                        queue.getId(),
                        name,
                        phone
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
  public Optional<List<User>> getUsers() {
    ResultSet set = this.getResultSet(Constants.SELECT_USERS);
    try {
      if (set != null && set.next()) {
        List<User> list = new ArrayList<>();
        do {
          Optional<User> userOptional = getUserFromResultSet(set);
          userOptional.ifPresent(list::add);
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
    try {
      ResultSet set = getResultSet(String.format(Constants.SELECT_USER, userId));
      if (set != null && set.next()) {
        return getUserFromResultSet(set);
      } else {
        log.error(Constants.UNDEFINED_USER);
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(Constants.UNDEFINED_USER);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses editUser(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String phone,
          long addressId) {
    try {
      Optional<User> user = getUser(userId);
      Optional<Address> address = getAddress(addressId);
      if (user.isPresent() && address.isPresent()) {

        return execute(
                String.format(Constants.UPDATE_USER,
                        addressId,
                        name,
                        phone,
                        userId
                )
        );
      } else {
        return RequestStatuses.NOT_FOUNDED;
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
      if (user.isPresent()
              && deleteQueue(userId).equals(RequestStatuses.SUCCESS)
              && deleteCompany(userId).equals(RequestStatuses.SUCCESS)
      ) {
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
          Optional<Address> address = getAddressFromResultSet(set);
          address.ifPresent(list::add);
        } while (set.next());
        return Optional.of(list);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(long id) {
    try {
      ResultSet set = this.getResultSet(String.format(Constants.SELECT_ADDRESS_BY_ID, id));
      if (set != null && set.next()) {
        return getAddressFromResultSet(set);
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
    try {
      ResultSet set = getResultSet(String.format(Constants.SELECT_ADDRESS_BY_NAME, city.toLowerCase()));
      if (set != null && set.next()) {
        return getAddressFromResultSet(set);
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
  public RequestStatuses addAddress(
          @NotNull String city,
          @NotNull String region,
          @NotNull String district
  ) {
    Optional<List<Address>> addresses = getAddresses();
    if (addresses.isPresent()) {
      return execute(String.format(Constants.INSERT_ADDRESS, addresses.get().size(), city, region, district));
    } else {
      return execute(String.format(Constants.INSERT_ADDRESS, Constants.INIT_ADDRESS_ID, city, region, district));
    }
  }

  @Override
  public RequestStatuses removeAddress(long id) {
      return getAddress(id).isPresent()
              ? execute(String.format(Constants.DELETE_ADDRESS, id))
              : RequestStatuses.FAILED;
  }

  @Override
  public RequestStatuses updateAddress(
          long id,
          @NotNull String city,
          @NotNull String region,
          @NotNull String district
  ) {
    return getAddress(id).isPresent()
            ? execute(String.format(Constants.UPDATE_ADDRESS, city, region, district, id))
            : RequestStatuses.FAILED;
  }

  @Override
  public RequestStatuses createDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          long addressId,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price
  ) {
    try {
      String newId = UUID.randomUUID().toString();
      Queue queue = new Queue();
      queue.setId(newId);
      Deal deal = new Deal();
      deal.setId(newId);
      deal.setOwner(userId);
      if (createQueue(queue).equals(RequestStatuses.SUCCESS)
              && getAddress(addressId).isPresent()
              && addDealToCompany(deal).equals(RequestStatuses.SUCCESS)
      ) {
        return execute(
                String.format(Constants.INSERT_DEAL,
                        newId,
                        name,
                        description,
                        addressId,
                        queue.getId(),
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
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses createPublicDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          long addressId,
          @NotNull DealStatus currentStatus,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price
  ) {
    try {
      String newId = UUID.randomUUID().toString();
      Queue requests = new Queue();
      requests.setId(newId);
      PublicDeal deal = new PublicDeal();
      Optional<Address> addressOptional = getAddress(addressId);
      deal.setId(newId);
      deal.setOwner(userId);
      if (createQueue(requests).equals(RequestStatuses.SUCCESS)
              && addressOptional.isPresent()
              && addDealHistory(newId, currentStatus).equals(RequestStatuses.SUCCESS)
              && addDealToCompany(deal).equals(RequestStatuses.SUCCESS)
      ) {
        return execute(
                String.format(Constants.INSERT_DEAL,
                        newId,
                        name,
                        description,
                        addressId,
                        requests.getId(),
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
      } else {
        return RequestStatuses.FAILED;
      }
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
          list.add(getDealFromResultSet(set));
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
          list.add((PublicDeal) getDealFromResultSet(set));
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
        return Optional.of(getDealFromResultSet(set));
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
      Optional<Deal> deal = manageDeal(id);
      if (deal.isPresent()
              && deleteDealFromCompany(deal.get()).equals(RequestStatuses.SUCCESS)
              && deleteQueue(deal.get().getId()).equals(RequestStatuses.SUCCESS)
              && removeDealHistoryByDeal(deal.get().getId()).equals(RequestStatuses.SUCCESS)
      ) {
        return execute(String.format(Constants.DELETE_DEAL, id));
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses updateDeal(
          @NotNull String id,
          @NotNull String name,
          long addressId,
          @NotNull String description,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price) {
    if (manageDeal(id).isPresent()
            && getAddress(addressId).isPresent()
    ) {
      return execute(String.format(
              Constants.UPDATE_DEAL,
              name,
              description,
              dealType,
              objectType,
              price,
              id)
      );
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses setStatus(@NotNull String id, @NotNull DealStatus newStatus) {
    Optional<Deal> deal = manageDeal(id);
    if (deal.isPresent()
            && deal.get().getDealModel().equals(DealModel.PUBLIC)
            && addDealHistory(id, newStatus).equals(RequestStatuses.SUCCESS)
    ) {
      return execute(String.format(Constants.UPDATE_DEAL_STATUS, newStatus, id));
    } else {
      log.error(Constants.DEAL_NOT_PUBLIC);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses addDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Deal> dealOptional = manageDeal(id);
    if (dealOptional.isPresent() && getUser(userId).isPresent()) {
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
    log.info(execute(Constants.DROP_USER_TABLE
            + Constants.DROP_ADDRESS_TABLE
            + Constants.DROP_QUEUE_TABLE
            + Constants.DROP_DEAL_TABLE
            + Constants.DROP_DEAL_HISTORY_TABLE
            + Constants.DROP_COMPANY_TABLE)
    );
  }

  @Override
  public void initDB() {
    log.info(execute(
            Constants.CREATE_USER_TABLE
                    + Constants.CREATE_ADDRESS_TABLE
                    + Constants.CREATE_QUEUE_TABLE
                    + Constants.CREATE_DEAL_TABLE
                    + Constants.CREATE_DEAL_HISTORY_TABLE
                    + Constants.CREATE_COMPANY_TABLE)
    );

  }

  public RequestStatuses createCompany(@NotNull User user) {
    Company company = new Company();
    List<String> employees = new ArrayList<>();
    employees.add(user.getId());
    String id = UUID.randomUUID().toString();
    if (execute(String.format(Constants.INSERT_COMPANY, id, employees, company.getDeals())).equals(RequestStatuses.SUCCESS)) {
      return RequestStatuses.SUCCESS;
    } else {
      return RequestStatuses.FAILED;
    }
  }

  public Optional<Company> getCompany(@NotNull String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_COMPANY_BY_ID, id));
    try {
      if (set != null && set.next()) {
        List<String> userIds = listConvertor.convert(set.getString(Constants.COLUMN_COMPANY_EMPLOYEES));
        List<String> dealsIds = listConvertor.convert(set.getString(Constants.COLUMN_COMPANY_DEALS));
        Company company = new Company();
        company.setId(set.getString(Constants.COLUMN_COMPANY_ID));
        company.setEmployees(companyEmployees(userIds));
        company.setDeals(companyDeals(dealsIds));
        return Optional.of(company);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  private RequestStatuses createCompany(String userId) {
    Company company = new Company();
    List<String> employees = new ArrayList<>();
    employees.add(userId);
    String id = UUID.randomUUID().toString();
    return execute(String.format(Constants.INSERT_COMPANY, id, employees, company.getDeals()));
  }

  public Optional<Company> getUserCompany(@NotNull String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_COMPANY, id));
    try {
      if (set != null && set.next()) {
        List<String> userIds = listConvertor.convert(set.getString(Constants.COLUMN_COMPANY_EMPLOYEES));
        List<String> dealsIds = listConvertor.convert(set.getString(Constants.COLUMN_COMPANY_DEALS));
        Company company = new Company();
        company.setId(set.getString(Constants.COLUMN_COMPANY_ID));
        company.setEmployees(companyEmployees(userIds));
        company.setDeals(companyDeals(dealsIds));
        return Optional.of(company);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  public RequestStatuses updateCompany(@NotNull Company updatedCompany) {
    List<String> userIds = new ArrayList<>();
    List<String> dealsIds = new ArrayList<>();
    updatedCompany.getEmployees().forEach(item -> userIds.add(item.getId()));
    updatedCompany.getDeals().forEach(item -> dealsIds.add(item.getId()));
    return execute(String.format(Constants.UPDATE_COMPANY, userIds, dealsIds, updatedCompany.getId()));
  }

  public RequestStatuses deleteCompany(@NotNull String id) {
    return execute(String.format(Constants.DELETE_COMPANY, id));
  }

  private List<User> companyEmployees(List<String> userIds) {
    List<User> userList = new ArrayList<>();
    userIds.forEach(item -> {
      if (!item.equals(Constants.EMPTY_STRING)) {
        Optional<User> user = getUser(item);
        user.ifPresent(userList::add);
      }
    });
    return userList;
  }

  private List<Deal> companyDeals(List<String> dealIds) {
    List<Deal> dealsList = new ArrayList<>();
    dealIds.forEach(item -> {
      if (!item.equals(Constants.EMPTY_STRING)) {
        Optional<Deal> deal = manageDeal(item);
        deal.ifPresent(dealsList::add);
      }
    });
    return dealsList;
  }

  private RequestStatuses addDealToCompany(Deal deal) {
    Optional<Company> companyOptional = getUserCompany(deal.getOwner());
    if (companyOptional.isPresent()) {
      Company company = companyOptional.get();
      List<Deal> deals = company.getDeals();
      deals.add(deal);
      company.setDeals(deals);
      return updateCompany(company);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses deleteDealFromCompany(Deal deal) {
    Optional<Company> optionalCompany = getUserCompany(deal.getOwner());
    if (optionalCompany.isPresent()) {
      Company company = optionalCompany.get();
      company.setDeals(company.getDeals().stream().filter(item -> !item.getId()
              .equals(deal.getId()))
              .collect(Collectors.toList())
      );
      return updateCompany(company);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses createQueue(@NotNull Queue queue) {
    try {
      return this.execute(
              String.format(Constants.INSERT_QUEUE,
                      queue.getId(),
                      queue.getItems().toString()
              )
      );
    } catch (Exception e) {
      return null;
    }
  }

  public Optional<Queue> getQueue(@NotNull String id) {
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

  public RequestStatuses updateQueue(@NotNull Queue queue) {
    try {
      if (getQueue(queue.getId()).isPresent()) {
        return execute(String.format(Constants.UPDATE_QUEUE, queue.getItems(), queue.getId()));
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses deleteQueue(@NotNull String id) {
    return execute(String.format(Constants.DELETE_QUEUE, id));
  }

  private RequestStatuses addDealHistory(String id, DealStatus status) {
    DealHistory dealHistory = new DealHistory();
    dealHistory.setId(id);
    dealHistory.setStatus(status);
    dealHistory.setText(status.getMessage());
    dealHistory.setCreated_at(new Date());
    return addDealHistory(dealHistory);
  }

  @Override
  public RequestStatuses addDealHistory(@NotNull DealHistory dealHistoryItem) {
    try {
      return execute(String.format(Constants.INSERT_DEAL_HISTORY,
              dealHistoryItem.getId(),
              dealHistoryItem.getText(),
              dealHistoryItem.getStatus(),
              new Date().getTime())
      );
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses removeDealHistoryByDeal(@NotNull String id) {
    return execute(String.format(Constants.DELETE_DEAL_HISTORY, id));
  }

  public Optional<List<DealHistory>> getDealHistoryList(@NotNull String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_DEAL_HISTORY, id));
    try {
      if (set != null && set.next()) {
        List<DealHistory> list = new ArrayList<>();
        do {
          Optional<DealHistory> dealHistoryOptional = getDealHistoryFromResultSet(set);
          dealHistoryOptional.ifPresent(list::add);
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

  public Optional<DealHistory> getDealHistory(@NotNull String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_DEAL_HISTORY, id));
    try {
      if (set != null && set.next()) {
        return getDealHistoryFromResultSet(set);
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      return Optional.empty();
    }
  }

  public RequestStatuses updateDealHistoryItem(@NotNull DealHistory updatedDH) {
    if (getDealHistory(updatedDH.getId()).isPresent()) {
      return execute(String.format(Constants.UPDATE_DEAL_HISTORY, updatedDH.getStatus(), updatedDH.getText(), updatedDH.getId()));
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private Optional<DealHistory> getDealHistoryFromResultSet(ResultSet set) {
    try {
      DealHistory dealHistory = new DealHistory();
      dealHistory.setId(set.getString(Constants.COLUMN_HISTORY_ID));
      dealHistory.setStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_HISTORY_STATUS)));
      dealHistory.setText(set.getString(Constants.COLUMN_HISTORY_TEXT));
      dealHistory.setCreated_at(parseDate(set.getLong(Constants.COLUMN_HISTORY_CREATED_AT)));
      return Optional.of(dealHistory);
    } catch (SQLException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  private Deal getDealFromResultSet(ResultSet set) throws SQLException {
    Deal deal = new Deal();
    Optional<Address> addressOptional = getAddress(set.getLong(Constants.COLUMN_DEAL_ADDRESS));
    Optional<Queue> queueOptional = getQueue(set.getString(Constants.COLUMN_DEAL_REQUESTS));
    if (addressOptional.isPresent() && queueOptional.isPresent()) {
      deal.setId(set.getString(Constants.COLUMN_DEAL_ID));
      deal.setDealModel(DealModel.valueOf(set.getString(Constants.COLUMN_DEAL_MODEL)));
      deal.setName(set.getString(Constants.COLUMN_DEAL_NAME));
      deal.setDescription(set.getString(Constants.COLUMN_DEAL_DESCRIPTION));
      deal.setAddress(addressOptional.get());
      deal.setRequests(queueOptional.get());
      deal.setOwner(set.getString(Constants.COLUMN_DEAL_OWNER));
      deal.setPerformer(set.getString(Constants.COLUMN_DEAL_PERFORMER));
      deal.setDealType(DealTypes.valueOf(set.getString(Constants.COLUMN_DEAL_TYPE)));
      deal.setObject(ObjectTypes.valueOf(set.getString(Constants.COLUMN_DEAL_OBJECT)));
      deal.setCreated_at(parseDate(set.getLong(Constants.COLUMN_DEAL_CREATED_AT)));
      deal.setPrice(set.getString(Constants.COLUMN_DEAL_PRICE));
    } else {
      log.error(Constants.SMT_WRONG);
      throw new SQLException();
    }

    if (deal.getDealModel() == DealModel.PUBLIC) {
      return getPublicDealFieldsFromResultSet(deal, set);
    }
    return deal;
  }

  private PublicDeal getPublicDealFieldsFromResultSet(Deal deal, ResultSet set) throws SQLException {
    PublicDeal publicDeal = new PublicDeal();
    publicDeal.setId(deal.getId());
    publicDeal.setDealModel(deal.getDealModel());
    publicDeal.setName(deal.getName());
    publicDeal.setDescription(deal.getDescription());
    publicDeal.setAddress(deal.getAddress());
    publicDeal.setRequests(deal.getRequests());
    publicDeal.setOwner(deal.getOwner());
    publicDeal.setPerformer(deal.getPerformer());
    publicDeal.setDealType(deal.getDealType());
    publicDeal.setObject(deal.getObject());
    publicDeal.setCreated_at(deal.getCreated_at());
    publicDeal.setPrice(deal.getPrice());
    publicDeal.setCurrentStatus(DealStatus.valueOf(set.getString(Constants.COLUMN_DEAL_STATUS)));
    Optional<List<DealHistory>> dealHistory = getDealHistoryList(publicDeal.getId());
    publicDeal.setHistory(dealHistory.orElseGet(ArrayList::new));
    return publicDeal;
  }

  private Optional<Address> getAddressFromResultSet(ResultSet set) throws SQLException {
    Address address = new Address();
    address.setId(set.getLong(Constants.COLUMN_ADDRESS_ID));
    address.setCity(set.getString(Constants.COLUMN_ADDRESS_CITY));
    address.setDistrict(set.getString(Constants.COLUMN_ADDRESS_DISTRICT));
    address.setRegion(set.getString(Constants.COLUMN_ADDRESS_REGION));
    return Optional.of(address);
  }

  private boolean checkIdInQueue(Queue queue, String id) {
    return queue.getItems().contains(id);
  }

  private Optional<User> getUserFromResultSet(ResultSet set) throws SQLException {
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
    } else {
      log.error(Constants.UNDEFINED_USER_DATA);
      return Optional.empty();
    }
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
