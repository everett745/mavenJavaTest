package ru.sfedu.maven1.dataProviders;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;
import ru.sfedu.maven1.TestBase;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.*;

import java.util.Optional;
import java.util.UUID;

public class DataProviderCSVTest extends TestBase {

  private static final DataProvider dataProvider = DataProviderCSV.getInstance();
  private static Logger log = LogManager.getLogger(DataProviderCSVTest.class);
  private static User savedUser;
  private static User savedUser1;
  private static Deal savedDeal;
  private static Deal savedPublicDeal;

  private static Queue getTestQueue() {
    Queue queue = new Queue();
    queue.setId(TestsConstants.TEST_USER_QUEUE_ID);
    queue.setItems(TestsConstants.TEST_USER_QUEUE_ITEMS);
    return queue;
  }

  private static Address getTestAddress() {
    Address address = new Address();
    address.setId(TestsConstants.TEST_ADDRESS_ID);
    address.setCity(TestsConstants.TEST_ADDRESS_CITY);
    address.setRegion(TestsConstants.TEST_ADDRESS_REGION);
    address.setDistrict(TestsConstants.TEST_ADDRESS_DISTRICT);
    return address;
  }

  private static User getTestUserA() {
    User user = new User();
    user.setId(TestsConstants.TEST_USER_1_ID);
    user.setName(TestsConstants.TEST_USER_1_NAME);
    user.setPhone(TestsConstants.TEST_USER_1_PHONE);
    user.setQueue(new Queue());
    user.setAddress(getTestAddress());
    return user;
  }

