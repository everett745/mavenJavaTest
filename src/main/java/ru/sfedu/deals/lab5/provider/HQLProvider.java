package ru.sfedu.deals.lab5.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.lab5.Address;
import ru.sfedu.deals.lab5.Company;
import ru.sfedu.deals.lab5.Deal;
import ru.sfedu.deals.lab5.User;
import ru.sfedu.deals.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class HQLProvider implements IProvider {
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

  private <T> Optional<List<T>> selectAllEntity(String sqlQuery) {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(sqlQuery);
      List resList = query.getResultList();
      log.debug(resList);
      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  @Override
  public Optional<List<Address>> selectAllAddress() {
    return selectAllEntity(SqlQueriesConstants.HQL_SELECT_ADDRESS);
  }

  @Override
  public Optional<List<User>> selectAllUsers() {
    return selectAllEntity(SqlQueriesConstants.HQL_SELECT_USER);
  }

  @Override
  public Optional<List<Deal>> selectAllDeals() {
    return selectAllEntity(SqlQueriesConstants.HQL_SELECT_DEAL);
  }

  @Override
  public Optional<List<Company>> selectAllCompanies() {
    return selectAllEntity(SqlQueriesConstants.HQL_SELECT_COMPANY);
  }
}
