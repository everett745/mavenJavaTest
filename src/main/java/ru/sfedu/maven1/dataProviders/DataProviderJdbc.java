package ru.sfedu.maven1.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.dataConvertors.ListConvertor;
import ru.sfedu.maven1.dataConvertors.UUIDListConvertor;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.*;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.utils.PropertyProvider;

import java.io.*;
import java.sql.*;
import java.util.*;

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
      String queueId = createQueue();
      if (queueId == null) {
        return RequestStatuses.FAILED;
      }
      System.out.println(name);
      this.execute(
              String.format(Constants.INSERT_USER,
                      UUID.randomUUID().toString(),
                      address.getId(),
                      queueId,
                      name,
                      phone
              )
      );
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<User> getUser(@NotNull String userId) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_USER, userId));
    try {
      if (set != null && set.next()) {
        User user = new User();
        user.setId(set.getString(Constants.USER_ID));
        user.setAddress(getAddressDB(set.getLong(Constants.USER_ADDRESS)));
        user.setQueue(getQueue(set.getString(Constants.USER_QUEUE)));
        user.setName(set.getString(Constants.USER_NAME));
        user.setPhone(set.getString(Constants.USER_PHONE));
        return Optional.of(user);
      } else {
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
      this.execute(
              String.format(Constants.UPDATE_USER,
                      user.getAddress().getId(),
                      user.getQueue().getId(),
                      user.getName(),
                      user.getPhone(),
                      user.getId()
              )
      );
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses deleteUser(@NotNull String userId) {
    try {
      this.execute(String.format(Constants.DELETE_USER, userId));
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      return RequestStatuses.FAILED;
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

  @Override
  public Optional<List<User>> getUsers() {
    return Optional.empty();
  }

  @Override
  public Optional<List<Address>> getAddresses() {
    return Optional.empty();
  }

  @Override
  public Optional<Address> getAddress(long id) {
    return Optional.empty();
  }

  @Override
  public Optional<Address> getAddress(@NotNull String city) {
    return Optional.empty();
  }

  @Override
  public RequestStatuses createDeal(@NotNull String userId, @NotNull String name, @NotNull String description, @NotNull Address address, @NotNull DealTypes dealType, @NotNull ObjectTypes objectType, @NotNull String price) {
    return null;
  }

  @Override
  public RequestStatuses createDeal(@NotNull String userId, @NotNull String name, @NotNull String description, @NotNull Address address, @NotNull DealStatus currentStatus, @NotNull DealTypes dealType, @NotNull ObjectTypes objectType, @NotNull String price) {
    return null;
  }

  @Override
  public Optional<List<PublicDeal>> getGlobalDeals(@NotNull String userId) {
    return Optional.empty();
  }

  @Override
  public Optional<List<Deal>> getMyDeals(@NotNull String userId) {
    return Optional.empty();
  }

  @Override
  public Optional<Deal> manageDeal(@NotNull String id) {
    return Optional.empty();
  }

  @Override
  public RequestStatuses removeDeal(@NotNull String id) {
    return null;
  }

  @Override
  public RequestStatuses updateDeal(@NotNull Deal deal) {
    return null;
  }

  @Override
  public RequestStatuses setStatus(@NotNull String id, @NotNull DealStatus newStatus) {
    return null;
  }

  @Override
  public RequestStatuses addDealRequest(@NotNull String userId, @NotNull String id) {
    return null;
  }

  @Override
  public Optional<Queue> getDealQueue(@NotNull String id) {
    return Optional.empty();
  }

  @Override
  public RequestStatuses manageDealRequest(@NotNull String userId, @NotNull String id, boolean accept) {
    return null;
  }

  @Override
  public RequestStatuses acceptDealRequest(@NotNull String userId, @NotNull String id) {
    return null;
  }

  @Override
  public RequestStatuses refuseDealRequest(@NotNull String userId, @NotNull String id) {
    return null;
  }

  @Override
  public RequestStatuses addDealPerformer(@NotNull String userId, @NotNull String id) {
    return null;
  }

  @Override
  public Optional<Queue> getMyQueue(@NotNull String userId) {
    return Optional.empty();
  }

  @Override
  public RequestStatuses manageDealPerform(@NotNull String userId, @NotNull String id, boolean accept) {
    return null;
  }

  @Override
  public RequestStatuses acceptDealPerform(@NotNull String userId, @NotNull String id) {
    return null;
  }

  @Override
  public RequestStatuses refuseDealPerform(@NotNull String userId, @NotNull String id) {
    return null;
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

  private String createQueue() {
    try {
      Queue queue = new Queue();
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

  private Queue getQueue(String id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_QUEUE, id));
    try {
      if (set != null && set.next()) {
        Queue queue = new Queue();
        queue.setId(set.getString(Constants.QUEUE_ID));
        String str = set.getString(Constants.QUEUE_ITEMS);
        queue.setItems(listConvertor.convert(str));
        return queue;
      } else {
        return new Queue();
      }
    } catch (SQLException e) {
      log.error(e);
      return new Queue();
    }
  }

  private Address getAddressDB(long id) {
    ResultSet set = this.getResultSet(String.format(Constants.SELECT_ADDRESS, id));
    try {
      if (set != null && set.next()) {
        Address address = new Address();
        address.setId(set.getLong(Constants.ADDRESS_ID));
        address.setCity(set.getString(Constants.ADDRESS_CITY));
        address.setDistrict(set.getString(Constants.ADDRESS_DISTRICT));
        address.setRegion(set.getString(Constants.ADDRESS_REGION));
        return address;
      } else {
        return new Address();
      }
    } catch (SQLException e) {
      log.error(e);
      return new Address();
    }
  }
}
