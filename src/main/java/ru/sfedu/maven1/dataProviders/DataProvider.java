package ru.sfedu.maven1.dataProviders;

import org.jetbrains.annotations.NotNull;
import ru.sfedu.maven1.enums.DealStatus;
import ru.sfedu.maven1.enums.DealTypes;
import ru.sfedu.maven1.enums.ObjectTypes;
import ru.sfedu.maven1.enums.RequestStatuses;
import ru.sfedu.maven1.model.*;

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
          @NotNull String name,
          @NotNull String phone,
          @NotNull Address address);

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
  RequestStatuses editUser(@NotNull User user);

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
   * Получить полный список адресов в системе
   * @return набор объектов адресов
   */
  Optional<List<Address>> getAddresses();

  /**
   * Получить адрес по идентификатору
   * @param id идентификатор адреса
   * @return объект адреса
   */
  Optional<Address> getAddress(@NotNull long id);

  /**
   * Получить адрес по ключевому слову "Город"
   * @param city ключевое слово для поиска по названию города
   * @return объект адреса
   */
  Optional<Address> getAddress(@NotNull String city);


  /*Deal*/
  /**
   * Создать новую обычную сделку сделки
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
          @NotNull UUID userId,
          @NotNull String name,
          @NotNull String description,
          @NotNull Address address,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price);

  /**
   * Создать новой публичной сделки
   * @param userId идентификатор пользователя, создающего сделку
   * @param name название сделки
   * @param description описание сделки
   * @param address объект адрес сделки
   * @param currentStatus при передаче статуса сделка становится публичной и ей присваивается указанный статус
   * @param dealType тип сделки
   * @param objectType объект сделки
   * @param price цена
   * @return RequestStatuses
   */
  RequestStatuses createDeal(
          @NotNull UUID userId,
          @NotNull String name,
          @NotNull String description,
          @NotNull Address address,
          @NotNull DealStatus currentStatus,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price);

  /**
   * Получить глобальные сделки для пользователя
   * @param userId личный идентификатор пользователя
   * @return набор объектов сделок
   */
  Optional<List<PublicDeal>> getGlobalDeals(@NotNull UUID userId);

  /**
   * Получить сделки, созданные пользователем
   * @param userId личный идентификатор пользователя
   * @return набор объектов сделок
   */
  Optional<List<Deal>> getMyDeals(@NotNull UUID userId);

  /**
   * Получить сделку
   * @param id идентификатор сделки, которую необходимо получить
   * @return объект сделки
   */
  Optional<Deal> manageDeal(@NotNull UUID id);

  /**
   * Удалить сделку
   * @param id идентификатор сделки, которую необходимо удалить
   * @return RequestStatuses
   */
  RequestStatuses removeDeal(@NotNull UUID id);

  /**
   * Обновить информацию сделки
   * @param deal объект сделки
   * @return RequestStatuses
   */
  RequestStatuses updateDeal(@NotNull Deal deal);

  /**
   * Обновить статус сделки
   * @param id Идентификатор сделки
   * @param newStatus новый статус
   * @return RequestStatuses
   */
  RequestStatuses setStatus(@NotNull UUID id,
                            @NotNull DealStatus newStatus);


  /* Deal Requests */
  /**
   * Отправить запрос на передачу сделки себе
   * @param id идентификатор сделки
   * @param userId идентификатор пользователя
   * @return RequestStatuses
   */
  RequestStatuses addDealRequest(@NotNull UUID userId,
                                 @NotNull UUID id);

  /**
   * Получить список "запросов" на сделку
   * @param id идентификатор сделки
   * @return объект сделки
   */
  Optional<Queue> getDealQueue(@NotNull UUID id);

  /**
   * Управление запросом на передачу сделки
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses
   */
  RequestStatuses manageDealRequest(@NotNull UUID userId,
                                 @NotNull UUID id,
                                 boolean accept);

  /**
   * Принять запрос пользователя на передачу сделки ему
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses acceptDealRequest(@NotNull UUID userId,
                                    @NotNull UUID id);

  /**
   * Отклонить запрос пользователя на передачу сделки ему
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses refuseDealRequest(@NotNull UUID userId,
                                    @NotNull UUID id);


  /* Deal Performs */
  /**
   * Отправить запрос пользователю на принятие сделки
   * @param id идентификатор сделки, учавствующей в запросе
   * @param userId идентификатор пользователя, кому направлен запрос
   * @return RequestStatuses
   */
  RequestStatuses addDealPerformer(@NotNull UUID userId,
                                   @NotNull UUID id);

  /**
   * Получить очередь из сделок, которые пытаются передать пользователю
   * @param userId идентификатор пользователя
   * @return объект очереди
   */
  Optional<Queue> getMyQueue(@NotNull UUID userId);

  /**
   * Управление запросом на принятие сделки
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки, учавствующей в запросе
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses
   */
  RequestStatuses manageDealPerform(@NotNull UUID userId,
                                    @NotNull UUID id,
                                    boolean accept);

  /**
   * Принять сделку
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки
   * @return RequestStatuses
   */
  RequestStatuses acceptDealPerform(@NotNull UUID userId,
                                    @NotNull UUID id);

  /**
   * Отклонить сделку
   * @param userId идентификатор пользователя
   * @param id идентификатор сделки
   * @return RequestStatuses
   */
  RequestStatuses refuseDealPerform(@NotNull UUID userId,
                                    @NotNull UUID id);
  /**
   * Очистить базу
   */
  void deleteAll();

}