  private static User getTestUserB() {
    User user = new User();
    user.setId(TestsConstants.TEST_USER_2_ID);
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);
    user.setQueue(new Queue());
    user.setAddress(getTestAddress());
    return user;
  }

  private static Deal getTestDeal() {
    Deal deal = new Deal();
    deal.setName(TestsConstants.TEST_DEAL_NAME);
    deal.setDescription(TestsConstants.TEST_DEAL_NAME);
    deal.setAddress(getTestAddress());
    deal.setDealType(TestsConstants.TEST_DEAL_TYPE);
    deal.setObject(TestsConstants.TEST_PUBLIC_DEAL_OBJECT);
    deal.setPrice(TestsConstants.TEST_DEAL_PRICE);
    return deal;
  }

  private static PublicDeal getTestPublicDeal() {
    PublicDeal deal = new PublicDeal();
    deal.setName(TestsConstants.TEST_DEAL_NAME);
    deal.setDescription(TestsConstants.TEST_DEAL_NAME);
    deal.setAddress(getTestAddress());
    deal.setCurrentStatus(TestsConstants.TEST_PUBLIC_DEAL_STATUS);
    deal.setDealType(TestsConstants.TEST_DEAL_TYPE);
    deal.setObject(TestsConstants.TEST_PUBLIC_DEAL_OBJECT);
    deal.setPrice(TestsConstants.TEST_DEAL_PRICE);
    return deal;
  }

  @BeforeAll
  static void setUp() {
    dataProvider.deleteAll();

    User user = getTestUserA();
    dataProvider.createUser(
            user.getName(),
            user.getPhone(),
            user.getAddress());

    User user1 = getTestUserB();
    dataProvider.createUser(
            user1.getName(),
            user1.getPhone(),
            user1.getAddress());

    savedUser = dataProvider.getUsers().get().get(0);
    savedUser1 = dataProvider.getUsers().get().get(1);

    Deal deal = getTestDeal();
    dataProvider.createDeal(
            savedUser.getId(),
            deal.getName(),
            deal.getDescription(),
            deal.getAddress(),
            deal.getDealType(),
            deal.getObject(),
            deal.getPrice());

    PublicDeal publicDeal = getTestPublicDeal();
    dataProvider.createDeal(
            savedUser1.getId(),
            publicDeal.getName(),
            publicDeal.getDescription(),
            publicDeal.getAddress(),
            publicDeal.getCurrentStatus(),
            publicDeal.getDealType(),
            publicDeal.getObject(),
            publicDeal.getPrice());

    savedDeal = dataProvider.getMyDeals(savedUser.getId()).get().get(0);
    savedPublicDeal = dataProvider.getMyDeals(savedUser1.getId()).get().get(0);
  }

  @Test
  void preTest() {}

  @Test
  void createUserCorrect() {
    User user = getTestUserB();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createUser(
            user.getName(),
            user.getPhone(),
            user.getAddress())
    );
  }

  @Test
  void createUserIncorrect() {
    User user = getTestUserB();

    Assertions.assertThrows(NullPointerException.class, () -> dataProvider.createUser(
            null,
            user.getPhone(),
            user.getAddress()));
  }

  @Test
  void getUserByIdCorrect() {
    Optional<User> user = dataProvider.getUser(savedUser.getId());
    Assertions.assertTrue(user.isPresent());
    Assertions.assertEquals(savedUser.getName(), user.get().getName());
    Assertions.assertEquals(savedUser.getPhone(), user.get().getPhone());
    Assertions.assertEquals(savedUser.getAddress(), user.get().getAddress());
  }

  @Test
  void getUserByIdIncorrect() {
    Optional<User> user = dataProvider.getUser(UUID.randomUUID());
    Assertions.assertTrue(user.isPresent());
  }

  @Test
  void editUserCorrect() {
    User user = savedUser;
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.editUser(user));

    User newUser = dataProvider.getUser(user.getId()).get();

    Assertions.assertEquals(newUser.getName(), TestsConstants.TEST_USER_2_NAME);
    Assertions.assertEquals(newUser.getPhone(), TestsConstants.TEST_USER_2_PHONE);
  }

  @Test
  void editUserIncorrect() {
    User user = savedUser;
    user.setId(UUID.randomUUID());
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.editUser(user));
  }

  @Test
  void deleteUserCorrect() {
    User user = savedUser;

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.deleteUser(user.getId()));
  }

  @Test
  void deleteUserIncorrect() {
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.deleteUser(UUID.randomUUID()));
  }

  @Test
  void getUsersCorrect() {
    log.debug(dataProvider.getUsers());
  }

  @Test
  void getUsersIncorrect() {
    dataProvider.deleteAll();
    log.debug(dataProvider.getUsers());
  }

  @Test
  void getAddresses() {
    log.debug(dataProvider.getAddresses());
  }

  @Test
  void getAddressCorrect() {
    Assertions.assertTrue(dataProvider.getAddress(1).isPresent());
  }

  @Test
  void getAddressIncorrect() {
    Assertions.assertTrue(dataProvider.getAddress(99999).isPresent());
  }

  @Test
  void getAddressByNameCorrect() {
    Assertions.assertTrue(dataProvider.getAddress("Моск").isPresent());
  }

  @Test
  void getAddressByNameIncorrect() {
    Assertions.assertTrue(dataProvider.getAddress("[]asd").isPresent());
  }

  @Test
  void getQueueCorrect() { }

  @Test
  void getQueueIncorrect() { }

  @Test
  void createDealCorrect() {
    Deal deal = getTestDeal();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createDeal(
                savedUser.getId(),
                deal.getName(),
                deal.getDescription(),
                deal.getAddress(),
                deal.getDealType(),
                deal.getObject(),
                deal.getPrice())
    );
  }

  @Test
  void createDealIncorrect() {
    Deal deal = getTestDeal();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createDeal(
                    UUID.randomUUID(),
                    deal.getName(),
                    deal.getDescription(),
                    null,
                    deal.getDealType(),
                    deal.getObject(),
                    deal.getPrice())
    );
  }

  @Test
  void createPublicDealCorrect() {
    PublicDeal publicDeal = getTestPublicDeal();
    Assertions.assertEquals(
        RequestStatuses.SUCCESS,
        dataProvider.createDeal(
            savedUser1.getId(),
            publicDeal.getName(),
            publicDeal.getDescription(),
            publicDeal.getAddress(),
            publicDeal.getCurrentStatus(),
            publicDeal.getDealType(),
            publicDeal.getObject(),
            publicDeal.getPrice())
    );
  }

  @Test
  void createPublicDealIncorrect() {
    PublicDeal publicDeal = getTestPublicDeal();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createDeal(
                    UUID.randomUUID(),
                    publicDeal.getName(),
                    publicDeal.getDescription(),
                    null,
                    publicDeal.getCurrentStatus(),
                    publicDeal.getDealType(),
                    publicDeal.getObject(),
                    publicDeal.getPrice())
    );
  }

  @Test
  void getGlobalDealsCorrect() {
    Assertions.assertTrue(dataProvider.getGlobalDeals(savedUser.getId()).isPresent());
    log.info(dataProvider.getGlobalDeals(savedUser.getId()).get());
  }

  @Test
  void getGlobalDealsIncorrect() {
    dataProvider.deleteAll();
    Assertions.assertTrue(dataProvider.getGlobalDeals(savedUser.getId()).isPresent());
  }

  @Test
  void getMyDealsCorrect() {
    Assertions.assertTrue(dataProvider.getMyDeals(savedUser.getId()).isPresent());
    log.info(dataProvider.getMyDeals(savedUser.getId()).get());
  }

  @Test
  void getMyDealsIncorrect() {
    Assertions.assertTrue(dataProvider.getMyDeals(UUID.randomUUID()).isPresent());
    log.info(dataProvider.getMyDeals(UUID.randomUUID()).get());
  }

  @Test
  void manageDealCorrect() {
    Assertions.assertTrue(dataProvider.manageDeal(savedDeal.getId()).isPresent());
    log.info(dataProvider.manageDeal(savedDeal.getId()).get());
  }

  @Test
  void manageDealIncorrect() {
    Assertions.assertTrue(dataProvider.manageDeal(UUID.randomUUID()).isPresent());
    log.info(dataProvider.manageDeal(savedDeal.getId()).get());
  }

  @Test
  void removeDealCorrect() {
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.removeDeal(savedPublicDeal.getId()));
  }

  @Test
  void removeDealIncorrect() {
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.removeDeal(UUID.randomUUID()));
  }

  @Test
  void updateDealCorrect() {
    Deal deal = savedDeal;
    deal.setPrice(TestsConstants.TEST_PUBLIC_DEAL_PRICE);
    deal.setName(TestsConstants.TEST_PUBLIC_DEAL_NAME);
    deal.setDescription(TestsConstants.TEST_PUBLIC_DEAL_DESCRIPTION);
    deal.setObject(TestsConstants.TEST_PUBLIC_DEAL_OBJECT);

    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.updateDeal(deal));
    Deal updatedDeal = dataProvider.manageDeal(deal.getId()).get();

    Assertions.assertEquals(updatedDeal.getName(), TestsConstants.TEST_PUBLIC_DEAL_NAME);
    Assertions.assertEquals(updatedDeal.getPrice(), TestsConstants.TEST_PUBLIC_DEAL_PRICE);
    Assertions.assertEquals(updatedDeal.getDescription(), TestsConstants.TEST_PUBLIC_DEAL_DESCRIPTION);
    Assertions.assertEquals(updatedDeal.getObject(), TestsConstants.TEST_PUBLIC_DEAL_OBJECT);
  }

  @Test
  void updateDealIncorrect() {
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.removeDeal(UUID.randomUUID()));
  }

  @Test
  void setStatusCorrect(){
    log.debug(savedPublicDeal);
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.setStatus(savedPublicDeal.getId(), DealStatus.AD));
    log.debug(dataProvider.manageDeal(savedPublicDeal.getId()).get());
  }

  @Test
  void setStatusIncorrect(){
    log.debug(savedDeal);
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.setStatus(savedDeal.getId(), DealStatus.AD));
    log.debug(dataProvider.manageDeal(savedDeal.getId()).get());
  }

  @Test
  void addDealRequestCorrect(){
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealRequest(savedUser.getId(), savedDeal.getId()));
  }

  @Test
  void addDealRequestIncorrect(){
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealRequest(savedUser.getId(), savedDeal.getId()));
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealRequest(savedUser.getId(), savedDeal.getId()));
  }

  @Test
  void getDealQueueCorrect(){
    Queue queue = dataProvider.getDealQueue(savedDeal.getId()).get();
    Assertions.assertEquals(queue.getId(), savedDeal.getRequests().getId());
  }

  @Test
  void getDealQueueIncorrect(){
    Queue queue = dataProvider.getDealQueue(UUID.randomUUID()).get();
    Assertions.assertEquals(queue.getId(), savedDeal.getRequests().getId());
  }

  @Test
  void manageDealRequestQueueCorrect(){
    dataProvider.addDealRequest(savedDeal.getId(), savedUser.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealRequest(savedUser.getId(),savedDeal.getId(),  true));
  }

  @Test
  void manageDealRequestIncorrect(){
    dataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealRequest(UUID.randomUUID(), savedDeal.getId(),  true));
  }

  @Test
  void acceptDealRequestCorrect() {
    dataProvider.addDealRequest(savedDeal.getId(), savedUser.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.acceptDealRequest(savedUser.getId(),savedDeal.getId()));
  }

  @Test
  void acceptDealRequestIncorrect() {
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.acceptDealRequest(UUID.randomUUID(), savedDeal.getId()));
  }

  @Test
  void refuseDealRequestCorrect() {
    dataProvider.addDealRequest(savedDeal.getId(), savedUser.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.refuseDealRequest(savedUser.getId(),savedDeal.getId()));
  }

  @Test
  void refuseDealRequestIncorrect() {
    dataProvider.addDealRequest(savedDeal.getId(), savedUser.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.refuseDealRequest(UUID.randomUUID(),savedDeal.getId()));
  }

  @Test
  void addDealPerformerCorrect(){
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId()));
  }

  @Test
  void addDealPerformerIncorrect(){
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId()));
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.addDealPerformer(savedUser.getId(), savedPublicDeal.getId()));
  }

  @Test
  void getMyQueueCorrect(){
    Queue queue = dataProvider.getMyQueue(savedUser.getId()).get();
    Assertions.assertEquals(queue.getId(), savedUser.getQueue().getId());
  }

  @Test
  void getMyQueueIncorrect(){
    Queue queue = dataProvider.getDealQueue(UUID.randomUUID()).get();
    Assertions.assertEquals(queue.getId(), savedUser.getQueue().getId());
  }

  @Test
  void manageDealPerformerQueueCorrect(){
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealPerform(savedUser.getId(),savedDeal.getId(),  true));
  }

  @Test
  void manageDealPerformerIncorrect(){
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealPerform(savedUser1.getId(), savedDeal.getId(),  true));
  }

  @Test
  void acceptDealPerformerCorrect() {
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.acceptDealPerform(savedUser.getId(),savedDeal.getId()));
  }

  @Test
  void acceptDealPerformerIncorrect() {
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.acceptDealPerform(savedUser.getId(), savedPublicDeal.getId()));
  }

  @Test
  void refuseDealPerformerCorrect() {
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.refuseDealPerform(savedUser.getId(),savedDeal.getId()));
  }

  @Test
  void refuseDealPerformerIncorrect() {
    dataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.refuseDealPerform(savedUser1.getId(),savedDeal.getId()));
  }

}
