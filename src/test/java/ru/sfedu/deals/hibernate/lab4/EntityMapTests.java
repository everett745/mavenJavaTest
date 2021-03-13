package ru.sfedu.deals.hibernate.lab4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.lab4.entityMap.HibernateDataProvider;
import ru.sfedu.deals.lab4.entityMap.User;
import ru.sfedu.deals.model.AddressT;

import java.util.HashMap;
import java.util.Map;

public class EntityMapTests {
  private static final Logger log = LogManager.getLogger(SetTests.class);
  private static final HibernateDataProvider provider = new HibernateDataProvider();

  private AddressT genAddress1() {
    AddressT address = new AddressT();
    address.setDistrict("Московская область");
    address.setCity("Москова");
    address.setRegion("Центральный");
    return address;
  }

  private AddressT genAddress2() {
    AddressT address = new AddressT();
    address.setDistrict("Ростовская область");
    address.setCity("Ростов-не-Дону");
    address.setRegion("Южный");
    return address;
  }

  private Map<String, AddressT> getAddresses() {
    Map<String, AddressT> map = new HashMap<>();
    map.put("Work", genAddress1());
    map.put("Last work", genAddress2());
    return map;
  }

  @Test
  void insertUser() {
    log.debug("start test insertUser");
    User user = new User();
    user.setName("Иван");
    user.setLast_name("Иванов");
    user.setAddresses(getAddresses());

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
