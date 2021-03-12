package ru.sfedu.deals.dataProviders;

import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.enums.DealStatus;
import ru.sfedu.deals.enums.DealTypes;
import ru.sfedu.deals.enums.ObjectTypes;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.model.*;

import java.util.List;
import java.util.Optional;
import java.lang.String;

public interface IDataProvider {

  /**
   * Метод для создания нового пользователя
   *
   * @param name      имя пользователя
   * @param phone     телефон пользователя
   * @param addressId идентификатор ранее созданного адреса
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses createUser(
          @NotNull String name,
          @NotNull String phone,
          long addressId);

  /**
   * Получить пользователя по идентификатору
   *
   * @param userId личный идентификатор пользователя
   * @return модель пользователя
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  Optional<User> getUser(@NotNull String userId);

  /**
   * Метод редактирования конкретного пользователя
   *
   * @param addressId идентификатор ранее созданного адреса
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses editUser(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String phone,
          long addressId);

  /**
   * Метод удаления пользователя из системы
   *
   * @param userId личный идентификатор пользователя
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  RequestStatuses deleteUser(@NotNull String userId);

  /**
   * Получить полный список пользователей в системе
   *
   * @return набор объектов пользователей
   */
  Optional<List<User>> getUsers();


  /**
   * Получить полный список адресов в системе
   *
   * @return набор объектов адресов
   */
  Optional<List<Address>> getAddresses();

  /**
   * Получить адрес по идентификатору
   *
   * @param id идентификатор адреса
   * @return объект адреса
   */
  Optional<Address> getAddress(long id);

  /**
   * Получить адрес по ключевому слову "Город"
   *
   * @param city ключевое слово для поиска по названию города
   * @return объект адреса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Address> getAddress(@NotNull String city);

  /**
   * Получить адрес по ключевому слову "Город"
   *
   * @param city     города
   * @param region   область
   * @param district район
   * @return объект адреса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses addAddress(
          @NotNull String city,
          @NotNull String region,
          @NotNull String district
  );


  /**
   * @param id       идентификатор редактируемого адреса
   * @param city     новое название города
   * @param region   новое название региона
   * @param district новое название района
   * @return RequestStatuses статус выполнения запроса
   */
  RequestStatuses updateAddress(
          long id,
          @NotNull String city,
          @NotNull String region,
          @NotNull String district
  );

  /**
   * Получить адрес по ключевому слову "Город"
   *
   * @param id идентификатор адреса, который необходимо удалить
   * @return RequestStatuses статус выполнения запроса
   */
  RequestStatuses removeAddress(long id);

