package ru.sfedu.maven1;

import ru.sfedu.maven1.model.User;

import java.util.UUID;

public class TestBase {

  public User createUser(long id, String name) {
    User user = new User();
    UUID uuid = UUID.randomUUID();
    user.setId(uuid);
    user.setName(name);
    return user;
  }

}
