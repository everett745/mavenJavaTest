package ru.sfedu.deals.hibernate.lab4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.lab4.list.HibernateDataProvider;
import ru.sfedu.deals.lab4.list.User;

import java.util.ArrayList;
import java.util.List;

public class ListTests {
  private static final Logger log = LogManager.getLogger(SetTests.class);
  private static final HibernateDataProvider provider = new HibernateDataProvider();

  private List<String> getPhones() {
    List list = new ArrayList();
    list.add("89004561234");
    list.add("88005553535");
    return list;
  }

  @Test
  void insertUser() {
    log.debug("start test insertUser");
    User user = new User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setPhones(getPhones());

    Assertions.assertEquals(provider.insertUser(user), RequestStatuses.SUCCESS);
  }

  @Test
  void selectAllUsers() {
    log.debug("start test selectAllUsers");
    insertUser();
    Assertions.assertTrue(provider.selectAllUsers().isPresent());
  }

  @Test
  void deleteUsers() {
    log.debug("start test deleteUsers");
    insertUser();
    User user = provider.selectAllUsers().get().get(0);
    Assertions.assertEquals(provider.deleteUser(user), RequestStatuses.SUCCESS);
  }

  @Test
  void updateUsers() {
    log.debug("start test updateUsers");
    insertUser();
    User editedUser = provider.selectAllUsers().get().get(0);
    editedUser.setName("Петр");
    editedUser.setLast_name("Семеныч");

    Assertions.assertEquals(provider.updateUser(editedUser), RequestStatuses.SUCCESS);
  }
}