  /**
   * Создать новую обычную сделку сделки
   *
   * @param userId      идентификатор пользователя, создающего сделку
   * @param name        название сделки
   * @param description описание сделки
   * @param address     объект адрес сделки
   * @param dealType    тип сделки
   * @param objectType  объект сделки
   * @param price       цена
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses createDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          long address,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price);

  /**
   * Создать новой публичной сделки
   *
   * @param userId        идентификатор пользователя, создающего сделку
   * @param name          название сделки
   * @param description   описание сделки
   * @param addressId     идентификатор сделки
   * @param currentStatus при передаче статуса сделка становится публичной и ей присваивается указанный статус
   * @param dealType      тип сделки
   * @param objectType    объект сделки
   * @param price         цена
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses createPublicDeal(
          @NotNull String userId,
          @NotNull String name,
          @NotNull String description,
          long addressId,
          @NotNull DealStatus currentStatus,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price);

  /**
   * Получить глобальные сделки для пользователя
   *
   * @param userId личный идентификатор пользователя
   * @return набор объектов сделок
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<List<PublicDeal>> getGlobalDeals(@NotNull String userId);

  /**
   * Получить сделки, созданные пользователем
   *
   * @param userId личный идентификатор пользователя
   * @return набор объектов сделок
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<List<Deal>> getMyDeals(@NotNull String userId);

  /**
   * Получить сделку
   *
   * @param id идентификатор сделки, которую необходимо получить
   * @return объект сделки
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Deal> manageDeal(@NotNull String id);

  /**
   * Удалить сделку
   *
   * @param id идентификатор сделки, которую необходимо удалить
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  RequestStatuses removeDeal(@NotNull String id);


  /**
   * @param id          идентификатор сделки, которную неоходимо изменить
   * @param name        новое имя
   * @param addressId   новый адрес (его идентификатор)
   * @param description новое описание сделки
   * @param dealType    новый тип сделки
   * @param objectType  новый объект сделки
   * @param price       новая цена
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses updateDeal(
          @NotNull String id,
          @NotNull String name,
          long addressId,
          @NotNull String description,
          @NotNull DealTypes dealType,
          @NotNull ObjectTypes objectType,
          @NotNull String price);

  /**
   * Обновить статус сделки
   *
   * @param id        Идентификатор сделки
   * @param newStatus новый статус
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses setStatus(@NotNull String id,
                            @NotNull DealStatus newStatus);

  /**
   * Отправить запрос на передачу сделки себе
   *
   * @param id     идентификатор сделки
   * @param userId идентификатор пользователя
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses addDealRequest(@NotNull String userId,
                                 @NotNull String id);

  /**
   * Получить список "запросов" на сделку
   *
   * @param id идентификатор сделки
   * @return объект сделки
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Queue> getDealQueue(@NotNull String id);

  /**
   * Управление запросом на передачу сделки
   *
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id     идентификатор сделки, на которную направлен запрос
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses manageDealRequest(@NotNull String userId,
                                    @NotNull String id,
                                    boolean accept);

  /**
   * Принять запрос пользователя на передачу сделки ему
   *
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id     идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses acceptDealRequest(@NotNull String userId,
                                    @NotNull String id);

  /**
   * Отклонить запрос пользователя на передачу сделки ему
   *
   * @param userId идентификатор пользователя, отправившего запрос
   * @param id     идентификатор сделки, на которную направлен запрос
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses refuseDealRequest(@NotNull String userId,
                                    @NotNull String id);

  /**
   * Отправить запрос пользователю на принятие сделки
   *
   * @param id     идентификатор сделки, учавствующей в запросе
   * @param userId идентификатор пользователя, кому направлен запрос
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses addDealPerformer(@NotNull String userId,
                                   @NotNull String id);

  /**
   * Получить очередь из сделок, которые пытаются передать пользователю
   *
   * @param userId идентификатор пользователя
   * @return объект очереди
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Queue> getMyQueue(@NotNull String userId);

  /**
   * Управление запросом на принятие сделки
   *
   * @param userId идентификатор пользователя
   * @param id     идентификатор сделки, учавствующей в запросе
   * @param accept true - принять запрос, false - отклонить запрос
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses manageDealPerform(@NotNull String userId,
                                    @NotNull String id,
                                    boolean accept);

  /**
   * Принять сделку
   *
   * @param userId идентификатор пользователя
   * @param id     идентификатор сделки
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses acceptDealPerform(@NotNull String userId,
                                    @NotNull String id);

  /**
   * Отклонить сделку
   *
   * @param userId идентификатор пользователя
   * @param id     идентификатор сделки
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses refuseDealPerform(@NotNull String userId,
                                    @NotNull String id);

  /**
   * Создать компанию
   *
   * @param user объект пользователя (владельца компании)
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  RequestStatuses createCompany(@NotNull User user);

  /**
   * Получить компанию сотрудника
   *
   * @param id идентификатор пользователя
   * @return Optional<Company> объект компании
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Company> getUserCompany(@NotNull String id);

  /**
   * Получить компанию по идентификатору
   *
   * @param id идентификатор компании
   * @return Optional<Company> объект компании
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  Optional<Company> getCompany(@NotNull String id);

  /**
   * Редактирование компании
   *
   * @param updatedCompany объект компании, которную необходимо изменить
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  RequestStatuses updateCompany(@NotNull Company updatedCompany);

  /**
   * Удалить компанию
   *
   * @param id идентификатор компании
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если передать null в качестве аргумента
   */
  RequestStatuses deleteCompany(@NotNull String id);

  /**
   * Создать новую очередь
   * @param queue объект очереди
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses createQueue(@NotNull Queue queue);

  /**
   * Получить очередь
   * @param id идентификатор очереди
   * @return объект очереди Queue
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  Optional<Queue> getQueue(@NotNull String id);

  /**
   * Обновить очередь
   * @param queue объект очереди
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses updateQueue(@NotNull Queue queue);

  /**
   * Удалить очередь
   * @param id идентификатор очереди
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses deleteQueue(@NotNull String id);

  /**
   * Добавить новую историю
   * Добавить историю
   * @param dealHistoryItem объект истории
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses addDealHistory(@NotNull DealHistory dealHistoryItem);

  /**
   * Получить историю
   * @param id идентификатор истории
   * @return объект истории DealHistory
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  Optional<DealHistory> getDealHistory(@NotNull String id);

  /**
   * Обновить историю
   * @param updatedDH объект истории
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses updateDealHistoryItem(@NotNull DealHistory updatedDH);

  /**
   * Удалить истоирю
   * @param id идентификатор истории
   * @return RequestStatuses статус выполнения запроса
   * @throws IllegalArgumentException если один из аргументов будет null
   */
  RequestStatuses removeDealHistoryByDeal(@NotNull String id);

  /**
   * Очиститка бд
   */
  void clearDB();

  /**
   * Инициализация
   */
  void initDB();

}
