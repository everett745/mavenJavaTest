package ru.sfedu.deals.dataProviders;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.Constants;
import ru.sfedu.deals.enums.*;
import ru.sfedu.deals.model.*;
import ru.sfedu.deals.model.Queue;
import ru.sfedu.deals.utils.PropertyProvider;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataProviderCSV implements IDataProvider {
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(DataProviderCSV.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderCSV();
    }
    return INSTANCE;
  }

  private <T> void insertIntoCSV(T object) throws IOException {
    insertIntoCSV(object.getClass(), Collections.singletonList(object), false);
  }

  private <T> void insertIntoCSV(Class<?> tClass,
                                 List<T> objectList,
                                 boolean overwrite) throws IOException {
    List<T> tList;
    if (!overwrite) {
      tList = (List<T>) getFromCSV(tClass);
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
    File file = getFile(tClass);
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
    File file = getFile(tClass);

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

  private <T> List<T> getFromCSV(Class<T> tClass) throws IOException {
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
      log.info(Constants.DELETE_FILE + getFile(tClass));
      log.info(getFile(tClass).delete());
    } catch (IOException e) {
      log.error(e);
    }
  }

  private <T> File getFile(Class<T> tClass) throws IOException {
    return new File(PropertyProvider.getProperty(Constants.CSV_PATH)
            + tClass.getSimpleName().toLowerCase()
            + PropertyProvider.getProperty(Constants.CSV_EXTENSION));
  }

  @Override
  public RequestStatuses createUser(
          @NotNull String name,
          @NotNull String phone,
          long addressId) {
    Optional<Address> address = getAddress(addressId);
    if (address.isPresent()) {
      return createUserOptional(name, phone, address.get());
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<User>> getUsers() {
    return getUsersOptional();
  }

  @Override
  public Optional<User> getUser(@NotNull String userId) {
    Optional<User> optionalUser = getUserOptional(userId);
    if (optionalUser.isPresent()) {
      return optionalUser;
    } else {
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
    Optional<User> userOptional = getUser(userId);
    Optional<Address> addressOptional = getAddress(addressId);
    if (userOptional.isPresent() && addressOptional.isPresent()) {
      User user = userOptional.get();
      user.setName(name);
      user.setPhone(phone);
      user.setAddress(addressOptional.get());
      return updateUser(user);
    } else {
      return RequestStatuses.NOT_FOUNDED;
    }
  }

  @Override
  public RequestStatuses deleteUser(@NotNull String userId) {
    Optional<List<User>> userListOptional = getUsers();
    Optional<User> optionalUser = getUser(userId);
    if (userListOptional.isPresent() && optionalUser.isPresent()) {
      List<User> users = userListOptional.get();

      users = users.stream()
              .filter(user -> !user.getId()
                      .equals(userId))
              .collect(Collectors.toList());

      if (deleteUserFromCompany(optionalUser.get().getId()).equals(RequestStatuses.SUCCESS)) {
        deleteQueue(optionalUser.get().getQueue().getId());
        return updateUsersList(users);
      } else {
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<Address>> getAddresses() {
    try {
      List<Address> addressList = getFromCSV(Address.class);
      if (addressList.isEmpty()) {
        return Optional.empty();
      } else {
        return Optional.of(addressList);
      }
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<Address> getAddress(long id) {
    Optional<List<Address>> optionalAddresses = getAddresses();
    if (optionalAddresses.isPresent()) {
      Optional<Address> address = optionalAddresses.get().stream()
              .filter(item -> item.getId() == id)
              .findFirst();
      if (address.isPresent()) {
        return address;
      } else {
        log.error(Constants.UNDEFINED_ADDRESS);
        return Optional.empty();
      }
    } else {
      log.error(Constants.UNDEFINED_ADDRESS);
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
              .filter(address ->
                      address.getCity().toLowerCase()
                              .contains(city.toLowerCase()))
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses addAddress(
          @NotNull String city,
          @NotNull String region,
          @NotNull String district) {
    Optional<List<Address>> addresses = getAddresses();
    try {
      Address address = new Address();
      address.setCity(city);
      address.setRegion(region);
      address.setDistrict(district);
      address.setId(addresses.isPresent() ? addresses.get().size() : Constants.INIT_ADDRESS_ID);
      insertIntoCSV(address);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.SUCCESS;
    }
  }

  @Override
  public RequestStatuses removeAddress(long id) {
    try {
      Optional<List<Address>> addresses = getAddresses();
      if (addresses.isPresent() && getAddress(id).isPresent()) {
        List<Address> addressList = addresses.get();
        addressList = addressList.stream().filter(item -> item.getId() != id)
                .collect(Collectors.toList()
                );
        insertIntoCSV(Address.class, addressList, true);
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses updateAddress(
          long id,
          @NotNull String city,
          @NotNull String region,
          @NotNull String district
  ) {
    try {
      Optional<List<Address>> addresses = getAddresses();
      Optional<Address> addressOptional = getAddress(id);
      if (addressOptional.isPresent() && addresses.isPresent()) {
        List<Address> addressList = addresses.get();
        Address updatedAddress = addressOptional.get();
        addressList = addressList.stream().filter(item -> item.getId() != id).collect(Collectors.toList());
        updatedAddress.setCity(city);
        updatedAddress.setRegion(region);
        updatedAddress.setDistrict(district);
        addressList.add(updatedAddress);
        insertIntoCSV(Address.class, addressList, true);
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
      return RequestStatuses.FAILED;
    }
  }

  public Optional<Queue> getQueue(@NotNull String id) {
    return getQueueById(id);
  }

  @Override
  public RequestStatuses createDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          long addressId,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price) {
    try {
      Deal deal = new Deal();
      String uuid = UUID.randomUUID().toString();
      Queue queue = new Queue();
      queue.setId(uuid);
      Optional<Address> addressOptional = getAddress(addressId);

      if (createQueue(queue).equals(RequestStatuses.SUCCESS) && addressOptional.isPresent()) {
        deal.setId(uuid);
        deal.setName(name);
        deal.setDescription(description);
        deal.setAddress(addressOptional.get());
        deal.setRequests(queue);
        deal.setOwner(userId);
        deal.setDealModel(DealModel.PRIVATE);
        deal.setDealType(dealType);
        deal.setObject(objectType);
        deal.setCreated_at(new Date());
        deal.setPrice(price);
        insertIntoCSV(deal);
        addDealToCompany(userId, deal);
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
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
          @NotNull String price) {
    try {
      PublicDeal publicDeal = new PublicDeal();
      String uuid = UUID.randomUUID().toString();
      Queue queue = new Queue();
      queue.setId(uuid);
      Optional<Address> addressOptional = getAddress(addressId);

      if (createQueue(queue).equals(RequestStatuses.SUCCESS) && addressOptional.isPresent()) {
        publicDeal.setId(uuid);
        publicDeal.setName(name);
        publicDeal.setDescription(description);
        publicDeal.setAddress(addressOptional.get());
        publicDeal.setRequests(queue);
        publicDeal.setDealModel(DealModel.PUBLIC);
        publicDeal.setCurrentStatus(currentStatus);
        publicDeal.setHistory(createFirstDealHistory(uuid, currentStatus));
        publicDeal.setOwner(userId);
        publicDeal.setDealType(dealType);
        publicDeal.setObject(objectType);
        publicDeal.setCreated_at(new Date());
        publicDeal.setPrice(price);
        insertIntoCSV(publicDeal);
        addDealToCompany(userId, publicDeal);
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Deal> manageDeal(@NotNull String id) {
    Optional<Deal> deal = getDealById(id);
    if (deal.isPresent()) {
      return deal;
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses removeDeal(@NotNull String id) {
    Optional<Deal> optionalDeal = getDealById(id);
    if (optionalDeal.isPresent()) {
      Deal deal = optionalDeal.get();

      switch (deal.getDealModel()) {
        case PUBLIC:
          return removePublicDeal((PublicDeal) deal);
        case PRIVATE:
          return removeSimpleDeal(deal);
        default:
          return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL);
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
    Optional<Deal> optionalDeal = getDealById(id);
    Optional<Address> addressOptional = getAddress(addressId);
    if (optionalDeal.isPresent() && addressOptional.isPresent()) {
      Deal deal = optionalDeal.get();
      deal.setName(name);
      deal.setDescription(description);
      deal.setAddress(addressOptional.get());
      deal.setDealType(dealType);
      deal.setObject(objectType);
      deal.setPrice(price);

      return updateDeal(deal);
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<List<PublicDeal>> getGlobalDeals(@NotNull String userId) {
    Optional<List<PublicDeal>> optionalDeals = getPublicDealsList();
    if (optionalDeals.isPresent()) {
      List<PublicDeal> deals = optionalDeals.get();
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
  public Optional<List<Deal>> getMyDeals(@NotNull String userId) {
    Optional<List<Deal>> optionalDeals = getAllDeals();
    Optional<User> checkUser = getUser(userId);
    if (optionalDeals.isPresent() && checkUser.isPresent()) {
      List<Deal> deals = optionalDeals.get();
      deals = deals.stream()
              .filter(deal -> deal.getOwner().equals(userId))
              .collect(Collectors.toList());
      return Optional.of(deals);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses setStatus(@NotNull String id, @NotNull DealStatus newStatus) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      try {
        PublicDeal publicDeal = (PublicDeal) dealOptional.get();
        DealHistory newDHistory = new DealHistory();
        newDHistory.setId(publicDeal.getId());
        newDHistory.setStatus(newStatus);
        newDHistory.setText(newStatus.getMessage());
        newDHistory.setCreated_at(new Date());
        publicDeal.setCurrentStatus(newStatus);
        updatePublicDeal(publicDeal);
        return addDealHistory(newDHistory);
      } catch (ClassCastException e) {
        log.error(Constants.DEAL_NOT_PUBLIC);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses addDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Deal> dealOptional = getDealById(id);

    if (dealOptional.isPresent() && getUser(userId).isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();

      List<String> requestsList = new ArrayList<>();
      requests.getItems().forEach(item -> {
        if (!item.equals(Constants.EMPTY_STRING)) {
          requestsList.add(item);
        }
      });

      if (requestsList.contains(userId)) {
        log.error(Constants.ALREADY_IN_QUEUE + userId);
        return RequestStatuses.FAILED;
      } else if (!deal.getPerformer().equals(Constants.EMPTY_STRING)) {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      }
      requestsList.add(userId);
      requests.setItems(requestsList);
      return updateQueue(requests);
    } else {
      log.error(Constants.UNDEFINED_USER_OR_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getDealQueue(@NotNull String id) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      return Optional.of(dealOptional.get().getRequests());
    } else {
      log.error(Constants.UNDEFINED_DEAL);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses manageDealRequest(@NotNull String userId, @NotNull String id, boolean accept) {
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
  public RequestStatuses acceptDealRequest(@NotNull String userId, @NotNull String id) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();

      if (requests.getItems().contains(userId)) {
        requests.setItems(new ArrayList<>());
        updateQueue(requests);
        deal.setPerformer(userId);

        return updateDeal(deal);
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
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      Deal deal = dealOptional.get();
      Queue requests = deal.getRequests();

      if (requests.getItems().contains(userId)) {
        List<String> requestsList = requests.getItems()
                .stream().filter(requestId -> !requestId.equals(userId))
                .collect(Collectors.toList());

        requests.setItems(requestsList);
        updateQueue(requests);
        return RequestStatuses.SUCCESS;
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
    Optional<User> userOptional = getUser(userId);
    Optional<Deal> dealOptional = getDealById(id);
    if (userOptional.isPresent() && dealOptional.isPresent()) {
      User user = userOptional.get();
      Queue queue = user.getQueue();

      List<String> requestsList = new ArrayList<>();
      queue.getItems().forEach(item -> {
        if (!item.equals(Constants.EMPTY_STRING)) {
          requestsList.add(item);
        }
      });

      if (requestsList.contains(id)) {
        log.info(Constants.ALREADY_IN_QUEUE + id);
        return RequestStatuses.FAILED;
      } else {
        requestsList.add(id);
        queue.setItems(requestsList);
        updateQueue(queue);
        return RequestStatuses.SUCCESS;
      }
    } else {
      log.error(Constants.UNDEFINED_USER_OR_DEAL);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public Optional<Queue> getMyQueue(@NotNull String userId) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      return Optional.of(userOptional.get().getQueue());
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses manageDealPerform(@NotNull String userId, @NotNull String id, boolean accept) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      if (accept) {
        return acceptDealPerform(userId, id);
      } else {
        return refuseDealPerform(userId, id);
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses acceptDealPerform(@NotNull String userId, @NotNull String id) {
    Optional<User> userOptional = getUser(userId);
    Optional<Deal> dealOptional = getDealById(id);
    if (userOptional.isPresent() && dealOptional.isPresent()) {
      User user = userOptional.get();
      Deal deal = dealOptional.get();
      Queue requests = user.getQueue();

      if (deal.getPerformer().equals(Constants.EMPTY_STRING)) {
        if (requests.getItems().contains(id)) {
          List<String> requestsList = requests.getItems()
                  .stream().filter(requestId -> !requestId.equals(id))
                  .collect(Collectors.toList());

          deal.setPerformer(userId);

          requests.setItems(requestsList);
          updateQueue(requests);

          return updateDeal(deal);
        } else {
          log.error(Constants.UNDEFINED_PERFORM);
          return RequestStatuses.FAILED;
        }
      } else {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_PERFORM);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public RequestStatuses refuseDealPerform(@NotNull String userId, @NotNull String id) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent() && userOptional.get().getQueue().getItems().contains(id)) {
      User user = userOptional.get();
      Queue requests = user.getQueue();

      List<String> requestsList = requests.getItems()
              .stream().filter(requestId -> !requestId.equals(id))
              .collect(Collectors.toList());

      requests.setItems(requestsList);
      updateQueue(requests);
      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_PERFORM);
      return RequestStatuses.FAILED;
    }
  }

  @Override
  public void clearDB() {
    List<Class> classList = new ArrayList<>();
    classList.add(Deal.class);
    classList.add(PublicDeal.class);
    classList.add(DealHistory.class);
    classList.add(Queue.class);
    classList.add(User.class);
    classList.add(Company.class);
    classList.add(Address.class);
    classList.forEach(this::deleteFile);
  }

  @Override
  public void initDB() {
    try {
      insertIntoCSV(Deal.class, new ArrayList<>(), true);
      insertIntoCSV(PublicDeal.class, new ArrayList<>(), true);
      insertIntoCSV(DealHistory.class, new ArrayList<>(), true);
      insertIntoCSV(Queue.class, new ArrayList<>(), true);
      insertIntoCSV(User.class, new ArrayList<>(), true);
      insertIntoCSV(Address.class, new ArrayList<>(), true);
      insertIntoCSV(Company.class, new ArrayList<>(), true);
    } catch (IOException e) {
      log.error(e);
    }
  }

  public RequestStatuses createCompany(@NotNull User user) {
    Company company = new Company();
    String uuid = UUID.randomUUID().toString();
    company.setId(uuid);
    try {
      insertIntoCSV(company);
      return addUserToCompany(company.getId(), user);
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses updateCompany(@NotNull Company updatedCompany) {
    Optional<List<Company>> optionalCompanies = getCompanies();
    if (optionalCompanies.isPresent()) {
      List<Company> companies = optionalCompanies.get();
      companies = companies.stream()
              .filter(company -> !company.getId().equals(updatedCompany.getId()))
              .collect(Collectors.toList());

      companies.add(updatedCompany);
      try {
        if (updatedCompany.getEmployees().isEmpty()) {
          deleteCompany(updatedCompany.getId());
        } else {
          insertIntoCSV(Company.class, companies, true);
        }
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses deleteCompany(@NotNull String id) {
    Optional<List<Company>> optionalCompanies = getCompanies();
    Optional<Company> optionalCompany = getCompany(id);
    if (optionalCompanies.isPresent() && optionalCompany.isPresent()) {
      List<Company> companies = optionalCompanies.get();
      companies = companies.stream()
              .filter(company -> !company.getId().equals(id))
              .collect(Collectors.toList());
      try {
        optionalCompany.get().getDeals().forEach(deal -> removeDeal(deal.getId()));
        insertIntoCSV(Company.class, companies, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Company>> getCompanies() {
    try {
      return Optional.of(getFromCSV(Company.class));
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  public Optional<Company> getCompany(@NotNull String id) {
    Optional<List<Company>> optionalCompanies = getCompanies();
    if (optionalCompanies.isPresent()) {
      List<Company> companies = optionalCompanies.get();
      return companies.stream()
              .filter(company -> company.getId().equals(id))
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  public Optional<Company> getUserCompany(@NotNull String userId) {
    Optional<List<Company>> optionalCompanies = getCompanies();
    if (optionalCompanies.isPresent()) {
      List<Company> companies = optionalCompanies.get();
      return companies.stream()
              .filter(company -> company
                      .getEmployees()
                      .stream()
                      .anyMatch(item -> item.getId()
                              .equals(userId)
                      )
              )
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  private RequestStatuses addUserToCompany(String id, User user) {
    Optional<Company> companyOptional = getCompany(id);
    if (companyOptional.isPresent()) {
      Company company = companyOptional.get();
      List<User> employees = company.getEmployees();
      employees.add(user);
      company.setEmployees(employees);
      return updateCompany(company);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses deleteUserFromCompany(@NotNull String userId) {
    Optional<Company> companyOptional = getUserCompany(userId);
    if (companyOptional.isPresent()) {
      Company company = companyOptional.get();
      List<User> employees = company.getEmployees();
      employees = employees.stream()
              .filter(user -> !user.getId().equals(userId))
              .collect(Collectors.toList());
      company.setEmployees(employees);
      return updateCompany(company);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses addDealToCompany(String userId, Deal deal) {
    Optional<Company> companyOptional = getUserCompany(userId);
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

  private RequestStatuses deleteCompanyDeal(Deal deal) {
    Optional<Company> companyOptional = getUserCompany(deal.getOwner());
    if (companyOptional.isPresent()) {
      Company company = companyOptional.get();
      List<Deal> deals = company.getDeals();
      deals = deals.stream()
              .filter(dealE -> !dealE.getId().equals(deal.getId()))
              .collect(Collectors.toList());
      company.setDeals(deals);
      return updateCompany(company);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses createUserOptional(
          @NotNull String name,
          @NotNull String phone,
          @NotNull Address address) {
    try {
      String uuid = UUID.randomUUID().toString();
      Queue queue = new Queue();
      queue.setId(uuid);
      if (createQueue(queue).equals(RequestStatuses.SUCCESS)) {
        User user = new User();
        user.setId(uuid);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        user.setQueue(queue);
        insertIntoCSV(user);
        return createCompany(user);
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<User>> getUsersOptional() {
    try {
      List<User> users = getFromCSV(User.class);
      return users.isEmpty() ? Optional.empty() : Optional.of(users);
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  private Optional<User> getUserOptional(String userId) {
    Optional<List<User>> userList = getUsers();
    if (userList.isPresent()) {
      List<User> users = userList.get();
      return users
              .stream()
              .filter(user -> user.getId().equals(userId))
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  private RequestStatuses updateUsersList(List<User> users) {
    try {
      insertIntoCSV(User.class, users, true);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses updateUser(@NotNull User editedUser) {
    Optional<List<User>> userList = getUsers();
    Optional<User> userOptional = getUser(editedUser.getId());
    if (userList.isPresent() && userOptional.isPresent()) {
      List<User> users = userList.get();

      users = users.stream()
              .filter(user -> !user.getId().equals(editedUser.getId()))
              .collect(Collectors.toList());
      users.add(editedUser);

      return updateUsersList(users);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses createQueue(@NotNull Queue queue) {
    try {
      insertIntoCSV(queue);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Queue>> getQueuesList() {
    try {
      return Optional.of(getFromCSV(Queue.class));
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_QUEUES_LIST);
      return Optional.empty();
    }
  }

  private Optional<Queue> getQueueById(String id) {
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

  public RequestStatuses updateQueue(@NotNull Queue queue) {
    Optional<List<Queue>> optionalQueues = getQueuesList();
    Optional<Queue> optionalQueue = getQueueById(queue.getId());
    if (optionalQueues.isPresent() && optionalQueue.isPresent()) {
      List<Queue> queues = optionalQueues.get();

      queues = queues.stream()
              .filter(queueL -> !queueL.getId().equals(queue.getId()))
              .collect(Collectors.toList());
      queues.add(queue);
      try {
        insertIntoCSV(Queue.class, queues, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses deleteQueue(@NotNull String id) {
    Optional<List<Queue>> optionalQueues = getQueuesList();
    if (optionalQueues.isPresent()) {
      List<Queue> queues = optionalQueues.get();
      queues = queues.stream()
              .filter(user -> !user.getId()
                      .equals(id))
              .collect(Collectors.toList());
      try {
        insertIntoCSV(Queue.class, queues, true);
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
      return Optional.of(getFromCSV(Deal.class));
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  private Optional<List<PublicDeal>> getPublicDealsList() {
    try {
      List<PublicDeal> dealList = getFromCSV(PublicDeal.class);
      if (dealList.isEmpty()) {
        log.debug(Constants.EMPTY_PUBLIC_DEALS);
        return Optional.empty();
      }
      return Optional.of(getFromCSV(PublicDeal.class));
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  private Optional<List<Deal>> getAllDeals() {
    try {
      List<Deal> deals = getFromCSV(Deal.class);
      deals.addAll(getFromCSV(PublicDeal.class));
      return Optional.of(deals);
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  private Optional<Deal> getDealById(String id) {
    Optional<List<Deal>> deals = getAllDeals();
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

  private RequestStatuses removePublicDeal(PublicDeal removedDeal) {
    Optional<List<PublicDeal>> publicDeals = getPublicDealsList();
    if (publicDeals.isPresent()) {
      List<PublicDeal> deals = publicDeals.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId().equals(removedDeal.getId()))
              .collect(Collectors.toList());

      try {
        deleteQueue(removedDeal.getRequests().getId());
        deleteCompanyDeal(removedDeal);
        removeDealHistoryByDeal(removedDeal.getId());
        insertIntoCSV(PublicDeal.class, deals, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses removeSimpleDeal(Deal removedDeal) {
    Optional<List<Deal>> dealsList = getDealsList();
    if (dealsList.isPresent()) {
      List<Deal> deals = dealsList.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId().equals(removedDeal.getId()))
              .collect(Collectors.toList());
      try {
        deleteQueue(removedDeal.getRequests().getId());
        deleteCompanyDeal(removedDeal);
        insertIntoCSV(Deal.class, deals, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses updateDeal(Deal deal) {
    switch (deal.getDealModel()) {
      case PUBLIC:
        return updatePublicDeal((PublicDeal) deal);
      case PRIVATE:
        return updateSimpleDeal(deal);
      default:
        return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses updateSimpleDeal(Deal updatedDeal) {
    Optional<List<Deal>> dealsList = getDealsList();
    if (dealsList.isPresent()) {
      List<Deal> deals = dealsList.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId().equals(updatedDeal.getId()))
              .collect(Collectors.toList());

      deals.add(updatedDeal);
      try {
        insertIntoCSV(Deal.class, deals, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses updatePublicDeal(PublicDeal updatedDeal) {
    Optional<List<PublicDeal>> publicDeals = getPublicDealsList();
    if (publicDeals.isPresent()) {
      List<PublicDeal> deals = publicDeals.get();
      deals = deals.stream()
              .filter(deal -> !deal.getId().equals(updatedDeal.getId()))
              .collect(Collectors.toList());

      deals.add(updatedDeal);
      try {
        insertIntoCSV(PublicDeal.class, deals, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private List<DealHistory> createFirstDealHistory(String parentId, DealStatus currentStatus) {
    List<DealHistory> dealHistoryList = new ArrayList<>();
    DealHistory dealHistory = new DealHistory();
    dealHistory.setId(parentId);
    dealHistory.setStatus(currentStatus);
    dealHistory.setText(currentStatus.getMessage());
    dealHistory.setCreated_at(new Date());
    addDealHistory(dealHistory);
    dealHistoryList.add(dealHistory);
    return dealHistoryList;
  }

  private RequestStatuses insertDealHistory(List<DealHistory> dealHistoryList) {
    try {
      insertIntoCSV(DealHistory.class, dealHistoryList, true);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses addDealHistory(@NotNull DealHistory dealHistoryItem) {
    Optional<List<DealHistory>> optionalDealHistories = getOptionalDealHistoryList();
    if (optionalDealHistories.isPresent()) {
      List<DealHistory> dealHistoryList = optionalDealHistories.get();
      dealHistoryList.add(dealHistoryItem);
      insertDealHistory(dealHistoryList);
      return RequestStatuses.SUCCESS;
    } else {
      log.error(Constants.UNDEFINED_DEAL_HISTORY);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<DealHistory>> getOptionalDealHistoryList() {
    try {
      List<DealHistory> dealHistory = getFromCSV(DealHistory.class);
      return Optional.of(dealHistory);
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  public Optional<List<DealHistory>> getDealHistoryByDeal(String id) {
    Optional<List<DealHistory>> optionalDealHistories = getOptionalDealHistoryList();
    if (optionalDealHistories.isPresent()) {
      List<DealHistory> dealHistoryList = optionalDealHistories.get();
      dealHistoryList = dealHistoryList.stream()
              .filter(item -> item.getId().equals(id))
              .collect(Collectors.toList());
      if (dealHistoryList.isEmpty()) {
        log.error(Constants.EMPTY_DEAL_HISTORY);
        return Optional.empty();
      } else {
        return Optional.of(dealHistoryList);
      }
    } else {
      log.error(Constants.UNDEFINED_DEAL_HISTORY);
      return Optional.empty();
    }
  }

  public Optional<DealHistory> getDealHistory(@NotNull String id) {
    Optional<List<DealHistory>> optionalDealHistories = getOptionalDealHistoryList();
    if (optionalDealHistories.isPresent()) {
      List<DealHistory> dealHistoryList = optionalDealHistories.get();
      return dealHistoryList.stream()
              .filter(item -> item.getId().equals(id))
              .findFirst();
    } else {
      log.info(Constants.UNDEFINED_DEAL_HISTORY);
      return Optional.empty();
    }
  }

  public RequestStatuses removeDealHistoryByDeal(@NotNull String id) {
    Optional<List<DealHistory>> optionalDealHistories = getOptionalDealHistoryList();
    if (optionalDealHistories.isPresent()) {
      List<DealHistory> dealHistoryList = optionalDealHistories.get();
      dealHistoryList = dealHistoryList.stream()
              .filter(item -> !item.getId().equals(id))
              .collect(Collectors.toList());
      return insertDealHistory(dealHistoryList);
    } else {
      log.error(Constants.UNDEFINED_DEAL_HISTORY);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses updateDealHistoryItem(@NotNull DealHistory updatedDH) {
    Optional<List<DealHistory>> listOptional = getOptionalDealHistoryList();
    Optional<DealHistory> dealHistoryOptional = getDealHistory(updatedDH.getId());
    if (listOptional.isPresent() &&
            dealHistoryOptional.isPresent()
    ) {
      List<DealHistory> list = listOptional.get();
      list = list.stream()
              .filter(item -> !item.getId().equals(updatedDH.getId()))
              .collect(Collectors.toList());
      list.add(updatedDH);
      return insertDealHistory(list);
    } else {
      return RequestStatuses.FAILED;
    }
  }

}

