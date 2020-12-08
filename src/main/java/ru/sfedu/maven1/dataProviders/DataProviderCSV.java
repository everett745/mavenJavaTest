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
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;
import ru.sfedu.maven1.utils.PropertyProvider;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
      List<User> userList = getFromCsv(User.class);
      if (userList.stream().anyMatch(listUser -> listUser.getPhone().equals(phone))) {
        return RequestStatuses.FAILED;
      }
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
  public Optional<List<User>> getUserList() {
    try {
      return Optional.of(getFromCsv(User.class));
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> getUser(UUID userId) {
    Optional<List<User>> optionalUsers = getUserList();
    if (optionalUsers.isPresent()) {
      List<User> users = optionalUsers.get();
      Optional<User> optionalUser = users
                    .stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst();
      if (optionalUser.isPresent()) {
        return optionalUser;
      } else {
        log.error("User not found");
        return Optional.empty();
      }
    } else {
      log.error("Users list is empty");
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
  public RequestStatuses deleteUser(UUID userId) {
    Optional<List<User>> optionalUsers = getUserList();
    if (optionalUsers.isPresent()) {
      List<User> users = optionalUsers.get();
      users = users.stream()
              .filter(user -> !user.getId()
              .equals(userId))
              .collect(Collectors.toList());
      try {
        insertIntoCsv(User.class, users, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error("Users list is empty");
      return RequestStatuses.FAILED;
    }
  }


  @Override
  public RequestStatuses addAddress(
          @NotNull final String city,
          @NotNull final String country,
          @NotNull final String region
  ) {
    try {
      Address address = new Address();
      address.setId(999);
      address.setCity(city);
      address.setRegion(country);
      address.setDistrict(region);
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
      Optional<Address> resAddress = addresses .stream()
              .filter(address -> address.getId() == id)
              .findFirst();
      return resAddress;
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(String city) {
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
  public RequestStatuses addQueue(
          @NotNull final Queue queue
  ) {
    try {
      insertIntoCsv(queue);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<Queue> getQueueById(UUID id) {
    try {
      List<Queue> queues = getFromCsv(Queue.class);
      return queues
              .stream()
              .filter(queue -> queue.getId().equals(id))
              .findFirst();
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Queue> getQueue(UUID id) {
    Optional<Queue> optionalQueue = getQueueById(id);
    if (optionalQueue.isPresent()) {
      Queue queue = optionalQueue.get();
      return Optional.of(queue);
    } else {
      return Optional.empty();
    }
  }


}

