package ru.sfedu.deals.lab5.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;
import ru.sfedu.deals.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class DataProvider {
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(HQLProvider.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().openSession();
  }

  public <T> RequestStatuses insertEntity(T entity) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.save(entity);
      session.getTransaction().commit();

      session.close();

      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public <T> RequestStatuses deleteEntity(T entity) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.delete(entity);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  private Optional<User> selectUserById(String id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      User item = session.get(User.class, id);

      return item != null ? Optional.of(item) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses deleteUser(String id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Optional<User> user = selectUserById(id);
      if (user.isEmpty()) return RequestStatuses.FAILED;

      session.delete(user.get());
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public <T> RequestStatuses updateEntity(T entity) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.update(entity);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses insertAddress(Address address) {
    return insertEntity(address);
  }

  public RequestStatuses deleteAddress(Address address) {
    return deleteEntity(address);
  }

  public RequestStatuses updateAddress(Address address) {
    return updateEntity(address);
  }

  public RequestStatuses insertUser(User user) {
    return insertEntity(user);
  }

  public RequestStatuses deleteUser(User user) {
    return deleteEntity(user);
  }

  public RequestStatuses updateUser(User user) {
    return updateEntity(user);
  }

  public RequestStatuses insertDeal(Deal deal) {
    return insertEntity(deal);
  }

  public RequestStatuses deleteDeal(Deal deal) {
    return deleteEntity(deal);
  }

  public RequestStatuses updateDeal(Deal deal) {
    return updateEntity(deal);
  }

  public RequestStatuses insertCompany(Company company) {
    log.debug(company);
    return insertEntity(company);
  }

  public RequestStatuses deleteCompany(Company company) {
    return deleteEntity(company);
  }

  public RequestStatuses updateCompany(Company company) {
    return updateEntity(company);
  }
}
