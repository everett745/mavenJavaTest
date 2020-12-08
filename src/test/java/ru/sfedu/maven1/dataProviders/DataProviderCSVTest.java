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
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class DataProviderCSVTest extends TestBase {

  private static final DataProvider dataProvider = DataProviderCSV.getInstance();
  private static Logger log = LogManager.getLogger(DataProviderCSVTest.class);

  private Queue getCorrectTestQueue() throws IOException {
    Queue queue = new Queue();
    long[] items = {1, 2, 3};
    queue.setId(UUID.randomUUID());
    queue.setItems(items);
    return queue;
  }

  private Address getCorrectTestAddress() throws IOException {
    Address address = new Address();
    address.setId(1);
    address.setCity("Ростов-на-Дону");
    address.setRegion("Ростовская область");
    address.setDistrict("Южный");
    return address;
  }

  private User getCorrectTestUser() throws IOException {
    User user = new User();
    user.setId(Constants.TEST_USER_ID);
    user.setName(Constants.TEST_USER_NAME);
    user.setPhone(Constants.TEST_USER_PHONE);
    user.setQueue(getCorrectTestQueue());
    user.setAddress(getCorrectTestAddress());
    return user;
  }

  @Test
  @Order(0)
  void createUserTest() throws IOException {
    User user = getCorrectTestUser();
    Assertions.assertEquals(RequestStatuses.SUCCESS, dataProvider.createUser(user.getName(),
            user.getPhone(),
            user.getAddress())
    );
  }

  @Test
  @Order(1)
  void getUserByIdCorrect() throws IOException {
    User correctUser = getCorrectTestUser();
    Optional<User> user = dataProvider.getUser(Constants.TEST_USER_ID);
    if (user.isEmpty()) {
      Assertions.fail("Fail test -- getUserByIdCorrect");
    }
    log.debug(user.get());
    Assertions.assertEquals(correctUser.getName(), user.get().getName());
    Assertions.assertEquals(correctUser.getPhone(), user.get().getPhone());
    Assertions.assertEquals(correctUser.getQueue(), user.get().getQueue());
    Assertions.assertEquals(correctUser.getAddress(), user.get().getAddress());
  }
}
