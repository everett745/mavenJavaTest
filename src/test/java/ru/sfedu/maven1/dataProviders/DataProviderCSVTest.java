package ru.sfedu.maven1.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.TestBase;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Deal;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataProviderCSVTest extends TestBase {

  private static final DataProvider dataProvider = DataProviderCSV.getInstance();
  private static Logger log = LogManager.getLogger(DataProviderCSVTest.class);

  private Queue getCorrectTestQueue() {
    Queue queue = new Queue();
    queue.setId(TestsConstants.TEST_USER_QUEUE_ID);
    queue.setItems(TestsConstants.TEST_USER_QUEUE_ITEMS);
    return queue;
  }

  private Address getCorrectTestAddress() {
    Address address = new Address();
    address.setId(TestsConstants.TEST_ADDRESS_ID);
    address.setCity(TestsConstants.TEST_ADDRESS_CITY);
    address.setRegion(TestsConstants.TEST_ADDRESS_REGION);
    address.setDistrict(TestsConstants.TEST_ADDRESS_DISTRICT);
    return address;
  }

  private User getCorrectTestUserA() {
    User user = new User();
    user.setId(TestsConstants.TEST_USER_1_ID);
    user.setName(TestsConstants.TEST_USER_1_NAME);
    user.setPhone(TestsConstants.TEST_USER_1_PHONE);
    user.setQueue(getCorrectTestQueue());
    user.setAddress(getCorrectTestAddress());
    return user;
  }

  private User getCorrectTestUserB() {
    User user = new User();
    user.setId(TestsConstants.TEST_USER_2_ID);
    user.setName(TestsConstants.TEST_USER_2_NAME);
    user.setPhone(TestsConstants.TEST_USER_2_PHONE);
    user.setQueue(getCorrectTestQueue());
    user.setAddress(getCorrectTestAddress());
    return user;
  }

  @Test
  @Order(0)
  void createUserTest() {
    User user = getCorrectTestUserB();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createUser(
            user.getName(),
            user.getPhone(),
            user.getAddress())
    );
  }

  @Test
  Optional<User> getUserFromList() {
    Optional<List<User>> usersListOptional = dataProvider.getUsers();
    if (usersListOptional.isPresent()) {
      List<User> usersList = usersListOptional.get();
      UUID testedUser = usersList.get(0).getId();

      return dataProvider.getUser(testedUser);
    } else {
      Assertions.fail(TestsConstants.ERROR_USERS_LIST_EMPTY);
      return Optional.empty();
    }
  }

  @Test
  @Order(1)
  void getUserByIdCorrect() {
    Optional<User> optionalUser = getUserFromList();
    if (optionalUser.isPresent()) {
      Optional<User> user = dataProvider.getUser(optionalUser.get().getId());
      if (user.isEmpty()) {
        Assertions.fail(TestsConstants.ERROR_GET_USER_BY_ID);
      }
      log.info(user.get());
    } else {
      Assertions.fail(TestsConstants.ERROR_GET_USER_FROM_LIST);
    }
  }

  @Test
  Optional<Deal> getDealFromList() {
    Optional<List<Deal>> dealsListOptional = dataProvider.getMyDeals(TestsConstants.TEST_USER_1_ID);
    if (dealsListOptional.isPresent()) {
      List<Deal> dealsList = dealsListOptional.get();
      UUID testedUser = dealsList.get(0).getId();

      return dataProvider.manageDeal(testedUser);
    } else {
      Assertions.fail(TestsConstants.ERROR_USERS_LIST_EMPTY);
      return Optional.empty();
    }
  }

  @Test
  void createDeal() {
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.createDeal(
                    TestsConstants.TEST_USER_1_ID,
                    TestsConstants.TEST_DEAL_NAME,
                    TestsConstants.TEST_DEAL_DESCRIPTION,
                    getCorrectTestAddress(),
                    TestsConstants.TEST_DEAL_TYPE,
                    TestsConstants.TEST_DEAL_OBJECT,
                    TestsConstants.TEST_DEAL_PRICE)
    );
  }

  @Test
  void removeDeal() {
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.removeDeal(getDealFromList().get().getId())
    );
  }

  @Test
  void updateDeal() {
    Deal deal = getDealFromList().get();
    deal.setName(TestsConstants.TEST_DEAL_2_NAME);
    deal.setDescription(TestsConstants.TEST_DEAL_2_DESCRIPTION);

    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.updateDeal(deal));

    Deal newDeal = dataProvider.manageDeal(deal.getId()).get();

    Assertions.assertEquals(
            TestsConstants.TEST_DEAL_2_NAME,
            newDeal.getName());
    Assertions.assertEquals(
            TestsConstants.TEST_DEAL_2_DESCRIPTION,
            newDeal.getDescription());

  }

  @Test
  void setStatus() {
    return;
  }


  @Test
  void addDealRequest() {
    Deal deal = getDealFromList().get();
    log.info(deal.getRequests());
    dataProvider.addDealRequest(deal.getId(), TestsConstants.TEST_USER_1_ID);
    log.info(dataProvider.manageDeal(deal.getId()).get().getRequests());
  }

  @Test
  void addExistingDealRequest() {
    Deal deal = getDealFromList().get();
    log.info(deal.getRequests());
    dataProvider.addDealRequest(deal.getId(), TestsConstants.TEST_USER_1_ID);
    dataProvider.addDealRequest(deal.getId(), TestsConstants.TEST_USER_1_ID);
    log.info(dataProvider.manageDeal(deal.getId()).get().getRequests());
  }

  @Test
  void getDealQueue() {
    Deal deal = getDealFromList().get();
    log.info(dataProvider.getDealQueue(deal.getId()));
  }

  @Test
  void acceptDealRequest() {
    Deal deal = getDealFromList().get();
    Queue queue = deal.getRequests();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealRequest(queue.getItems().get(0), deal.getId(), true));
  }

  @Test
  void refuseDealRequest() {
    Deal deal = getDealFromList().get();
    Queue queue = deal.getRequests();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealRequest(queue.getItems().get(0), deal.getId(), false));
  }


  @Test
  void addDealPerformer() {
    Deal deal = getDealFromList().get();
    User user = getUserFromList().get();

    log.info(user.getQueue());
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.addDealPerformer(deal.getId(), user.getId()));

    log.info(dataProvider.getMyQueue(user.getId()).get());
  }

  @Test
  void addExistDealPerformer() {
    Deal deal = getDealFromList().get();
    User user = getUserFromList().get();

    log.info(user.getQueue());
    dataProvider.addDealPerformer(deal.getId(), user.getId());
    dataProvider.addDealPerformer(deal.getId(), user.getId());

    log.info(dataProvider.getMyQueue(user.getId()).get());
  }

  @Test
  void getUserQueue() {
    User user = getUserFromList().get();
    log.info(dataProvider.getMyQueue(user.getId()));
  }

  @Test
  void acceptDealPerform() {
    User user = getUserFromList().get();
    Queue queue = user.getQueue();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealPerform(user.getId(), queue.getItems().get(0), true));
  }

  @Test
  void acceptDealExistPerformer() {
    User user = getUserFromList().get();
    Queue queue = user.getQueue();
    dataProvider.manageDealPerform(user.getId(), queue.getItems().get(0), true);
    Assertions.assertEquals(
            RequestStatuses.FAILED,
            dataProvider.manageDealPerform(user.getId(), queue.getItems().get(0), true));
  }

  @Test
  void refuseDealPerform() {
    User user = getUserFromList().get();
    Queue queue = user.getQueue();
    Assertions.assertEquals(
            RequestStatuses.SUCCESS,
            dataProvider.manageDealPerform(user.getId(), queue.getItems().get(0),  false));
  }

}
