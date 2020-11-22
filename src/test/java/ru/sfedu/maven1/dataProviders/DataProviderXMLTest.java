package ru.sfedu.maven1.dataProviders;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.sfedu.maven1.TestBase;
import ru.sfedu.maven1.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataProviderXMLTest extends TestBase {

  @Test
  public void testInsertUserSuccess() throws Exception {
    System.out.println("testInsertUserSuccess");
    List<User> listUsers = new ArrayList<User>() ;

    User user1 = createUser(1, "Test1");
    User user2 = createUser(2, "Test2");
    User user3 = createUser(3, "Test3");


    listUsers.add(user1);
    listUsers.add(user2);
    listUsers.add(user3);

    DataProviderXML instance = new DataProviderXML();

    instance.insertUser(listUsers);

    Assertions.assertEquals(user1, instance.getUserById(1));
  }

  @Test
  public void testInsertUserFailure() throws Exception {
    System.out.println("testInsertUserSuccess");
    List<User> listUsers = new ArrayList<User>() ;

    User user1 = createUser(1, "Test1");
    User user2 = createUser(2, "Test2");
    User user3 = createUser(3, "Test3");


    listUsers.add(user1);
    listUsers.add(user2);
    listUsers.add(user3);

    DataProviderXML instance = new DataProviderXML();

    instance.insertUser(listUsers);

    Assertions.assertNull(instance.getUserById(4));
  }

}
