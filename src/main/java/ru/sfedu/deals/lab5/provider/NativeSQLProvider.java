package ru.sfedu.deals.lab5.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;
import ru.sfedu.deals.utils.HibernateUtil;

import java.util.List;
import java.util.Optional;


public class NativeSQLProvider implements IProvider{
  private static IDataProvider INSTANCE = null;
  private static final Logger log = LogManager.getLogger(NativeSQLProvider.class);

  public static IDataProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DataProviderJdbc();
    }
    return INSTANCE;
  }

  private Session getSession() {
    return HibernateUtil.getSessionFactory().openSession();
  }

  @Override
  public Optional<List<Address>> selectAllAddress() {
    Session session = getSession();
    session.beginTransaction();

    try {
      String sql = SqlQueriesConstants.NSQL_SELECT_ADDRESS;
      NativeQuery query = session.createSQLQuery(sql).addEntity(Address.class);
      List resList = query.list();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (HibernateException e) {
      e.printStackTrace();
      return Optional.empty();
    } finally {
      session.close();
    }
  }

  @Override
  public Optional<List<User>> selectAllUsers() {
    Session session = getSession();
    session.beginTransaction();

    try {
      String sql = SqlQueriesConstants.NSQL_SELECT_USER;
      NativeQuery query = session.createSQLQuery(sql).addEntity(User.class);
      List resList = query.list();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (HibernateException e) {
      e.printStackTrace();
      return Optional.empty();
    } finally {
      session.close();
    }
  }

  @Override
  public Optional<List<Deal>> selectAllDeals() {
    Session session = getSession();
    session.beginTransaction();

    try {
      String sql = SqlQueriesConstants.NSQL_SELECT_DEAL;
      NativeQuery query = session.createSQLQuery(sql).addEntity(Deal.class);
      List resList = query.list();

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (HibernateException e) {
      e.printStackTrace();
      return Optional.empty();
    } finally {
      session.close();
    }
  }

  @Override
  public Optional<List<Company>> selectAllCompanies() {
    Session session = getSession();
    session.beginTransaction();

    try {
      String sql = SqlQueriesConstants.NSQL_SELECT_COMPANY;
      NativeQuery query = session.createSQLQuery(sql).addEntity(Company.class);
      List resList = query.list();
      log.debug(resList.get(0));

      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (HibernateException e) {
      e.printStackTrace();
      return Optional.empty();
    } finally {
      session.close();
    }
  }
}
