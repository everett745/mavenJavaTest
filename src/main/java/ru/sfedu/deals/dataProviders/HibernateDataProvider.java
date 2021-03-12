package ru.sfedu.deals.dataProviders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.Constants;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.enums.RequestStatuses;
import ru.sfedu.deals.model.Address;
import ru.sfedu.deals.model.TestEntity;
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

  public List getAllSchemas() {
    Session session = getSession();
    NativeQuery nativeQuery = session.createNativeQuery(SqlQueriesConstants.SQL_ALL_SCHEMA);
    List resList = nativeQuery.getResultList();
    session.close();
    log.info(String.format("Get chemas size: (%s)", resList != null ? String.valueOf(resList.size()) : null));
    return resList;
  }

  public List getSqlHelp() {
    Session session = getSession();
    NativeQuery nativeQuery = session.createNativeQuery(SqlQueriesConstants.SQL_HELP);
    List resList = nativeQuery.getResultList();
    session.close();
    log.info(String.format("Get chemas size: (%s)", resList != null ? String.valueOf(resList.size()) : null));
    return resList;
  }

  public List getAllTables() {
    Session session = getSession();
    NativeQuery nativeQuery = session.createNativeQuery(SqlQueriesConstants.SQL_ALL_TABLES);
    List resList = nativeQuery.getResultList();
    session.close();
    log.info(String.format("Get chemas size: (%s)", resList != null ? String.valueOf(resList.size()) : null));
    return resList;
  }

  public List getAllUsers() {
    Session session = getSession();
    NativeQuery nativeQuery = session.createNativeQuery(SqlQueriesConstants.SQL_ALL_USERS);
    List resList = nativeQuery.getResultList();
    session.close();
    log.info(String.format("Get chemas size: (%s)", resList != null ? String.valueOf(resList.size()) : null));
    return resList;
  }

  public Optional<Address> getAddressById(@NotNull Long id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(String.format(SqlQueriesConstants.SQL_SELECT_ADDRESS_BY_ID, id));
      Address address = (Address) query.getSingleResult();

      session.close();
      if (address != null) {
        return Optional.of(address);
      } else {
        log.error(Constants.UNDEFINED_ADDRESS);
        return Optional.empty();
      }
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses insertTestEntity(@NotNull TestEntity entity) {
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

  public Optional<List<TestEntity>> selectAllTestEntity() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_TEST_ENTITY);
      List<TestEntity> resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public Optional<TestEntity> selectById(long id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      TestEntity item = session.get(TestEntity.class, id);

      session.close();
      return item != null ? Optional.of(item) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses deleteTestEntity(@NotNull TestEntity entity) {
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

  public RequestStatuses deleteTestEntity(long id) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Optional<TestEntity> entity = selectById(id);
      if (entity.isPresent()) {
        session.delete(entity.get());
        session.getTransaction().commit();
        session.close();
        return RequestStatuses.SUCCESS;
      } else {
        return RequestStatuses.FAILED;
      }
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses updateTestEntity(TestEntity entity) {
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

}
