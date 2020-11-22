package ru.sfedu.maven1;

import ru.sfedu.maven1.model.User;

public class TestBase {

  public User createUser(long id, String name) {
    User user = new User();
    user.setId(id);
    user.setName(name);
    return user;
  }

}
