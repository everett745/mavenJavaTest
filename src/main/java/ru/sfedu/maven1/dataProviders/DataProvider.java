package ru.sfedu.maven1.dataProviders;

import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataProvider {

  /*User*/
  RequestStatuses createUser(
          @NotNull final String name,
          @NotNull final String phone,
          @NotNull final Address address);

  Optional<User> getUser(@NotNull UUID userId);

  RequestStatuses editUser(@NotNull final User user);

  RequestStatuses deleteUser(@NotNull UUID userId);

  Optional<List<User>> getUserList();


  /*Address*/
  RequestStatuses addAddress(
          @NotNull final String city,
          @NotNull final String country,
          @NotNull final String region);

  Optional<List<Address>> getAddresses();
  Optional<Address> getAddress(long id);
  Optional<Address> getAddress(String city);


  /*Queue*/
  RequestStatuses addQueue(
          @NotNull final Queue queue);

  Optional<Queue> getQueue(UUID id);

}
