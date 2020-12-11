package ru.sfedu.maven1.dataProviders;

import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.Address;
import ru.sfedu.maven1.model.Deal;
import ru.sfedu.maven1.model.Queue;
import ru.sfedu.maven1.model.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataProvider {

  /*User*/

  /**
   * Метод для создания нового пользователя
   * @param name имя пользователя
   * @param phone телефон пользователя
   * @param address обьект "Address" для нового пользователя
   * @return RequestStatuses
   */
  RequestStatuses createUser(
          @NotNull final String name,
          @NotNull final String phone,
          @NotNull final Address address);

  /**
   * Получить отдельного пользователя по личному идентификатору
   * @param userId личный идентификатор пользователя
   * @return модель пользователя
   */
  Optional<User> getUser(@NotNull UUID userId);

  /**
   * Метод редактирования конкретного пользователя
   * @param user модель пользователя
   * @return RequestStatuses
   */
  RequestStatuses editUser(@NotNull final User user);

  /**
   * Метод удаления пользователя из системы
   * @param userId личный идентификатор пользователя
   * @return RequestStatuses
   */
  RequestStatuses deleteUser(@NotNull UUID userId);

  /**
   * Получить полный список пользователей в системе
   * @return набор объектов пользователей
   */
  Optional<List<User>> getUsers();


  /*Address*/
  /**
   * Добавить новый адрес
   * @param city город
   * @param region область
   * @param district район
   * @return RequestStatuses
   */
  RequestStatuses addAddress(
          @NotNull final String city,
          @NotNull final String region,
          @NotNull final String district);

  /**
   * Получить полный список адресов в системе
   * @return набор объектов адресов
   */
  Optional<List<Address>> getAddresses();

  /**
   * Получить адрес по идентификатору
   * @param id идентификатор адреса
   * @return объект адреса
   */
  Optional<Address> getAddress(@NotNull final long id);

  /**
   * Получить адрес по ключевому слову "Город"
   * @param city ключевое слово для поиска по названию города
   * @return объект адреса
   */
  Optional<Address> getAddress(@NotNull final String city);


  /*Queue*/
  /**
   * Добавить новую очередь
   * @param queue объект очереди
   * @return RequestStatuses
   */
  RequestStatuses addQueue(@NotNull final Queue queue);

  /**
   * Получить очередь по идентификатору
   * @param id идентификатор очереди
   * @return объект очереди
   */
  Optional<Queue> getQueue(@NotNull final UUID id);


  /*Deal*/
  /**
   * Метод создания новой сделки
   * @param userId идентификатор пользователя, создающего сделку
   * @param name название сделки
   * @param description описание сделки
   * @param address объект адрес сделки
   * @param dealType тип сделки
   * @param objectType объект сделки
   * @param price цена
   * @return RequestStatuses
   */
  RequestStatuses createDeal(
          @NotNull final UUID userId,
          @NotNull final String name,
          @NotNull final String description,
          @NotNull final Address address,
          @NotNull final DealTypes dealType,
          @NotNull final ObjectTypes objectType,
          @NotNull final String price);

  /**
   * Получить глобальные сделки для пользователя
   * @param id личный идентификатор пользователя
   * @return набор объектов сделок
   */
  Optional<List<Deal>> getGlobalDeals(@NotNull final UUID id);

  /**
   * Получить сделки, созданные пользователем
   * @param id личный идентификатор пользователя
   * @return набор объектов сделок
   */
  Optional<List<Deal>> getMyDeals(@NotNull final UUID id);

  /**
   * Получить сделку
   * @param id идентификатор сделки, которую необходимо получить
   * @return объект сделки
   */
  Optional<Deal> manageDeal(@NotNull final UUID id);

  /**
   * Удалить сделку
   * @param id идентификатор сделки, которую необходимо удалить
   * @return RequestStatuses
   */
  RequestStatuses removeDeal(@NotNull final UUID id);

  /**
   * Обновить информацию сделки
   * @param deal объект сделки
   * @return RequestStatuses
   */
  RequestStatuses updateDeal(@NotNull final Deal deal);

  /**
   * Обновить статус сделки
   * @param id Идентификатор сделки
   * @param newStatus новый статус
   * @return RequestStatuses
   */
  RequestStatuses setStatus(@NotNull final UUID id,
                            @NotNull final DealStatus newStatus);


  /* Deal Requests */
  /**
   * Отправить запрос владельцу сделки на передачу сделки себе
   * @param id идентификатор сделки
   * @param userId идентификатор пользователя
   * @return RequestStatuses
   */
  RequestStatuses addDealRequest(@NotNull final UUID id,
                                 @NotNull final UUID userId);

  /**
   * Получить список "запросов" на сделку
   * @param id идентификатор сделки
   * @return объект сделки
   */
  Optional<Queue> getDealQueue(@NotNull final UUID id);

  /**
   * Управление запросом на передачу сделки
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses
   */
  RequestStatuses manageDealRequest(@NotNull final UUID userId,
                                 @NotNull final UUID id,
                                 @NotNull final boolean accept);

  /**
   * Принять запрос пользователя на передачу сделки ему
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses acceptDealRequest(@NotNull final UUID userId,
                                    @NotNull final UUID id);

  /**
   * Отклонить запрос пользователя на передачу сделки ему
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses refuseDealRequest(@NotNull final UUID userId,
                                    @NotNull final UUID id);


  /* Deal Performs */
  /**
   * Отправить запрос пользователю на принятие сделки
   * @param id идентификатор сделки, учавствующей в запросе
   * @param userId идентификатор пользователя, кому направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses addDealPerformer(@NotNull final UUID id,
                                 @NotNull final UUID userId);

  /**
   * Получить очередь из сделок, которые пытаются передать пользователю
   * @param userId идентификатор пользователя
   * @return объект очереди
   */
  Optional<Queue> getMyQueue(@NotNull final UUID userId);

  /**
   * Управление запросом на принятие сделки
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки, учавствующей в запросе
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses
   */
  RequestStatuses manageDealPerform(@NotNull final UUID userId,
                                    @NotNull final UUID id,
                                    final boolean accept);

  /**
   * Принять сделку
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки
   * @return RequestStatuses
   */
  RequestStatuses acceptDealPerform(@NotNull final UUID userId,
                                    @NotNull final UUID id);

  /**
   * Отклонить сделку
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки
   * @return RequestStatuses
   */
  RequestStatuses refuseDealPerform(@NotNull final UUID userId,
                                    @NotNull final UUID id);

}
