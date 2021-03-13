package ru.sfedu.deals.lab4.entityMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class HibernateDataProvider {
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(HibernateDataProvider.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().openSession();
  }

  public RequestStatuses insertUser(@NotNull User user) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.save(user);
      session.getTransaction().commit();

      session.close();

      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public Optional<List<User>> selectAllUsers() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_LAB4_USER);
      List resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public Optional<User> selectUserById(String id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      User item = session.get(User.class, id);

      session.close();
      return item != null ? Optional.of(item) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses deleteUser(@NotNull User user) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.delete(user);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
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

  public RequestStatuses updateUser(@NotNull User account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.update(account);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

}
