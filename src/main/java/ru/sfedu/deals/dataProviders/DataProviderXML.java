/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.deals.dataProviders;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.deals.Constants;
import ru.sfedu.deals.enums.*;
import ru.sfedu.deals.model.*;
import ru.sfedu.deals.model.Queue;
import ru.sfedu.deals.utils.PropertyProvider;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataProviderXML implements IDataProvider {
  private static final Logger log = LogManager.getLogger(DataProviderXML.class);
  private static IDataProvider INSTANCE;

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderXML();
    }
    return INSTANCE;
  }

  private <T> void insertIntoXML(T object) throws IOException {
    insertIntoXML(object.getClass(), Collections.singletonList(object), false);
  }

  private <T> void insertIntoXML(Class<?> tClass, List<T> objectList, boolean overwrite) throws IOException {
    List<T> tList;
    WrapperXML<T> xmlWrap = new WrapperXML<T>();
    if (!overwrite) {
      tList = (List<T>) getFromXML(tClass);
      tList.addAll(objectList);
    } else {
      tList = objectList;
    }

    try {
      FileWriter writer = new FileWriter(getFile(tClass));
      Persister serializer = new Persister();

      xmlWrap.setList(tList);
      serializer.write(xmlWrap, writer);
      writer.close();
    } catch (Exception e) {
      log.error(e);
      throw new IOException(e);
    }
  }

  private <T> List<T> getFromXML(Class<T> tClass) throws IOException {
    Reader reader = new FileReader(getFile(tClass));
    Persister serializer = new Persister();
    WrapperXML<T> xmlList = new WrapperXML<>();
    try {
      xmlList = serializer.read(xmlList.getClass(), reader);
      reader.close();
      return xmlList.getList() == null ? new ArrayList<>() : xmlList.getList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
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
    File path = new File(PropertyProvider.getProperty(Constants.XML_PATH));
    File file = new File(PropertyProvider.getProperty(Constants.XML_PATH)
            + tClass.getSimpleName().toLowerCase()
            + PropertyProvider.getProperty(Constants.XML_EXTENSION));

    if (!file.exists()) {
      log.debug(Constants.CREATE_FILE + path);
      log.debug(file.createNewFile());
    }
    return file;
  }

  @Override
  public RequestStatuses createUser(
          @NotNull String name,
          @NotNull String phone,
          long addressId) {
    Optional<Address> addressOptional = getAddress(addressId);
    if (addressOptional.isPresent()) {
      return createUserOptional(name, phone, addressOptional.get());
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
    Optional<User> optionalUser = getUser(userId);
    Optional<Address> address = getAddress(addressId);
    if (optionalUser.isPresent() && address.isPresent()) {
      User user = optionalUser.get();
      user.setName(name);
      user.setPhone(phone);
      user.setAddress(address.get());
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
        return rewriteUsers(users);
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
      List<Address> addressList = getFromXML(Address.class);
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
      insertIntoXML(address);
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
        insertIntoXML(Address.class, addressList, true);
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
        insertIntoXML(Address.class, addressList, true);
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (IOException e) {
      return RequestStatuses.FAILED;
    }
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
      Optional<Address> addressOptional = getAddress(addressId);
      if (addressOptional.isEmpty()) {
        return RequestStatuses.FAILED;
      }

      deal.setId(UUID.randomUUID().toString());
      deal.setName(name);
      deal.setDescription(description);
      deal.setAddress(addressOptional.get());
      deal.setRequests(new Queue());
      deal.setOwner(userId);
      deal.setDealModel(DealModel.PRIVATE);
      deal.setDealType(dealType);
      deal.setObject(objectType);
      deal.setCreated_at(new Date());
      deal.setPrice(price);
      insertIntoXML(deal);
      addDealToCompany(userId, deal);
      return RequestStatuses.SUCCESS;
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
      Optional<Address> address = getAddress(addressId);
      if (address.isEmpty()) {
        return RequestStatuses.FAILED;
      }
      PublicDeal publicDeal = new PublicDeal();
      String uuid = UUID.randomUUID().toString();
      Queue queue = new Queue();

      publicDeal.setId(uuid);
      publicDeal.setName(name);
      publicDeal.setDescription(description);
      publicDeal.setAddress(address.get());
      publicDeal.setRequests(queue);
      publicDeal.setDealModel(DealModel.PUBLIC);
      publicDeal.setCurrentStatus(currentStatus);
      publicDeal.setHistory(createFirstDealHistory(uuid, currentStatus));
      publicDeal.setOwner(userId);
      publicDeal.setDealType(dealType);
      publicDeal.setObject(objectType);
      publicDeal.setCreated_at(new Date());
      publicDeal.setPrice(price);
      insertIntoXML(publicDeal);
      addDealToCompany(userId, publicDeal);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
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
    Optional<Address> address = getAddress(addressId);
    if (optionalDeal.isPresent() && address.isPresent()) {
      Deal deal = optionalDeal.get();
      deal.setName(name);
      deal.setDescription(description);
      deal.setAddress(address.get());
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
  public RequestStatuses setStatus(@NotNull String id, @NotNull DealStatus newStatus) {
    Optional<Deal> dealOptional = getDealById(id);
    if (dealOptional.isPresent()) {
      try {
        PublicDeal publicDeal = (PublicDeal) dealOptional.get();
        List<DealHistory> dealHistoryList = publicDeal.getHistory();
        DealHistory newDHistory = new DealHistory();
        newDHistory.setId(publicDeal.getId());
        newDHistory.setStatus(newStatus);
        newDHistory.setText(newStatus.getMessage());
        newDHistory.setCreated_at(new Date());
        dealHistoryList.add(newDHistory);
        publicDeal.setHistory(dealHistoryList);
        publicDeal.setCurrentStatus(newStatus);

        return updateDeal(publicDeal);
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
      List<String> requestsList = requests.getItems();

      if (requestsList.contains(userId)) {
        log.error(Constants.ALREADY_IN_QUEUE + userId);
        return RequestStatuses.FAILED;
      } else if (deal.getPerformer() != null
              && !deal.getPerformer().equals(Constants.EMPTY_STRING)
      ) {
        log.error(Constants.ALREADY_DEAL_PERFORMER);
        return RequestStatuses.FAILED;
      }
      requestsList.add(userId);
      requests.setItems(requestsList);
      deal.setRequests(requests);
      return updateDeal(deal);
    } else {
      log.error(Constants.UNDEFINED_DEAL);
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
        deal.setRequests(requests);
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
        deal.setRequests(requests);
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
  public RequestStatuses addDealPerformer(@NotNull String userId, @NotNull String id) {
    Optional<User> userOptional = getUser(userId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      Queue queue = user.getQueue();

      List<String> requestsList = queue.getItems();

      if (requestsList.contains(id)) {
        log.info(Constants.ALREADY_IN_QUEUE + id);
        return RequestStatuses.FAILED;
      } else {
        requestsList.add(id);
        queue.setItems(requestsList);
        user.setQueue(queue);
        return updateUser(user);
      }
    } else {
      log.error(Constants.UNDEFINED_QUEUE);
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
      Queue queue = user.getQueue();

      if (deal.getPerformer() == null) {
        if (queue.getItems().contains(id)) {
          List<String> requestsList = queue.getItems()
                  .stream().filter(requestId -> !requestId.equals(id))
                  .collect(Collectors.toList());

          deal.setPerformer(userId);
          updateDeal(deal);

          queue.setItems(requestsList);
          user.setQueue(queue);
          return updateUser(user);
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
      Queue queue = user.getQueue();

      List<String> requestsList = queue.getItems()
              .stream().filter(requestId -> !requestId.equals(id))
              .collect(Collectors.toList());

      queue.setItems(requestsList);
      user.setQueue(queue);
      return updateUser(user);
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
    classList.add(Address.class);
    classList.add(Company.class);
    classList.forEach(this::deleteFile);
  }

  @Override
  public void initDB() {
    try {
      insertIntoXML(Deal.class, new ArrayList<>(), true);
      insertIntoXML(PublicDeal.class, new ArrayList<>(), true);
      insertIntoXML(User.class, new ArrayList<>(), true);
      insertIntoXML(Address.class, new ArrayList<>(), true);
      insertIntoXML(DealHistory.class, new ArrayList<>(), true);
      insertIntoXML(Company.class, new ArrayList<>(), true);
    } catch (IOException e) {
      log.error(e);
    }
  }

  public RequestStatuses createCompany(@NotNull User user) {
    Company company = new Company();
    String uuid = UUID.randomUUID().toString();
    company.setId(uuid);
    try {
      insertIntoXML(company);
      return addUserToCompany(company.getId(), user);
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
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

  private Optional<List<Company>> getCompanies() {
    try {
      return Optional.of(getFromXML(Company.class));
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
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
          insertIntoXML(Company.class, companies, true);
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
        insertIntoXML(Company.class, companies, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
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

  private RequestStatuses updateCompanyDeal(Deal updatedDeal) {
    Optional<Company> companyOptional = getUserCompany(updatedDeal.getOwner());
    if (companyOptional.isPresent()) {
      Company company = companyOptional.get();
      List<Deal> deals = company.getDeals();
      deals = deals.stream()
              .filter(dealE -> !dealE.getId().equals(updatedDeal.getId()))
              .collect(Collectors.toList());

      deals.add(updatedDeal);
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
      if (!createQueue(queue).equals(RequestStatuses.SUCCESS)) {
        return RequestStatuses.FAILED;
      }
      User user = new User();
      user.setId(uuid);
      user.setName(name);
      user.setPhone(phone);
      user.setAddress(address);
      user.setQueue(new Queue());
      insertIntoXML(user);
      return createCompany(user);
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses createQueue(@NotNull Queue queue) {
    try {
      insertIntoXML(queue);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Queue>> getQueues() {
    try {
      List<Queue> queues = getFromXML(Queue.class);
      return queues.isEmpty() ? Optional.empty() : Optional.of(queues);
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  public Optional<Queue> getQueue(@NotNull String id) {
    Optional<List<Queue>> queuesList = getQueues();
    if (queuesList.isPresent()) {
      List<Queue> list = queuesList.get();
      return list
              .stream()
              .filter(queue -> queue.getId().equals(id))
              .findFirst();
    } else {
      return Optional.empty();
    }
  }

  public RequestStatuses updateQueue(@NotNull Queue queue) {
    Optional<List<Queue>> queueList = getQueues();
    Optional<Queue> queueOptional = getQueue(queue.getId());
    if (queueOptional.isPresent() && queueList.isPresent()) {
      List<Queue> queues = queueList.get();
      queues = queues.stream()
              .filter(item -> item.getId().equals(queue.getId()))
              .collect(Collectors.toList());
      try {
        insertIntoXML(Queue.class, queues, true);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses deleteQueue(@NotNull String id) {
    Optional<List<Queue>> optionalQueues = getQueues();
    if (optionalQueues.isPresent()) {
      List<Queue> queues = optionalQueues.get();
      queues = queues.stream()
              .filter(user -> !user.getId()
                      .equals(id))
              .collect(Collectors.toList());
      try {
        insertIntoXML(Queue.class, queues, true);
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

  private Optional<List<User>> getUsersOptional() {
    try {
      List<User> users = getFromXML(User.class);
      return users.isEmpty() ? Optional.empty() : Optional.of(users);
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
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

      return rewriteUsers(users);
    } else {
      return RequestStatuses.FAILED;
    }
  }

  private RequestStatuses rewriteUsers(List<User> users) {
    try {
      insertIntoXML(User.class, users, true);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
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
    dealHistoryList.add(dealHistory);
    return dealHistoryList;
  }

  private Optional<List<DealHistory>> getOptionalDealHistoryList() {
    try {
      List<DealHistory> dealHistory = getFromXML(DealHistory.class);
      return Optional.of(dealHistory);
    } catch (IOException e) {
      log.error(e);
      return Optional.empty();
    }
  }

  @Override
  public RequestStatuses addDealHistory(@NotNull DealHistory dealHistoryItem) {
    try {
      insertIntoXML(dealHistoryItem);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      return RequestStatuses.FAILED;
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

  @Override
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

  @Override
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

  private RequestStatuses insertDealHistory(List<DealHistory> dealHistoryList) {
    try {
      insertIntoXML(DealHistory.class, dealHistoryList, true);
      return RequestStatuses.SUCCESS;
    } catch (IOException e) {
      log.error(e);
      return RequestStatuses.FAILED;
    }
  }

  private Optional<List<Deal>> getAllDeals() {
    try {
      List<Deal> deals = getFromXML(Deal.class);
      deals.addAll(getFromXML(PublicDeal.class));
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

  private Optional<List<Deal>> getDealsList() {
    try {
      return Optional.of(getFromXML(Deal.class));
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
      return Optional.empty();
    }
  }

  private Optional<List<PublicDeal>> getPublicDealsList() {
    try {
      List<PublicDeal> dealList = getFromXML(PublicDeal.class);
      if (dealList.isEmpty()) {
        log.debug(Constants.EMPTY_PUBLIC_DEALS);
        return Optional.empty();
      }

      return Optional.of(dealList);
    } catch (IOException e) {
      log.error(e);
      log.error(Constants.UNDEFINED_DEALS_LIST);
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
        deleteCompanyDeal(removedDeal);
        insertIntoXML(PublicDeal.class, deals, true);
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
        deleteCompanyDeal(removedDeal);
        insertIntoXML(Deal.class, deals, true);
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
        insertIntoXML(Deal.class, deals, true);
        updateCompanyDeal(updatedDeal);
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
        insertIntoXML(PublicDeal.class, deals, true);
        updateCompanyDeal(updatedDeal);
        return RequestStatuses.SUCCESS;
      } catch (IOException e) {
        log.error(e);
        return RequestStatuses.FAILED;
      }
    } else {
      return RequestStatuses.FAILED;
    }
  }
}
