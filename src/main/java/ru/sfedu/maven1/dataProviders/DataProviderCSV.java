package ru.sfedu.maven1.dataProviders;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.Main;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Deal;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;
import ru.sfedu.maven1.utils.PropertyProvider;


import java.io.*;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.stream.Collectors;

public class DataProviderCSV implements DataProvider {

  private static DataProvider INSTANCE = null;

  private static Logger log = LogManager.getLogger(Main.class);

  public static DataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderCSV();
    }
    return INSTANCE;
  }

  private <T> void insertIntoCsv(T object) throws IOException {
    insertIntoCsv(object.getClass(), Collections.singletonList(object), false);
  }

  private <T> void insertIntoCsv(Class<?> tClass,
                                 List<T> objectList,
                                 boolean overwrite) throws IOException {
    List<T> tList;
    if (!overwrite) {
      tList = (List<T>) getFromCsv(tClass);
      tList.addAll(objectList);
    } else {
      tList = objectList;
    }
    if (tList.isEmpty()) {
      deleteFile(tClass);
    }
    CSVWriter csvWriter = getCsvWriter(tClass);
    StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(csvWriter)
            .withApplyQuotesToAll(false)
            .build();
    try {
      beanToCsv.write(tList);
    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
      log.error(e);
    }
    csvWriter.close();
  }

  private <T> CSVWriter getCsvWriter(Class<T> tClass) throws IOException {
    FileWriter writer;
    File path = new File(PropertyProvider.getProperty(Constants.CSV_PATH));
    File file = new File(getFilePath(tClass));
    if (!file.exists()) {
      if (path.mkdirs()) {
        if (!file.createNewFile()) {
          throw new IOException(
                  String.format(Constants.EXCEPTION_CANNOT_CREATE_FILE,
                          file.getName()));
        }
      }
    }
    writer = new FileWriter(file);
    return new CSVWriter(writer);
  }


  private <T> CSVReader getCsvReader(Class<T> tClass) throws IOException {
    File file = new File(getFilePath(tClass));

    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new IOException(
                String.format(
                        Constants.EXCEPTION_CANNOT_CREATE_FILE,
                        file.getName()));
      }
    }

    FileReader fileReader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    return new CSVReader(bufferedReader);
  }


  private <T> List<T> getFromCsv(Class<T> tClass) throws IOException {
    List<T> tList;
    try {
      CSVReader csvReader = getCsvReader(tClass);
      CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
              .withType(tClass)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      tList = csvToBean.parse();
      csvReader.close();
    } catch (IOException e) {
      log.error(e);
      throw e;
    }
    return tList;
  }

  private <T> void deleteFile(Class<T> tClass) {
    try {
      log.debug(new File(getFilePath(tClass)).delete());
    } catch (IOException e) {
      log.error(e);
    }
  }

  private <T> String getFilePath(Class<T> tClass) throws IOException{
    return PropertyProvider.getProperty(Constants.CSV_PATH)
            + tClass.getSimpleName().toString().toLowerCase()
            + PropertyProvider.getProperty(Constants.CSV_EXTENSION);
  }

  @Override
  public RequestStatuses createUser(
          @NotNull String name,
          @NotNull String phone,
          @NotNull Address address) {
    try {
      UUID uuid = UUID.randomUUID();
      User user = new User();
      user.setId(uuid);
      user.setName(name);
      user.setPhone(phone);
      user.setAddress(address);
      user.setQueue(new Queue());
      insertIntoCsv(user);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<User>> getUsers() {
    try {
      List<User> userList = getFromCsv(User.class);
      if (userList.toArray().length != 0) {
        return Optional.of(userList);
      } else {
        log.error(Constants.UNDEFINED_USERS_LIST);
        return Optional.empty();
      }
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> getUser(UUID userId) {
    Optional<List<User>> optionalUsers = getUsers();
    if (optionalUsers.isPresent()) {
      List<User> users = optionalUsers.get();
      Optional<User> optionalUser = users
                    .stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst();
      if (optionalUser.isPresent()) {
        return optionalUser;
      } else {
        log.error(Constants.UNDEFINED_USERS_LIST);
        return Optional.empty();
      }
    } else {
      log.error(Constants.UNDEFINED_USERS_LIST);
      return Optional.empty();
    }
  }

  private boolean isEditValid(User user, User editedUser) {
    return user.getAddress().equals(editedUser.getAddress())
            && user.getPhone().equals(editedUser.getPhone())
            && user.getName().equals(editedUser.getName())
            && user.getQueue().equals(editedUser.getQueue());
  }

  @Override
  public RequestStatuses editUser(@NotNull final User editedUser) {
    try {
      var userList = getFromCsv(User.class);

      Optional<User> optionalUser = userList.stream()
              .filter(user -> user.getId() == editedUser.getId())
              .findFirst();

      if (optionalUser.isEmpty()) {
        return RequestStatuses.NOT_FOUNDED;
      }

      if (!isEditValid(editedUser, optionalUser.get())) {
        return RequestStatuses.FAILED;
      }
      userList.remove(optionalUser.get());
      userList.add(editedUser);
      insertIntoCsv(User.class, userList, true);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses deleteUser(@NotNull UUID userId) {
    Optional<List<User>> optionalUsers = getUsers();
    Optional<User> optionalUser = getUser(userId);
    if (optionalUsers.isPresent() && optionalUser.isPresent()) {
      List<User> users = optionalUsers.get();
      users = users.stream()
              .filter(user -> !user.getId()
              .equals(userId))
              .collect(Collectors.toList());
      deleteQueue(optionalUser.get().getQueue().getId());
      try {
        insertIntoCsv(User.class, users, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_USERS_LIST);
      return RequestStatuses.FAILED;
    }
  }


  @Override
  public RequestStatuses addAddress(
          @NotNull final String city,
          @NotNull final String region,
          @NotNull final String district
  ) {
    try {
      Address address = new Address();
      address.setId(new Random().nextLong());
      address.setCity(city);
      address.setRegion(region);
      address.setDistrict(district);
      insertIntoCsv(address);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<Address>> getAddresses() {
    try {
      return Optional.of(getFromCsv(Address.class));
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(long id) {
    Optional<List<Address>> optionalAddresses = getAddresses();
    if (optionalAddresses.isPresent()) {
      List<Address> addresses = optionalAddresses.get();
      return addresses .stream()
              .filter(address -> address.getId() == id)
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(@NotNull String city) {
    Optional<List<Address>> optionalAddresses = getAddresses();
    if (optionalAddresses.isPresent()) {
      List<Address> addresses = optionalAddresses.get();
      return addresses
              .stream()
              .filter(address -> address.getCity().toLowerCase().contains(city.toLowerCase()))
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses addQueue(@NotNull final Queue queue) {
    try {
      insertIntoCsv(queue);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Queue>> getQueuesList() {
    try {
      List<Queue> queues = getFromCsv(Queue.class);
      return Optional.of(queues);
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_QUEUES_LIST);
      return Optional.empty();
    }
  }

  private Optional<Queue> getQueueById(UUID id) {
    Optional<List<Queue>> queuesOptional = getQueuesList();
    if (queuesOptional.isPresent()) {
      List<Queue> queues = queuesOptional.get();
      return queues
              .stream()
              .filter(queue -> queue.getId().equals(id))
              .findFirst();
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
      return Optional.empty();
    }
  }

  private RequestStatuses updateQueue(@NotNull final Queue queue) {
    Optional<List<Queue>> optionalQueues = getQueuesList();
    Optional<Queue> optionalQueue = getQueueById(queue.getId());
    if (optionalQueues.isPresent()) {
      List<Queue> queues = optionalQueues.get();
      if (optionalQueue.isPresent()) {
        queues = queues.stream()
                .filter(queueL -> !queueL.getId().equals(queue.getId()))
                .collect(Collectors.toList());
        queues.add(queue);
        try {
          insertIntoCsv(Queue.class, queues, true);
        } catch (IOException e) {
          log.error(e);
          return RequestStatuses.FAILED;
        }
        return RequestStatuses.SUCCESS;
      } else {
        log.error(Constants.UNDEFINED_QUEUE);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_QUEUES_LIST);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getQueue(@NotNull UUID id) {
    return getQueueById(id);
  }

  private RequestStatuses deleteQueue(@NotNull UUID id) {
    Optional<List<Queue>> optionalQueues = getQueuesList();
    if (optionalQueues.isPresent()) {
      List<Queue> queues = optionalQueues.get();
      queues = queues.stream()
              .filter(user -> !user.getId()
              .equals(id))
              .collect(Collectors.toList());
      try {
        insertIntoCsv(Queue.class, queues, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_QUEUE_FOR_DELETE);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Deal>> getDealsList() {
    try {
      return Optional.of(getFromCsv(Deal.class));
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  private Optional<Deal> getDealById(UUID id) {
    Optional<List<Deal>> deals = getDealsList();
    if (deals.isPresent()) {
      List<Deal> dealList = deals.get();
      return dealList.stream()
              .filter(queue -> queue.getId().equals(id))
              .findFirst();
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses createDeal(
          @NotNull final UUID userId,
          @NotNull String name,
          @NotNull String description,
          @NotNull Address address,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price) {
    try {
      Deal deal = new Deal();
      UUID uuid = UUID.randomUUID();
      deal.setId(uuid);
      deal.setName(name);
      deal.setDescription(description);
      deal.setAddress(address);
      deal.setRequests(new Queue());
      deal.setOwner(userId);
      deal.setDealType(dealType);
      deal.setObject(objectType);
      deal.setCreated_at(new Date());
      deal.setPrice(price);
      insertIntoCsv(deal);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }


  @Override
  public Optional<Deal> manageDeal(@NotNull UUID id) {
    Optional<Deal> deal = getDealById(id);
    if (deal.isPresent()) {
      return deal;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses removeDeal(@NotNull UUID id) {
    Optional<List<Deal>> optionalDeals = getDealsList();
    Optional<Deal> optionalDeal = getDealById(id);
    if (optionalDeals.isPresent() && optionalDeal.isPresent()) {
      List<Deal> deals = optionalDeals.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId()
              .equals(id))
              .collect(Collectors.toList());
      deleteQueue(optionalDeal.get().getRequests().getId());
      try {
        insertIntoCsv(Deal.class, deals, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses updateDeal(@NotNull Deal deal) {
    Optional<List<Deal>> optionalDeals = getDealsList();
    Optional<Deal> optionalDeal = getDealById(deal.getId());
    if (optionalDeals.isPresent()) {
      List<Deal> deals = optionalDeals.get();
      if (optionalDeal.isPresent()) {
        deals = deals.stream()
                .filter(dealL -> !dealL.getId().equals(deal.getId()))
                .collect(Collectors.toList());
        deleteQueue(deal.getRequests().getId());
        deals.add(deal);
        try {
          insertIntoCsv(Deal.class, deals, true);
        } catch (IOException e) {
          log.error(e);
          return RequestStatuses.FAILED;
        }
        return RequestStatuses.SUCCESS;
      } else {
        log.error(Constants.UNDEFINED_DEAL);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<Deal>> getGlobalDeals(@NotNull UUID userId) {
    Optional<List<Deal>> optionalDeals = getDealsList();
    if (optionalDeals.isPresent()) {
      List<Deal> deals = optionalDeals.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId().equals(userId))
              .collect(Collectors.toList());
      return Optional.of(deals);
    } else {
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Deal>> getMyDeals(@NotNull UUID userId) {
    Optional<List<Deal>> optionalDeals = getDealsList();
    if (optionalDeals.isPresent()) {
      List<Deal> deals = optionalDeals.get();
      deals = deals.stream()
              .filter(deal -> deal.getOwner().equals(userId))
              .collect(Collectors.toList());
      return Optional.of(deals);
    } else {
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses setStatus(@NotNull UUID id, @NotNull DealStatus newStatus) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      deal.setDealStatus(newStatus);
      updateDeal(deal);
      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  private boolean userIsPerformer(@NotNull UUID userId, @NotNull Deal deal) {
    return deal.getPerformer().equals(userId);
  }

  @Override
  public RequestStatuses addDealRequest(@NotNull UUID id, @NotNull UUID userId) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();
      List<UUID> requestsList = requests.getItems();

      if (requestsList.contains(userId)) {
        log.info(Constants.ALREADY_IN_QUEUE + userId);
      } else if (userIsPerformer(userId, deal)) {
        log.info(Constants.ALREADY_PERFORMER);
      } else {
        requestsList.add(userId);
        requests.setItems(requestsList);
        updateQueue(requests);
      }

      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getDealQueue(@NotNull UUID id) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      return Optional.of(dealOptional.get().getRequests());
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses manageDealRequest(@NotNull UUID userId, @NotNull UUID id, boolean accept) {
    Optional<Deal> dealOptional = getDealById(id);
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
  public RequestStatuses acceptDealRequest(@NotNull UUID userId, @NotNull UUID id) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();

      requests.setItems(new ArrayList<UUID>());
      updateQueue(requests);

      deal.setPerformer(userId);
      updateDeal(deal);

      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses refuseDealRequest(@NotNull UUID userId, @NotNull UUID id) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();

      List<UUID> requestsList = requests.getItems()
              .stream().filter(requestId -> !requestId.equals(userId))
              .collect(Collectors.toList());

      requests.setItems(requestsList);
      updateQueue(requests);
      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses addDealPerformer(@NotNull UUID id, @NotNull UUID userId) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Queue queue = user.getQueue();

      List<UUID> requestsList = queue.getItems();

      if (requestsList.contains(id)) {
        log.info(Constants.ALREADY_IN_QUEUE + id);
      } else {
        requestsList.add(id);
        queue.setItems(requestsList);
        updateQueue(queue);
      }

      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getMyQueue(@NotNull UUID userId) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      return Optional.of(userOptional.get().getQueue());
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses manageDealPerform(@NotNull UUID userId, @NotNull UUID id, boolean accept) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      if (accept) {
        return acceptDealPerform(userId, id);
      } else {
        return refuseDealPerform(userId, id);
      }
    } else {
      log.error(Constants.UNDEFINED_USER);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses acceptDealPerform(@NotNull UUID userId, @NotNull UUID id) {
    Optional<User> userOptional = getUser(userId);
    Optional<Deal> dealOptional = getDealById(id);
    if (userOptional.isPresent() && dealOptional.isPresent()) {
      User user = userOptional.get();
      Deal deal = dealOptional.get();
      Queue requests = user.getQueue();

      if (deal.getPerformer() == null) {
        if (requests.getItems().contains(id)) {
          List<UUID> requestsList = requests.getItems()
                  .stream().filter(requestId -> !requestId.equals(id))
                  .collect(Collectors.toList());

          deal.setPerformer(userId);
          updateDeal(deal);

          requests.setItems(requestsList);
          updateQueue(requests);

          return RequestStatuses.SUCCESS;
        } else {
          log.error(Constants.UNDEFINED_PERFORM);
          return RequestStatuses.FAILED;
        }
      } else {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses refuseDealPerform(@NotNull UUID userId, @NotNull UUID id) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Queue requests = user.getQueue();

      List<UUID> requestsList = requests.getItems()
              .stream().filter(requestId -> !requestId.equals(id))
              .collect(Collectors.toList());

      requests.setItems(requestsList);
      updateQueue(requests);
      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }
}

