package ru.sfedu.maven1.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class DataProviderTests {
  private static final Logger log = LogManager.getLogger(DataProviderTests.class);
  private static IDataProvider IDataProvider;
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
    address.setCity(TestsConstants.TEST_ADDRESS_CITY);
    address.setRegion(TestsConstants.TEST_ADDRESS_REGION);
    address.setDistrict(TestsConstants.TEST_ADDRESS_DISTRICT);
    return address;
  }

  private static Address getTestAddress1() {
    Address address = new Address();
    address.setCity(TestsConstants.TEST_ADDRESS_CITY1);
    address.setRegion(TestsConstants.TEST_ADDRESS_REGION1);
    address.setDistrict(TestsConstants.TEST_ADDRESS_DISTRICT1);
    return address;
  }

  protected static User getTestUserA() {
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
    deal.setName(TestsConstants.TEST_PUBLIC_DEAL_NAME);
    deal.setDescription(TestsConstants.TEST_PUBLIC_DEAL_DESCRIPTION);
    deal.setAddress(getTestAddress());
    deal.setCurrentStatus(TestsConstants.TEST_PUBLIC_DEAL_STATUS);
    deal.setDealType(TestsConstants.TEST_PUBLIC_DEAL_TYPE);
    deal.setObject(TestsConstants.TEST_PUBLIC_DEAL_OBJECT);
    deal.setPrice(TestsConstants.TEST_PUBLIC_DEAL_PRICE);
    return deal;
  }

  static void refreshDB(IDataProvider IDataProviderInstance) {
    IDataProvider = IDataProviderInstance;
    System.err.close();
    System.setErr(System.out);
  }

  static void setUp(IDataProvider IDataProviderInstance) {
    IDataProvider = IDataProviderInstance;
    IDataProvider.clearDB();
    IDataProvider.initDB();

    Address address = getTestAddress();
    Address address1 = getTestAddress1();
    IDataProvider.addAddress(
            address.getCity(),
            address.getRegion(),
            address.getDistrict());

    IDataProvider.addAddress(
            address1.getCity(),
            address1.getRegion(),
            address1.getDistrict());

    User user = getTestUserA();
    IDataProvider.createUser(
            user.getName(),
            user.getPhone(),
            IDataProvider.getAddress(address.getCity()).get().getId());

    User user1 = getTestUserB();
    IDataProvider.createUser(
            user1.getName(),
            user1.getPhone(),
            IDataProvider.getAddress(address.getCity()).get().getId());

    savedUser = IDataProvider.getUsers().get().get(0);
    savedUser1 = IDataProvider.getUsers().get().get(1);

    Deal deal = getTestDeal();
    IDataProvider.createDeal(
            savedUser.getId(),
            deal.getName(),
            deal.getDescription(),
            IDataProvider.getAddress(address.getCity()).get().getId(),
            deal.getDealType(),
            deal.getObject(),
            deal.getPrice());

    PublicDeal publicDeal = getTestPublicDeal();
    IDataProvider.createDeal(
            savedUser1.getId(),
            publicDeal.getName(),
            publicDeal.getDescription(),
            IDataProvider.getAddress(address.getCity()).get().getId(),
            publicDeal.getCurrentStatus(),
            publicDeal.getDealType(),
            publicDeal.getObject(),
            publicDeal.getPrice());

    savedDeal = IDataProvider.getMyDeals(savedUser.getId()).get().get(0);
    savedPublicDeal = IDataProvider.getMyDeals(savedUser1.getId()).get().get(0);
  }

  void createUserCorrect() {
    log.debug("createUserCorrect");
    User user = getTestUserB();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.createUser(
                    user.getName(),
                    user.getPhone(),
                    1)
    );
  }

  void createUserIncorrect() {
    log.debug("createUserIncorrect");
    User user = getTestUserB();

    Assertions.assertThrows(IllegalArgumentException.class, () -> IDataProvider.createUser(
            null,
            user.getPhone(),
            user.getAddress().getId())
    );
  }

  void getUserByIdCorrect() {
    log.debug("getUserByIdCorrect");
    Optional<User> user = IDataProvider.getUser(savedUser.getId());
    Assertions.assertTrue(user.isPresent());
    Assertions.assertEquals(savedUser.getName(), user.get().getName());
    Assertions.assertEquals(savedUser.getPhone(), user.get().getPhone());
    Assertions.assertEquals(savedUser.getAddress(), user.get().getAddress());
    log.debug(user.get());
  }

  void getUserByIdIncorrect() {
    log.debug("getUserByIdIncorrect");
    Optional<User> user = IDataProvider.getUser(UUID.randomUUID().toString());
    Assertions.assertFalse(user.isPresent());
  }

  void editUserCorrect() {
    log.debug("editUserCorrect");
    User user = savedUser;
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.editUser(
                    user.getId(),
                    user.getName(),
                    user.getPhone(),
                    user.getAddress().getId()
            )
    );

    User newUser = IDataProvider.getUser(user.getId()).get();

    Assertions.assertEquals(newUser.getName(), TestsConstants.TEST_USER_2_NAME);
    Assertions.assertEquals(newUser.getPhone(), TestsConstants.TEST_USER_2_PHONE);
  }

  void editUserIncorrect() {
    log.debug("editUserIncorrect");
    User user = savedUser;
    user.setId(UUID.randomUUID().toString());
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);

    Assertions.assertEquals(
            RequestStatuses.NOT_FOUNDED,
            IDataProvider.editUser(
                    user.getId(),
                    user.getName(),
                    user.getPhone(),
                    user.getAddress().getId()
            )
    );
  }

  void deleteUserCorrect() {
    log.debug("deleteUserCorrect");
    User user = savedUser;

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.deleteUser(user.getId()));
  }

  void deleteUserIncorrect() {
    log.debug("deleteUserIncorrect");
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.deleteUser(UUID.randomUUID().toString()));
  }

  void getUsersCorrect() {
    log.debug("getUsersCorrect");
    log.debug(IDataProvider.getUsers());
  }

  void getUsersIncorrect() {
    log.debug("getUsersIncorrect");
    IDataProvider.clearDB();
    Assertions.assertFalse(IDataProvider.getUsers().isPresent());
    log.debug(IDataProvider.getUsers());
  }

  void getAddressesCorrect() {
    log.debug("getAddressesCorrect");
    Assertions.assertTrue(IDataProvider.getAddresses().isPresent());
  }

  void getAddressesIncorrect() {
    log.debug("getAddressesIncorrect");
    IDataProvider.clearDB();
    log.info(IDataProvider.getAddresses());
    Assertions.assertTrue(IDataProvider.getAddresses().isEmpty());
  }

  void getAddressCorrect() {
    log.debug("getAddressCorrect");
    Optional<Address> address = IDataProvider.getAddress(1);
    Assertions.assertTrue(address.isPresent());
    log.debug(address);
  }

  void getAddressIncorrect() {
    log.debug("getAddressIncorrect");
    Optional<Address> address = IDataProvider.getAddress(99999);
    Assertions.assertFalse(address.isPresent());
    log.debug(address);
  }

  void getAddressByNameCorrect() {
    log.debug("getAddressByNameCorrect");
    Optional<Address> address = IDataProvider.getAddress("rost");
    Assertions.assertTrue(address.isPresent());
    log.debug(address);
  }

  void getAddressByNameIncorrect() {
    log.debug("getAddressByNameIncorrect");
    Optional<Address> address = IDataProvider.getAddress("[]asd");
    Assertions.assertFalse(address.isPresent());
    log.debug(address);
  }

  void removeAddressCorrect() {
    log.debug("removeAddressCorrect");
    Assertions.assertEquals(RequestStatuses.SUCCESS, IDataProvider.removeAddress(1));
  }

  void removeAddressIncorrect() {
    log.debug("removeAddressIncorrect");
    Assertions.assertEquals(RequestStatuses.FAILED, IDataProvider.removeAddress(435345));
  }

  void updateAddressCorrect() {
    log.debug("updateAddressCorrect");
    Address address = IDataProvider.getAddresses().get().get(0);

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.updateAddress(address.getId(), "новый город", "новый регион", "новый дистрикт")
    );
    Address updatedAddress = IDataProvider.getAddress(address.getId()).get();
    Assertions.assertEquals("новый город", updatedAddress.getCity());
    Assertions.assertEquals("новый регион", updatedAddress.getRegion());
    Assertions.assertEquals("новый дистрикт", updatedAddress.getDistrict());
  }

  void updateAddressIncorrect() {
    log.debug("updateAddressCorrect");
    Address address = IDataProvider.getAddresses().get().get(0);

    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.updateAddress(9878978, "новый город", "новый регион", "новый дистрикт"));

    Assertions.assertThrows(IllegalArgumentException.class, () ->
                    IDataProvider.updateAddress(1,null,"",""));
  }

  void createDealCorrect() {
    log.debug("createDealCorrect");
    Deal deal = getTestDeal();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.createDeal(
                    savedUser.getId(),
                    deal.getName(),
                    deal.getDescription(),
                    1,
                    deal.getDealType(),
                    deal.getObject(),
                    deal.getPrice())
    );
  }

  void createDealIncorrect() {
    log.debug("createDealIncorrect");
    Deal deal = getTestDeal();
    Assertions.assertThrows(
            IllegalArgumentException.class, () ->
            IDataProvider.createDeal(
                    UUID.randomUUID().toString(),
                    deal.getName(),
                    null,
                    new Address().getId(),
                    deal.getDealType(),
                    deal.getObject(),
                    deal.getPrice())
    );
  }

  void createPublicDealCorrect() {
    log.debug("createPublicDealCorrect");
    PublicDeal publicDeal = getTestPublicDeal();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.createDeal(
                    savedUser1.getId(),
                    publicDeal.getName(),
                    publicDeal.getDescription(),
                    1,
                    publicDeal.getCurrentStatus(),
                    publicDeal.getDealType(),
                    publicDeal.getObject(),
                    publicDeal.getPrice())
    );
  }

  void createPublicDealIncorrect() {
    log.debug("createPublicDealIncorrect");
    PublicDeal publicDeal = getTestPublicDeal();
    Assertions.assertThrows(
            IllegalArgumentException.class, () ->
            IDataProvider.createDeal(
                savedUser1.getId(),
                null,
                publicDeal.getDescription(),
                publicDeal.getAddress().getId(),
                publicDeal.getCurrentStatus(),
                publicDeal.getDealType(),
                publicDeal.getObject(),
                publicDeal.getPrice())
    );
  }

  void getGlobalDealsCorrect() {
    log.debug("getGlobalDealsCorrect");
    Assertions.assertTrue(IDataProvider.getGlobalDeals(savedUser.getId()).isPresent());
    log.debug(IDataProvider.getGlobalDeals(savedUser.getId()).get());
  }

  void getGlobalDealsIncorrect() {
    log.debug("getGlobalDealsIncorrect");
    IDataProvider.clearDB();
    Assertions.assertFalse(IDataProvider.getGlobalDeals(savedUser.getId()).isPresent());
  }

  void getMyDealsCorrect() {
    log.debug("getMyDealsCorrect");
    Assertions.assertTrue(IDataProvider.getMyDeals(savedUser.getId()).isPresent());
    log.debug(IDataProvider.getMyDeals(savedUser.getId()).get());
  }

  void getMyDealsIncorrect() {
    log.debug("getMyDealsIncorrect");
    Assertions.assertFalse(IDataProvider.getMyDeals(UUID.randomUUID().toString()).isPresent());
  }

  void manageDealCorrect() {
    log.debug("manageDealCorrect");
    Assertions.assertTrue(IDataProvider.manageDeal(savedDeal.getId()).isPresent());
    log.debug(IDataProvider.manageDeal(savedDeal.getId()).get());
  }

  void manageDealIncorrect() {
    log.debug("manageDealIncorrect");
    Assertions.assertFalse(IDataProvider.manageDeal(UUID.randomUUID().toString()).isPresent());
    log.debug(IDataProvider.manageDeal(savedDeal.getId()).get());
  }

  void removeDealCorrect() {
    log.debug("removeDealCorrect");
    Assertions.assertEquals(RequestStatuses.SUCCESS, IDataProvider.removeDeal(savedPublicDeal.getId()));
  }

  void removeDealIncorrect() {
    log.debug("removeDealIncorrect");
    Assertions.assertEquals(RequestStatuses.FAILED, IDataProvider.removeDeal(UUID.randomUUID().toString()));
  }

  void updateDealCorrect() {
    log.debug("updateDealCorrect");
    Deal deal = savedDeal;
    deal.setPrice(TestsConstants.TEST_PUBLIC_DEAL_PRICE);
    deal.setName(TestsConstants.TEST_PUBLIC_DEAL_NAME);
    deal.setDescription(TestsConstants.TEST_PUBLIC_DEAL_DESCRIPTION);
    deal.setObject(TestsConstants.TEST_PUBLIC_DEAL_OBJECT);

    Assertions.assertEquals(RequestStatuses.SUCCESS,
            IDataProvider.updateDeal(
                    deal.getId(),
                    deal.getName(),
                    deal.getAddress().getId(),
                    deal.getDescription(),
                    deal.getDealType(),
                    deal.getObject(),
                    deal.getPrice()
            )
    );
    Deal updatedDeal = IDataProvider.manageDeal(deal.getId()).get();

    Assertions.assertEquals(updatedDeal.getName(), TestsConstants.TEST_PUBLIC_DEAL_NAME);
    Assertions.assertEquals(updatedDeal.getPrice(), TestsConstants.TEST_PUBLIC_DEAL_PRICE);
    Assertions.assertEquals(updatedDeal.getDescription(), TestsConstants.TEST_PUBLIC_DEAL_DESCRIPTION);
    Assertions.assertEquals(updatedDeal.getObject(), TestsConstants.TEST_PUBLIC_DEAL_OBJECT);
  }

  void updateDealIncorrect() {
    log.debug("updateDealIncorrect");
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            IDataProvider.updateDeal(
                    null,
                    "",
                    1,
                    "",
                    DealTypes.PURCHASE,
                    ObjectTypes.BUILDING,
                    ""
            )
    );
  }

  void setStatusCorrect(){
    log.debug("setStatusCorrect");
    log.debug(savedPublicDeal);
    Assertions.assertEquals(RequestStatuses.SUCCESS, IDataProvider.setStatus(savedPublicDeal.getId(), DealStatus.AD));
    log.debug(IDataProvider.manageDeal(savedPublicDeal.getId()).get());
  }

  void setStatusIncorrect(){
    log.debug("setStatusIncorrect");
    log.debug(savedDeal);
    Assertions.assertEquals(RequestStatuses.FAILED, IDataProvider.setStatus(savedDeal.getId(), DealStatus.AD));
    log.debug(IDataProvider.manageDeal(savedDeal.getId()).get());
  }

  void addDealRequestCorrect(){
    log.debug("addDealRequestCorrect");
    Assertions.assertEquals(RequestStatuses.SUCCESS, IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId()));
  }

  void addDealRequestIncorrect(){
    log.debug("addDealRequestIncorrect");
    Assertions.assertEquals(RequestStatuses.FAILED, IDataProvider.addDealRequest(UUID.randomUUID().toString(), savedDeal.getId()));
    Assertions.assertEquals(RequestStatuses.FAILED, IDataProvider.addDealRequest(savedUser.getId(), UUID.randomUUID().toString()));
  }

  void getDealQueueCorrect(){
    log.debug("getDealQueueCorrect");
    Queue queue = IDataProvider.getDealQueue(savedDeal.getId()).get();
    Assertions.assertEquals(queue.getId(), savedDeal.getRequests().getId());
    log.debug(queue);
  }

  void getDealQueueIncorrect(){
    log.debug("getDealQueueIncorrect");
    Assertions.assertFalse(IDataProvider.getDealQueue(UUID.randomUUID().toString()).isPresent());
  }

  void manageDealRequestQueueCorrect(){
    log.debug("manageDealRequestQueueCorrect");
    IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.manageDealRequest(savedUser.getId(),savedDeal.getId(),  true));
  }

  void manageDealRequestIncorrect(){
    log.debug("manageDealRequestIncorrect");
    IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.manageDealRequest(UUID.randomUUID().toString(), savedDeal.getId(),  true));
  }

  void acceptDealRequestCorrect() {
    log.debug("acceptDealRequestCorrect");
    IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.acceptDealRequest(savedUser.getId(),savedDeal.getId()));
  }

  void acceptDealRequestIncorrect() {
    log.debug("acceptDealRequestIncorrect");
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.acceptDealRequest(UUID.randomUUID().toString(), savedDeal.getId()));
  }

  void refuseDealRequestCorrect() {
    log.debug("refuseDealRequestCorrect");
    IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.refuseDealRequest(savedUser.getId(), savedDeal.getId()));
  }

  void refuseDealRequestIncorrect() {
    log.debug("refuseDealRequestIncorrect");
    IDataProvider.addDealRequest(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.refuseDealRequest(UUID.randomUUID().toString(),savedDeal.getId()));
  }

  void addDealPerformerCorrect(){
    log.debug("addDealPerformerCorrect");
    Assertions.assertEquals(RequestStatuses.SUCCESS, IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId()));
  }

  void addDealPerformerIncorrect(){
    log.debug("addDealPerformerIncorrect");
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            IDataProvider.addDealPerformer(null, savedDeal.getId())
    );
    Assertions.assertThrows(IllegalArgumentException.class, () ->
            IDataProvider.addDealPerformer(savedUser.getId(), null)
    );
  }

  void getMyQueueCorrect(){
    log.debug("getMyQueueCorrect");
    Queue queue = IDataProvider.getMyQueue(savedUser.getId()).get();
    Assertions.assertEquals(queue.getId(), savedUser.getQueue().getId());
  }

  void getMyQueueIncorrect(){
    log.debug("getMyQueueIncorrect");
    Assertions.assertFalse(IDataProvider.getDealQueue(UUID.randomUUID().toString()).isPresent());
  }

  void manageDealPerformerCorrect(){
    log.debug("manageDealPerformerCorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.manageDealPerform(savedUser.getId(),savedDeal.getId(),  true));
  }

  void manageDealPerformerIncorrect(){
    log.debug("manageDealPerformerIncorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.manageDealPerform(savedUser1.getId(), savedDeal.getId(),  true));
  }

  void acceptDealPerformerCorrect() {
    log.debug("acceptDealPerformerCorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.acceptDealPerform(savedUser.getId(),savedDeal.getId()));
  }

  void acceptDealPerformerIncorrect() {
    log.debug("acceptDealPerformerIncorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.acceptDealPerform(savedUser.getId(), savedPublicDeal.getId()));
  }

  void refuseDealPerformerCorrect() {
    log.debug("refuseDealPerformerCorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            IDataProvider.refuseDealPerform(savedUser.getId(),savedDeal.getId()));
  }

  void refuseDealPerformerIncorrect() {
    log.debug("refuseDealPerformerIncorrect");
    IDataProvider.addDealPerformer(savedUser.getId(), savedDeal.getId());
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            IDataProvider.refuseDealPerform(savedUser1.getId(),savedDeal.getId()));
  }
}
