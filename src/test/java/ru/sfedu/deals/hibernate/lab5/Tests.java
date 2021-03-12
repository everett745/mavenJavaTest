package ru.sfedu.deals.hibernate.lab5;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import ru.sfedu.deals.enums.DealModel;
import ru.sfedu.deals.enums.DealTypes;
import ru.sfedu.deals.enums.ObjectTypes;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;
import ru.sfedu.deals.lab5.provider.DataProvider;
import ru.sfedu.deals.lab5.provider.IProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Tests {
  private static final Logger log = LogManager.getLogger(Tests.class);
  private static final DataProvider staticProvider = new DataProvider();
  private static IProvider provider;

  private Address getAddress1() {
    Address address = new Address();
    address.setCity("Москва");
    address.setDistrict("Московская область");
    address.setRegion("Центральный");
    return address;
  }

  private Address getAddress2() {
    Address address = new Address();
    address.setCity("Ростов-на-Дону");
    address.setDistrict("Ростовсая область");
    address.setRegion("Южный");
    return address;
  }

  private Address getAddressFromBd() {
    insertAddress();
    return provider.selectAllAddress().get().get(0);
  }

  private User getUser() {
    User user = new User();
    user.setAddress(getAddress1());
    user.setName("Иван");
    user.setPhone("895615156413");
    return user;
  }

  private User getUserFromBd() {
    insertUser();
    return provider.selectAllUsers().get().get(0);
  }

  private Deal getDeal() {
    Deal deal = new Deal();
    deal.setAddress(getAddress2());
    deal.setName("Продажа квартиры");
    deal.setDescription("Продается квартира 25 кв м в новостройке");
    deal.setCreated_at(new Date());
    deal.setDealModel(DealModel.PUBLIC);
    deal.setDealType(DealTypes.SALE);
    deal.setObject(ObjectTypes.BUILDING);
    deal.setOwner(getUser());
    deal.setPrice("314123123");
    return deal;
  }

  private Deal getDealFromBd() {
    insertUser();
    return provider.selectAllDeals().get().get(0);
  }

  private Company getCompany() {
    Company company = new Company();
    List<Deal> deals = new ArrayList<>();
    List<User> users = new ArrayList<>();
    deals.add(getDeal());
    users.add(getUser());
    company.setEmployees(users);
    company.setDeals(deals);
    return company;
  }

  private Company getCompanyFromBd() {
    insertCompany();
    return provider.selectAllCompanies().get().get(0);
  }

  static void setUp(IProvider IProviderInstance) {
    provider = IProviderInstance;
  }

  void insertAddress(){
    log.debug("Start test: insertAddress");
    Assertions.assertEquals(staticProvider.insertAddress(getAddress1()), RequestStatuses.SUCCESS);
  }

  void selectAllAddresses(){
    log.debug("Start test: selectAllAddresses");
    insertAddress();
    Optional<List<Address>> list = provider.selectAllAddress();
    log.debug(list);
    Assertions.assertTrue(list.isPresent());
  }

  void deleteAddress(){
    log.debug("Start test: deleteAddress");
    Assertions.assertEquals(staticProvider.deleteAddress(getAddressFromBd()), RequestStatuses.SUCCESS);
  }

  void updateAddress(){
    log.debug("Start test: updateAddress");
    Address address = getAddressFromBd();
    address.setCity("Новый город");
    Assertions.assertEquals(staticProvider.updateAddress(address), RequestStatuses.SUCCESS);
  }

  void insertUser(){
    log.debug("Start test: insertUser");
    Assertions.assertEquals(staticProvider.insertUser(getUser()), RequestStatuses.SUCCESS);
  }

  void selectAllUsers(){
    log.debug("Start test: selectAllUsers");
    insertUser();
    Optional<List<User>> list = provider.selectAllUsers();
    log.debug(list);
    Assertions.assertTrue(list.isPresent());
  }

  void deleteUser(){
    log.debug("Start test: deleteUser");
    Assertions.assertEquals(staticProvider.deleteUser(getUserFromBd()), RequestStatuses.SUCCESS);
  }

  void updateUser(){
    log.debug("Start test: updateUser");
    User user = getUserFromBd();
    user.setName("Новое имя");
    Assertions.assertEquals(staticProvider.updateUser(user), RequestStatuses.SUCCESS);
  }

  void insertDeal() {
    log.debug("Start test: insertDeal");
    Assertions.assertEquals(staticProvider.insertDeal(getDeal()), RequestStatuses.SUCCESS);
  }

  void selectAllDeals(){
    log.debug("Start test: selectAllDeals");
    insertDeal();
    Optional<List<Deal>> list = provider.selectAllDeals();
    log.debug(list);
    Assertions.assertTrue(list.isPresent());
  }

  void deleteDeal(){
    log.debug("Start test: deleteDeal");
    Assertions.assertEquals(staticProvider.deleteDeal(getDealFromBd()), RequestStatuses.SUCCESS);
  }

  void updateDeal(){
    log.debug("Start test: updateDeal");
    Deal deal = getDealFromBd();
    deal.setName("Новое имя");
    Assertions.assertEquals(staticProvider.updateDeal(deal), RequestStatuses.SUCCESS);
  }


  void insertCompany() {
    log.debug("Start test: insertCompany");
    Assertions.assertEquals(staticProvider.insertCompany(getCompany()), RequestStatuses.SUCCESS);
  }

  void selectAllCompanies(){
    log.debug("Start test: selectAllCompanies");
    insertCompany();
    Optional<List<Company>> list = provider.selectAllCompanies();
    log.debug(list.get().get(0));
    Assertions.assertTrue(list.isPresent());
  }

  void deleteCompany(){
    log.debug("Start test: deleteCompany");
    Assertions.assertEquals(staticProvider.deleteCompany(getCompanyFromBd()), RequestStatuses.SUCCESS);
  }

  void updateCompany(){
    log.debug("Start test: updateCompany");
    insertCompany();
    Company company = getCompanyFromBd();
    List<Deal> deals = company.getDeals();
    deals.add(getDeal());
    company.setDeals(deals);
    Assertions.assertEquals(staticProvider.updateCompany(company), RequestStatuses.SUCCESS);
  }

}
