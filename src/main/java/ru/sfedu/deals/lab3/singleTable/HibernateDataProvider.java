package ru.sfedu.deals.lab3.singleTable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import ru.sfedu.deals.SqlQueriesConstants;
import ru.sfedu.deals.dataProviders.DataProviderJdbc;
import ru.sfedu.deals.dataProviders.IDataProvider;
import ru.sfedu.deals.enums.RequestStatuses;

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

  public RequestStatuses insertAccount(@NotNull Account account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.save(account);
      session.getTransaction().commit();

      session.close();

      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public Optional<List<CreditAccount>> selectAllCreditAccount() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_CREDIT_ACCOUNT);
      List<CreditAccount> resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public Optional<List<DebitAccount>> selectAllDebitAccount() {
    Session session = getSession();
    session.beginTransaction();

    try {
      Query query = session.createQuery(SqlQueriesConstants.SQL_SELECT_DEBIT_ACCOUNT);
      List<DebitAccount> resList = query.getResultList();

      session.close();
      return resList != null ? Optional.of(resList) : Optional.empty();
    } catch (Exception e) {
      log.error(e);
      session.close();
      return Optional.empty();
    }
  }

  public RequestStatuses deleteAccount(@NotNull Account account) {
    Session session = getSession();
    session.beginTransaction();

    try {
      session.delete(account);
      session.getTransaction().commit();
      session.close();
      return RequestStatuses.SUCCESS;
    } catch (Exception e) {
      log.error(e);
      session.close();
      return RequestStatuses.FAILED;
    }
  }

  public RequestStatuses updateAccount(@NotNull Account account) {
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
